package com.tharsol.endtb.util

import android.text.TextUtils
import com.tharsol.endtb.BuildConfig

object Constants {
    const val SERVICE_BASE_URL = "ServiceBaseUrl"
    const val COMPANY_ID = "CompanyId"
    const val ALLOW_SCREEN_CAPTURE = "AllowScreenCapture"
    const val EMPLOYEE_DOWN_HIERARCHY_EXECUTED = "EmployeeDownHierarchyExecuted"
    const val RESPONSE_OK = "OK"
    val OS = "Android - " + BuildConfig.VERSION_NAME
    const val TEAM_NAME = "name"
    const val TEAM_ROLE = "role"
    const val PRODUCT_NAME = "product_name"
    const val MONTH = "month"
    const val FROM = "from"
    const val TO = "to"
    const val EMPLOYEE_ID = "EmployeeId"
    const val PRE_FROM = "pre_from"
    const val PRE_TO = "pre_to"
    const val MALE = "MALE"
    const val MALE_ID = 1
    const val FEMALE_ID = 2
    const val FEMALE = "Female"
    const val PREFERENCES = "MrepPrefs"
    const val COMPANY = "company"
    const val GROUP = "group"
    const val ROLE = "role"
    const val IS_NEW_BIE = "NewBie"
    const val CHANGE_PIN = "ChangePin"
    const val IS_PIN_LOCK_ACTIVATED = "IsPinLockActivated"
    const val IS_FINGER_LOCK_ACTIVATED = "IsFingerLockActivated"
    const val PLANNED = "PLANNED"
    const val UNPLANNED = "UNPLANNED"
    const val HAPPENED = "HAPPENED"
    const val NOT_HAPPENED = "NOT HAPPENED"
    const val EMPLOYEE_REFERENCE_ID = "EmployeeReferenceId"
    const val PASSWORD = "Password"
    const val EMPLOYEE_NAME = "EmployeeName"
    const val LAST_SYNC_DATE = "LastSyncDate"
    const val MULTIPLE_CHOICE_MULTIPLE_SELECTION = "MULTIPLE_CHOICE_MULTIPLE_SELECTION"
    const val MULTIPLE_CHOICE_SINGLE_SELECTION = "MULTIPLE_CHOICE_SINGLE_SELECTION"
    const val OPEN_ENDED_QUESTION_TEXT = "OPEN_ENDED_QUESTION_TEXT"
    const val OPEN_ENDED_QUESTION_LIST = "OPEN_ENDED_QUESTION_LIST"
    const val NUMERIC_SCALE_RESPONSE = "NUMERIC_SCALE_RESPONSE"
    const val TODO_TASK = "TODO_TASK"
    const val MESSAGE_TASK = "MESSAGE_TASK"
    const val EVENT_TYPE = "Event Type"
    const val SPECIALITY_TYPE = "D_SPEC"
    const val EVENT_TYPE_CITY = "Cities"
    const val EVENT_TIME = "EventTimings"
    const val EVENT_QUALIFICATION = "qualifications"
    const val DOCTORS = "Doctors"
    const val CITIES = "Cities"
    const val ALL_TEAMS = "ATEAMS"
    const val CITY = "City"
    const val DISTRIBUTOR = "Distributor"
    const val WORK_PLANS = "WorkPlans"
    const val PRODUCTS = "Products"
    const val PERSONS = "Persons"
    const val STATUS_SUGGESTION_PENDING = "PENDING"
    const val STATUS_SUGGESTION_CLOSED = "CLOSED"
    const val STATUS_SUGGESTION_APPROVED = "Approved"
    const val STATUS_LOCATION_CLOSED = "C"
    const val STATUS_LOCATION_PENDING = "P"
    const val STATUS_LOCATION_EDIT = "E"
    const val REQUEST_TYPES = "RequestTypes"
    const val STAGES = "AllStages"
    const val CLINIC = 2
    const val CLINIC_TEXT = "CLINIC"
    const val HOSPITAL = 1
    const val HOSPITAL_TEXT = "HOSPITAL"
    const val APP_VERSION_CODE = "AppVersionCode"
    const val REASONS = "Reasons"
    const val RANK = "Rank"
    const val GIFTS = "Gifts"
    const val EVENT_TIMINGS = "EventTimings"
    const val APP_VERSION = "AppVersion"
    const val CALL_CHOICE = "CallChoice"
    const val FROM_Date = "FromTime"
    const val TO_DATE = "ToTime"
    const val PRODUCT_ID = "ProductId"
    const val SALE = "Sale"
    const val AREA_NAME = "AreaName"
    const val DAY_VIEW = "DayView"
    const val MONTH_VIEW = "MonthView"
    const val LIST_TYPE = "ListType"
    const val TEAM = "TEAM"
    const val BACKUP_EMAIL = "BackupEmail"
    const val UPCOMING_SCHEDULE_SYNC = "UpcomingScheduleSync"
    const val ATTENDANCE = "Attendance"
    const val UNTITLED_NAME = "UNTITLED"
    const val URL_LOGO = "LogoUrl"
    fun getPendingStatus(status: String?): Int {
        return if (TextUtils.equals(status, STATUS_SUGGESTION_PENDING)) 0 else 1
    }

    fun getStatus(status: String?): Int {
        return when (status) {
            STATUS_SUGGESTION_PENDING -> 0
            STATUS_SUGGESTION_CLOSED -> 1
            else -> -1
        }
    }

    fun getGenderName(gender: Int?): String {

        return when (gender) {
            1 -> {
                "Male"
            }
            2 -> {
                "Female"
            }
            3 -> {
                "Other"
            }
            else -> {
                ""
            }
        }
    }
}