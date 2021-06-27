package com.example.icarkotlin.view

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.R


class ProgressLoading private constructor() {
    companion object {
        private var isHide: Boolean = false
        private var pdLoading: Dialog? = null
        fun dontShow() {
            isHide = true
        }

        fun show(context: Context, isShowCancel: Boolean) {
            if (!CommonUtils.getInstance().isInternetAvailable()) {
                Toast.makeText(
                    context,
                    "Mạng dữ liệu không khả dụng, thử lại sau!",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            try {
                var viewCancel: View? = null
                if (pdLoading == null) {
                    pdLoading = Dialog(context)
                    pdLoading!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    val view: View =
                        LayoutInflater.from(context).inflate(R.layout.progress_loading, null, false)
                    viewCancel = view.findViewById(R.id.tv_cancel)
                    viewCancel?.setOnClickListener {
                        dismiss()
                    }
                    pdLoading!!.setContentView(view)
                    pdLoading!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    pdLoading!!.setCanceledOnTouchOutside(false)
                    pdLoading!!.window?.setGravity(Gravity.CENTER)
                    pdLoading!!.setCancelable(false)
                }
                viewCancel?.visibility = if (isShowCancel) View.VISIBLE else View.GONE
                pdLoading!!.show()
            } catch (ignored: Exception) {

            }
            isHide = false
        }

        fun dismiss() {
            if (pdLoading != null && pdLoading!!.isShowing) {
                Handler(Looper.getMainLooper())
                    .postDelayed(
                        {
                            try {
                                if (pdLoading != null && pdLoading!!.isShowing) {
                                    pdLoading!!.dismiss()
                                    pdLoading = null
                                }
                            } catch (ignored: Exception) {

                            }
                        },
                        800
                    )
            }
        }

        fun isLoading(): Boolean {
            return pdLoading != null && pdLoading!!.isShowing
        }
    }
}