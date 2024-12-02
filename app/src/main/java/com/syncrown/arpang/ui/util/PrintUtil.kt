package com.syncrown.arpang.ui.util

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syncrown.arpang.ui.commons.CommonFunc.Companion.byteArrToHex
import com.syncrown.sdk.OnInforListener
import com.syncrown.sdk.PrintPort
import kotlinx.coroutines.delay
import java.io.UnsupportedEncodingException

class PrintUtil private constructor() {
    lateinit var printPP: PrintPort

    //TODO 연결상태 체크
    private val _connectionState = MutableLiveData<Boolean>()
    val connectionState: LiveData<Boolean> get() = _connectionState

    //TODO 배터리 레벨
    private val _batteryVolume = MutableLiveData<Int>()
    val batteryVol: LiveData<Int> get() = _batteryVolume

    //TODO 프린터 정보
    private val _printerStatus = MutableLiveData<PaperStatusEnum>()
    val status: LiveData<PaperStatusEnum> get() = _printerStatus

    //TODO 용지 이름 정보
    private val _paperInfo = MutableLiveData<String>()
    val paperInfo: LiveData<String> get() = _paperInfo

    private val onPrinterStatusListener: OnInforListener = object : OnInforListener {
        override fun onOutPaper() {}

        override fun onOpenCover() {}

        override fun onOverHeat() {}

        override fun onLowVal() {}

        override fun onNormal() {}

        override fun onPrinting() {}

        override fun onUID(bytes: ByteArray) {
            Log.i("jung", byteArrToHex(bytes))
        }
    }

    fun init(context: Activity) {
        printPP = object : PrintPort(context, onPrinterStatusListener) {
            override fun onConnected() {
                Log.e("jung", "onConnected")
                _connectionState.value = true
            }

            override fun ondisConnected() {
                Log.e("jung", "ondisConnected")
                _connectionState.value = false
                _printerStatus.value = PaperStatusEnum.NONE
                _batteryVolume.value = 0
                _paperInfo.value = ""
            }

            override fun onsateOFF() {}

            override fun onsateOn() {}

            override fun onReceive(type: Int, bytes: ByteArray) {
                Log.e("jung", "type = " + type + ", data = " + byteArrToHex(bytes))
                when (type) {
                    SEARCHTYPE_MAC -> {
                        Log.e("jung", "SEARCHTYPE_MAC : ${byteArrToHex(bytes)}")
                        val btDeviceMac = byteArrToHex(bytes)
                    }

                    SEARCHTYPE_ENCINFOR -> {
                        val info: String
                        try {
                            info = String(bytes, charset("GB2312"))
                            Log.e("jung", "Paper Infor: $info")

                            if (info.isNotEmpty() && bytes[0].toInt() != 0x00) {
                                if (info.contains("/")) {
                                    val separate = info.split("/")

                                    val paperName = separate[0]
                                    val paperSN = separate[1]

                                    _paperInfo.value = paperName
                                } else {
                                    _paperInfo.value = ""
                                }
                            } else {
                                _paperInfo.value = ""
                            }
                        } catch (e: UnsupportedEncodingException) {
                            throw RuntimeException(e)
                        }
                    }

                    SEARCHTYPE_PRINTERSTATUS ->
                        /**Others (judge the printer status according to "bit")
                         *Bit 0: 1: printing
                         *Position 1: 1: paper hatch cover open
                         *Bit 2: 1: paper shortage
                         *Bit 3: 1: low battery voltage
                         *Bit 4: 1: print head overheating
                         *Bit 5: default (default 0)
                         *Bit 6: default (default 0)
                         *Bit 7: default (default 0)
                         */
                        if (bytes.size == 1) {
                            var isOk = true
                            var type: PaperStatusEnum = PaperStatusEnum.NONE
                            if ((bytes[0].toInt() and 0x01) == 0x01) {
                                type = PaperStatusEnum.PRINTING
                                isOk = false
                            }

                            if ((bytes[0].toInt() and 0x02) == 0x02) {
                                type = PaperStatusEnum.PAPER_HATCH_COVER_OPEN
                                isOk = false
                            }

                            if ((bytes[0].toInt() and 0x04) == 0x04) {
                                type = PaperStatusEnum.PAPER_OUT
                                isOk = false
                                _paperInfo.value = ""
                            }

                            if ((bytes[0].toInt() and 0x08) == 0x08) {
                                type = PaperStatusEnum.LOW_BATTERY_VOLTAGE
                                isOk = false
                            }

                            if ((bytes[0].toInt() and 0x10) == 0x10) {
                                type = PaperStatusEnum.PRINT_HEAD_OVERHEATING
                                isOk = false
                            }

                            if (isOk) {
                                type = PaperStatusEnum.OK
                            }

                            _printerStatus.value = type
                        }

                    SEARCHTYPE_BATTERYVOL -> {
                        if (bytes.size == 2) {
                            _batteryVolume.value = bytes[1].toInt()
                        }
                    }
                }
            }
        }
    }

    fun connect(name: String, address: String?) {
        if (printPP.isConnected) {
            printPP.disconnect()
        }

        printPP.connect(name, address)
    }

    fun disConnect() {
        if (printPP.isConnected) {
            printPP.disconnect()
        }
    }

    fun sendBatteryVol() {
        if (!printPP.isConnected) {
            return
        }
        printPP.printerBatteryVol()
    }

    fun sendPrinterStatus() {
        if (!printPP.isConnected) {
            return
        }
        printPP.printerStatus()
    }

    fun sendPaperInfo() {
        if (!printPP.isConnected) {
            return
        }
        printPP.getEncryptedInfor()
    }

    fun sendMac() {
        if (!printPP.isConnected) {
            return
        }
        printPP.printerMac()
    }

    fun printBitmap(bitmap: Bitmap, density: Int, count: Int) {
        printPP.printerWakeup()
        printPP.enablePrinter()
        printPP.backoffPaper()

        printPP.printBitmap(bitmap, 0)
        printPP.printerPosition()

        printPP.offsetAuto()
        printPP.stopPrintJob()
    }

    companion object {
        @Volatile
        private var instance: PrintUtil? = null

        fun getInstance(): PrintUtil {
            return instance ?: synchronized(this) {
                instance ?: PrintUtil().also { instance = it }
            }
        }
    }

}