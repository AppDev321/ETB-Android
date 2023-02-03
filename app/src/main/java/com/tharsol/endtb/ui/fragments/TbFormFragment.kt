package com.tharsol.endtb.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tharsol.endtb.R
import com.tharsol.endtb.database.AppDb
import com.tharsol.endtb.databinding.FragmentTbFormBinding
import com.tharsol.endtb.deserialize.Patient
import com.tharsol.endtb.extenstions.getNumberWithoutSpace
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.model.RefreshProduct
import com.tharsol.endtb.model.SearchedPatient
import com.tharsol.endtb.model.SingleItem
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.ui.FrameActivity
import com.tharsol.endtb.ui.widget.Dialogs
import com.tharsol.endtb.util.*
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TbFormFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
    private val REQUEST_PATIENT_CODE: Int = 4568
    private val REQUEST_PICTURE_CODE: Int = 1245
    private val REQUEST_PICK_PICTURE_CODE: Int = 1246
    private lateinit var binding: FragmentTbFormBinding
    private var progressDialog: SweetAlertDialog? = null
    private var pictureFile: String? = null
    private var district: SingleItem? = null
//    private var locality: SingleItem? = null
//    private val productViews = ArrayList<TbProductItem>()
//    private var products: List<Product>? = null
    private var listener: TopSearchListener? = null

    var mPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (activity is TopSearchListener)
        {
            listener = activity as TopSearchListener
        }
        retainInstance = true
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTbFormBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.disable.setOnClickListener {
            listener?.getSearchView()?.error = it.context.getString(R.string.search_patient_first)
            val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
            listener?.getSearchView()?.startAnimation(animation)
        }
