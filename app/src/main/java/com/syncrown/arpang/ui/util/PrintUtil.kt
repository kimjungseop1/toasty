package com.syncrown.arpang.ui.util

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syncrown.arpang.ui.commons.CommonFunc.Companion.byteArrToHex
import com.syncrown.sdk.OnInforListener
import com.syncrown.sdk.PrintPort
import java.io.UnsupportedEncodingException
import java.util.Locale

class PrintUtil private constructor() {
    lateinit var printPP: PrintPort

    private var isReceiverEnabled = false

    //TODO 연결상태 체크
    private val _connectionState = MutableLiveData<Boolean>()
    val connectionState: LiveData<Boolean> get() = _connectionState

    //TODO 배터리 레벨
    private val _batteryVolume = MutableLiveData<Int>()
    val batteryVol: LiveData<Int> get() = _batteryVolume

    //TODO 프린터 정보
    private val _printerStatus = MutableLiveData<PaperStatusEnum>()
    val status: LiveData<PaperStatusEnum> get() = _printerStatus

    private val onPrinterStatusListener: OnInforListener = object : OnInforListener {
        override fun onOutPaper() {
            Log.i("jung", "Paper shortage")
        }

        override fun onOpenCover() {
            Log.i("jung", "Open the cover")
        }

        override fun onOverHeat() {
            Log.i("jung", "overheated")
        }

        override fun onLowVal() {
            Log.i("jung", "Low voltage")
        }

        override fun onNormal() {
            Log.i("jung", "onNormal")
        }

        override fun onPrinting() {
            Log.i("jung", "onPrinting")
        }

        override fun onUID(bytes: ByteArray) {
            Log.i("jung", byteArrToHex(bytes))
        }
    }

    fun init(context: Activity) {
        printPP = object : PrintPort(context, onPrinterStatusListener) {
            override fun onConnected() {
                if (isReceiverEnabled) {
                    Log.e("jung", "onConnected")
                    _connectionState.value = true
                }
                isReceiverEnabled = false
            }

            override fun ondisConnected() {
                if (isReceiverEnabled) {
                    Log.e("jung", "ondisConnected")
                    _connectionState.value = false
                    _printerStatus.value = PaperStatusEnum.NONE
                    _batteryVolume.value = 0
                }
                isReceiverEnabled = false
            }

            override fun onsateOFF() {
                Log.e("jung", "onsateOFF")
            }

            override fun onsateOn() {
                Log.e("jung", "onsateOn")
            }

            override fun onReceive(type: Int, bytes: ByteArray) {
                if (bytes == null) return

                Log.e("jung", "type = " + type + ", data = " + byteArrToHex(bytes))
                when (type) {
                    SEARCHTYPE_ISCHARGE -> if (bytes.size == 1) {
                        if (bytes[0].toInt() == 0x01) {
                            Log.e("jung", "charging")
                        } else if (bytes[0].toInt() == 0x02) {
                            Log.e("jung", "charge over")
                        } else {
                            Log.e("jung", "no charge")
                        }
                    }

                    SEARCHTYPE_UID -> {
                        Log.e("jung", "UID:" + byteArrToHex(bytes))
                    }

                    SEARCHTYPE_ENCINFOR -> {
                        val strBuilder = java.lang.StringBuilder()
                        for (b in bytes) {
                            strBuilder.append(
                                String.format("%02x", b).uppercase(Locale.getDefault())
                            )
                            strBuilder.append(" ")
                        }
                        Log.e("jung", "Paper Infor:$strBuilder")
                    }

                    SEARCHTYPE_PRINTERSTATUS ->
                        /*Others (judge the printer status according to "bit")
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

                            Log.e("jung", "SEARCHTYPE_PRINTERSTATUS : " + type.name)

                            _printerStatus.value = type
                        }

                    SEARCHTYPE_BATTERYVOL -> if (bytes.size == 2) {
                        _batteryVolume.value = bytes[1].toInt()
                    }

                    SEARCHTYPE_MAC -> {
                        var mac_address: String? = null
                        mac_address = byteArrToHex(bytes)
                        Log.e("jung", mac_address)
                    }

                    SEARCHTYPE_PRINTERSN, SEARCHTYPE_PRINTERMODEL, SEARCHTYPE_BTVERSION, SEARCHTYPE_PRINTERVERSION, SEARCHTYPE_BTNAME -> try {
                        Log.e("jung", String(bytes, charset("GB2312")))
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }

                    else -> try {
                        Log.e("jung", String(bytes, charset("GB2312")))
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun connect(name: String, address: String?) {
        isReceiverEnabled = true

        if (printPP.isConnected) {
            printPP.disconnect()
        }

        printPP.connect(name, address)
    }

    fun disConnect() {
        isReceiverEnabled = true

        if (printPP.isConnected) {
            printPP.disconnect()
        }
    }

    fun sendBatteryCharge() {
        if (!printPP.isConnected) {
            return
        }

        handler.sendEmptyMessageDelayed(PrintPort.SEARCHTYPE_ISCHARGE, 500)
    }

    fun sendBatteryVol() {
        if (!printPP.isConnected) {
            return
        }
        handler.sendEmptyMessageDelayed(PrintPort.SEARCHTYPE_BATTERYVOL, 500)
    }

    fun sendPrinterStatus() {
        if (!printPP.isConnected) {
            return
        }
        handler.sendEmptyMessageDelayed(PrintPort.SEARCHTYPE_PRINTERSTATUS, 500)
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

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PrintPort.MSG_UPDATE_PROGRESS_BAR_PRINTER -> {
                    val progress = msg.obj as Int

                    Log.e("jung", "progress : $progress")
                }

                PrintPort.MSG_OTA_DATA_COMMAND_SEND_FAILED_PRINTER -> {
                    Log.e("jung", "Printer upgrade failed")
                }

                PrintPort.MSG_OTA_DATA_START_PRINTER -> {
                    Log.e("jung", "Start upgrading printer")
                }

                PrintPort.MSG_OTA_FINISHED_PRINTER -> {
                    Log.e("jung", "Printer Upgrade Complete")
                }

                PrintPort.SEARCHTYPE_PRINTERSTATUS -> printPP.printerStatus()
                PrintPort.SEARCHTYPE_BATTERYVOL -> printPP.printerBatteryVol()
                PrintPort.SEARCHTYPE_BTNAME -> printPP.printerBtname()
                PrintPort.SEARCHTYPE_MAC -> printPP.printerMac()
                PrintPort.SEARCHTYPE_BTVERSION -> printPP.printerBtVersion()
                PrintPort.SEARCHTYPE_PRINTERVERSION -> printPP.printerVersion()
                PrintPort.SEARCHTYPE_PRINTERSN -> printPP.printerSN()
                PrintPort.SEARCHTYPE_PRINTERMODEL -> printPP.printerModel()
                PrintPort.SEARCHTYPE_PRINTERINFOR -> printPP.printerInfor()
            }
            if (msg.what in 162..167) {
                sendEmptyMessageDelayed(++msg.what, 500)
            }
        }
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