//        loadProducts()
        enableActivity(false)
        onClickListeners()

    }

    companion object
    {
        val TAG = TbFormFragment::class.java.name
        fun newInstance(): TbFormFragment
        {
            return TbFormFragment()
        }
    }

    private fun onClickListeners()
    {
        listener?.getSearchView()?.setOnClickListener {
            if (!ValidationUtils.validateInternet(requireContext())) return@setOnClickListener
            FrameActivity.startActivityForResult(this, getString(R.string.title_search), SearchFragment.TAG, REQUEST_PATIENT_CODE)
        }
        binding.btnConfirm.setOnClickListener {
            submit()
        }
//        binding.btnAddProduct.setOnClickListener(this)
        binding.ivPrescription.setOnClickListener(this)
        binding.tvPrescription.setOnClickListener {

            pickImage()
        }
        binding.editTextDistrict.setOnClickListener(this)
//        binding.editTextLocality.setOnClickListener(this)
        binding.cbFemale.setOnCheckedChangeListener(this)
        binding.cbMale.setOnCheckedChangeListener(this)
        binding.cbOther.setOnCheckedChangeListener(this)
        binding.editTextDistrict.editor.setOnClickListener { showDistrictDialog() }
//        binding.editTextLocality.editor.setOnClickListener {
//            if (TextUtils.isEmpty(binding.editTextDistrict.text))
//            {
//                binding.editTextDistrict.setError(getString(R.string.error_district_first))
//            }
//            else
//            {
//                showLocalityDialog()
//            }
//        }
    }

    private fun submit()
    {

        if (!ValidationUtils.validateMobile(binding.editTextMobile.editor))
        {
            binding.editTextMobile.setError(getString(R.string.error_mobile_msg))
            ViewUtils.requestFocus(binding.editTextMobile)
            return
        }

        if (!ValidationUtils.validateName(binding.editTextPatientName.editor))
        {
            binding.editTextPatientName.setError(getString(R.string.error_name_msg))
            ViewUtils.requestFocus(binding.editTextPatientName)
            return
        }

        if (!ValidationUtils.validateAge(binding.editTextPatientAge.editor))
        {
            binding.editTextPatientAge.setError(getString(R.string.error_age_msg))
            ViewUtils.requestFocus(binding.editTextPatientAge)
            return
        }

        if (!isGenderSelected())
        {
            binding.cbMale.error = getString(R.string.error_gender)
            ViewUtils.requestFocus(binding.cbMale)
            return
        }
        binding.cbMale.error = null

        if (!ValidationUtils.validateCNIC(binding.editTextCnicNumber.editor))
        {
            binding.editTextCnicNumber.setError(getString(R.string.error_cnic))
            ViewUtils.requestFocus(binding.editTextCnicNumber)
            return
        }

        if (district == null || district?.value == 0)
        {
            binding.editTextDistrict.setError(getString(R.string.error_district_first))
            ViewUtils.requestFocus(binding.editTextDistrict)
            return
        }

//        if (locality == null || locality?.value == 0)
//        {
//            binding.editTextLocality.setError(getString(R.string.error_empty_field))
//            ViewUtils.requestFocus(binding.editTextLocality)
//            return
//        }

        if (TextUtils.isEmpty(pictureFile))
        {
            Utilities.showToast(requireContext(), getString(R.string.error_picture))
            return
        }

//        if (productViews.isNullOrEmpty())
//        {
//            Utilities.showToast(requireContext(), getString(R.string.error_transaction))
//            return
//        }

        if (isEmptyProduct()) return

        if (mPatient == null)
        {
            mPatient = Patient(mobileNumber = binding.editTextMobile.getNumberWithoutSpace(), name = binding.editTextPatientName.text.toString(), age = binding.editTextPatientAge.text.toString().toInt(), gender = getGender(), cnic = binding.editTextCnicNumber.text.toString())
        }
        mPatient?.image = pictureFile
        mPatient?.addedBy = UserData.user?.userId
//        mPatient?.transactions = productViews.map { it.getItem() }
        println("PATIENT ID:" + mPatient?.id)
        request()
    }

    private fun request()
    {
        lifecycleScope.launch {
            progressDialog = ProgressDialogUtils.createProgressDialog(requireContext(), "Saving...")
            progressDialog?.setOnShowListener { dialog ->
                val alertDialog = dialog as SweetAlertDialog
                val text: TextView = alertDialog.findViewById<View>(R.id.title_text) as TextView
                text.textAlignment = View.TEXT_ALIGNMENT_CENTER
                text.isSingleLine = false
            }
            progressDialog?.show()
            try
            {
                val response = ApiAdapter.apiClient.addPatientTransaction(mPatient!!)
                if (response.isValid())
                {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!)
                    {
                        ProgressDialogUtils.showSuccess(progressDialog!!, getString(R.string.app_name), innerResponse.activityInfo) {
                            it.dismiss()
                            mPatient = null
                            clearFormData()
                        }
                    }
                    else
                    {
                        ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), innerResponse.activityInfo)
                    }
                }
                else
                {
                    ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), response.message())
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(requireContext(), "Error Occurred: ${e.message}", Toast.LENGTH_LONG).show()
                progressDialog?.dismiss()
            }
        }
    }

    private fun getGender(): Int
    {
        return when
        {
            binding.cbMale.isChecked   ->
            {
                1
            }
            binding.cbFemale.isChecked ->
            {
                2
            }
            else                       ->
            {
                3
            }
        }
    }

    @Subscribe fun loadEvent(item: RefreshProduct)
    {
//        loadProducts()
    }

    private fun isGenderSelected(): Boolean
    {
        return binding.cbMale.isChecked || binding.cbOther.isChecked || binding.cbFemale.isChecked
    }


    private fun enableActivity(enable: Boolean)
    {
        binding.disable.visibility = if (!enable) View.VISIBLE else View.GONE
        binding.editTextMobile.isEnabled = enable
        binding.editTextPatientName.isEnabled = enable
        binding.editTextPatientAge.isEnabled = enable
        binding.editTextCnicNumber.isEnabled = enable
        enableGenderViews(true)
        binding.editTextDistrict.isEnabled = enable
//        binding.editTextLocality.isEnabled = enable
    }


    override fun onClick(v: View)
    {
        pickImage()
//        when (v.id)
//        {
////            R.id.btnAddProduct                       ->
////            {
////                onAddProduct()
////            }
//            R.id.ivPrescription, R.id.tvPrescription ->
//            {
//                pickImage()
//            }
//        }
    }

    private fun onAddProduct()
    {
//        if (products.isNullOrEmpty())
//        {
////            loadProducts()
//            if (products.isNullOrEmpty())
//            {
//                Utilities.showToast(requireContext(), getString(R.string.error_product_sync))
//                return
//            }
//        }
        if (isEmptyProduct()) return
//        val item = object : TbProductItem(requireContext(), mPatient?.id, products!!)
//        {
//            override fun onRemove(item: TbProductItem)
//            {
//                this@TbFormFragment.binding.productsContainer.removeView(item.getView())
//                productViews.remove(item)
//            }
//        }
//        productViews.add(item)
//        this@TbFormFragment.binding.productsContainer.addView(item.getView())
    }

    private fun isEmptyProduct(): Boolean
    { //        if (productViews.isEmpty()) return true
//        productViews.forEach {
//            if (it.isEmpty()) return true
//        }
        return false
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean)
    {
        if (isChecked)
        {
            when (buttonView.id)
            {
                R.id.cbFemale ->
                {
                    binding.cbMale.isChecked = false
                    binding.cbOther.isChecked = false
                }
                R.id.cbMale   ->
                {
                    binding.cbFemale.isChecked = false
                    binding.cbOther.isChecked = false
                }
                R.id.cbOther  ->
                {
                    binding.cbFemale.isChecked = false
                    binding.cbMale.isChecked = false
                }

            }
        }
    }

    private fun disableGenders(gender: Int)
    {
        enableGenderViews(false)

        when (gender)
        {
            1 ->
            {
                binding.cbMale.isChecked = true
            }
            2 ->
            {
                binding.cbFemale.isChecked = true
            }
            3 ->
            {
                binding.cbOther.isChecked = true
            }
        }
    }

    private fun enableGenderViews(value: Boolean)
    {
        binding.cbMale.isEnabled = value
        binding.cbOther.isEnabled = value
        binding.cbFemale.isEnabled = value
    }

    @Subscribe(threadMode = ThreadMode.MAIN) fun onEvent(item: SearchedPatient)
    {
        enableActivity(true)
    }

    private fun showDistrictDialog()
    {
        val items = AppDb.get().localities().getDistrict()
        val dialogs = Dialogs(requireContext())
        dialogs.addOnItemSelectedListener(object : Dialogs.AddItemSelectedListener
        {


            override fun onItemSelected(dialogInterface: DialogInterface?, singleItem: SingleItem?)
            {
                district = singleItem
                binding.editTextDistrict.setText(singleItem?.title)
                binding.editTextDistrict.setError(null)
//                binding.editTextLocality.setText("");
            }

        })
        dialogs.showSingleChoiceItemsDialog(items!!.map {
            SingleItem(it.districtId!!, it.districtName)
        }, getString(R.string.choose_an_item), false)
    }

    private fun showLocalityDialog()
    {
        val items = AppDb.get().localities().getLocalitys(district?.value)
        val dialogs = Dialogs(requireContext())
        dialogs.addOnItemSelectedListener(object : Dialogs.AddItemSelectedListener
        {
            override fun onItemSelected(dialogInterface: DialogInterface?, singleItem: SingleItem?)
            {
//                locality = singleItem
//                binding.editTextLocality.setText(singleItem?.title)
//                binding.editTextLocality.setError(null)
            }

        })
        dialogs.showSingleChoiceItemsDialog(items!!.map {
            SingleItem(it.localityId!!, it.localityName)
        }, getString(R.string.choose_an_item), false)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        if ((requestCode == REQUEST_PICTURE_CODE || requestCode == REQUEST_PICK_PICTURE_CODE) && resultCode == Activity.RESULT_OK)
        {


            if (requestCode === REQUEST_PICK_PICTURE_CODE) {
                if (data != null) {
                    val contentURI = data.data
                    try {

                        binding.ivPrescription.setImageURI(contentURI)

                        try {
                            val bytes = requireContext().contentResolver.openInputStream(contentURI!!)!!.readBytes()

                            pictureFile = Base64.getEncoder().encodeToString(bytes)
                        } catch (error: IOException) {
                            error.printStackTrace() // This exception always occurs
                        }

//                        pictureFile = encodeImage(MediaStore.Images.Media.getBitmap(requireContext().contentResolver, contentURI))

//                        Utilities.showToast(requireContext(), pictureFile)
                    } catch (e: IOException) {
                        e.printStackTrace()

                    }
                }
            } else if (requestCode === REQUEST_PICTURE_CODE) {
                val thumbnail: Bitmap? = data?.extras?.get("data") as Bitmap

                val byteArrayOutputStream = ByteArrayOutputStream()

                thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encoded: String = Base64.getEncoder().encodeToString(byteArray)
                pictureFile = encoded
                binding.ivPrescription.setImageBitmap(thumbnail)
                //saveImage(thumbnail)

            }
        }
        else if (requestCode == REQUEST_PATIENT_CODE && resultCode == Activity.RESULT_OK)
        {
            pictureFile = null
            mPatient = data?.getParcelableExtra(SearchFragment.KEY_PATIENT)
            if (mPatient != null)
            {
                fillFormData(mPatient!!)
            }
            else
            {
                clearFormData()
                enableActivity(true)
                val text = data?.getStringExtra(SearchFragment.KEY_MOBILE_NUMBER)
                text.let {
                    binding.editTextMobile.setText(it)
                }
            }
        }
        listener?.getSearchView()?.error = null
    }


    private fun fillFormData(patient: Patient)
    {
        binding.editTextMobile.setText(patient.mobileNumber?.substring(4))
        binding.editTextMobile.editor.isEnabled = TextUtils.isEmpty(patient.mobileNumber)
        binding.editTextPatientName.setText(patient.name)
        binding.editTextPatientName.editor.isEnabled = TextUtils.isEmpty(patient.name)
        binding.editTextPatientAge.setText(patient.age.toString())
        binding.editTextPatientAge.editor.isEnabled = patient.age == 0
        binding.editTextCnicNumber.setText(patient.cnic)
        binding.editTextCnicNumber.editor.isEnabled = TextUtils.isEmpty(patient.cnic)
        patient.gender?.let {
            disableGenders(it)
        }
//        loadData(patient.locality)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val b = baos.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }
    private fun clearFormData()
    {
        binding.editTextMobile.setText("")
        binding.editTextPatientName.text.clear()
        binding.editTextPatientAge.text.clear()
        binding.editTextCnicNumber.text.clear()
        district = null
        binding.editTextDistrict.text.clear()
//        locality = null
//        binding.editTextLocality.text.clear()
//        productViews.clear()
//        binding.productsContainer.removeAllViews()
        pictureFile = null
        Glide.with(this).load(R.drawable.ic_placeholde_p).apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(resources.getDimension(R.dimen.dimen_size_8dp).toInt(), 3))).into(binding.ivPrescription)
        enableActivity(false)
    }

    private fun loadData(locality: Int?)
    {
        val item = AppDb.get().localities().getLocality(locality)
        item?.let {
            district = SingleItem(item.districtId, item.districtName)
            binding.editTextDistrict.editor.isEnabled = district == null
//            this@TbFormFragment.locality = SingleItem(item.localityId, item.localityName)
//            binding.editTextLocality.editor.isEnabled = this@TbFormFragment.locality == null
        }
        binding.editTextDistrict.setText(district?.title)
//        binding.editTextLocality.setText(this@TbFormFragment.locality?.title)
    }

    var currentPhotoPath: String? = null

    @Throws(IOException::class) private fun createImageFile(): File?
    { // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = App.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent()
    {
        Toast.makeText(requireContext(), "dispatchTakePictureIntent", Toast.LENGTH_LONG).show()
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 546)
            return
        }

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent -> // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(App.context.packageManager)?.also { // Create the File where the photo should go
                    val photoFile: File? = try
                    {
                        createImageFile()
                    }
                    catch (ex: IOException)
                    { // Error occurred while creating the File

                        null
                    } // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(requireContext(), "com.tharsol.endtb.fileprovider", it)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_PICTURE_CODE)
                    }
                }
        }
    }

    /*protected fun loadPlaceImage(url: Uri)
    {
        if (url == null)
        {
            Glide.with(this).load(R.drawable.ic_placeholde_p)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(resources.getDimension(
                        R.dimen.dimen_size_8dp).toInt(), 3))).into(binding.ivPrescription!!)
            return
        }
        Glide.with(this).load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(resources.getDimension(
                    R.dimen.dimen_size_8dp).toInt(), 3))).placeholder(R.drawable.ic_placeholde_p)
            .error(R.drawable.ic_placeholde_p).into(binding.ivPrescription!!)
    }

    private fun decodeBase64(base64: String)
    {
        lifecycleScope.launch {
            val asyn = async(Dispatchers.Default) { ImageUtil.convertBitmap(base64) }
            loadPlaceImage(asyn.await())
        }
    }*/

    private fun loadProducts()
    {
        lifecycleScope.launch {
            try
            {

                val asyn = async(Dispatchers.Default) {
                    AppDb.get().products().getProducts()
                }
//                products = asyn.await()
            }
            catch (ex: Exception)
            {

            }
        }
    }

    /*protected fun loadPlaceImage(url: Bitmap)
    {
        if (url == null)
        {
            Glide.with(this).load(R.drawable.ic_placeholde_p).into(binding.ivPrescription!!)
            return
        }
        Glide.with(this).load(url).placeholder(R.drawable.ic_placeholde_p)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(16, 0)))
            *//*.error(R.drawable.error_place_holder)*//*.into(binding.ivPrescription)
    }*/

    private fun loadPlaceImage(url: String?)
    {
        if (TextUtils.isEmpty(url))
        {
            Glide.with(this).load(R.drawable.ic_placeholde_p).into(binding.ivPrescription)
            return
        }
        Glide.with(this).load(url).placeholder(R.drawable.ic_placeholde_p).into(binding.ivPrescription)
    }

    private fun pickImage()
    {
        selectImage()
//        showImagePickerOptions()
    }

    private fun showImagePickerOptions()
    {
        dispatchTakePictureIntent()
    }

    interface TopSearchListener
    {
        fun getSearchView(): EditText?
    }

    private fun selectImage() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 546)
            return
        }


        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                val storageDir: File? = App.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val f = File(storageDir, "temp.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                //pic = f;
                takePhotoFromCamera()
            } else if (options[item] == "Choose from Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                choosePhotoFromGallary()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        this.startActivityForResult(galleryIntent, REQUEST_PICK_PICTURE_CODE)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_PICTURE_CODE)
    }
}