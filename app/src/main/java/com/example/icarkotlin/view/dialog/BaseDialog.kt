package com.example.icarkotlin.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icarkotlin.App
import com.example.icarkotlin.R
import com.example.icarkotlin.Storage
import com.example.icarkotlin.view.callback.OnActionCallBack
import com.example.icarkotlin.view.viewmodel.BaseViewModel


abstract class BaseDialog<T : BaseViewModel, M> : Dialog, View.OnClickListener, OnActionCallBack {
    protected var isAnimEnd = true
    protected var mView: View? = null
    protected var mAnim: Animation? = null
    protected var mModel: T? = null
    var mData: M? = null
    protected var mContext: Context? = null
    var mCallBack: OnActionCallBack? = null

    constructor(
        context: Context,
        data: M?,
        owner: ViewModelStoreOwner? = null,
        clazz: Class<T>?
    ) : super(context) {
        mContext = context
        initCommon(data, owner, clazz)
    }

    constructor(
        context: Context,
        data: M?,
        theme: Int,
        owner: ViewModelStoreOwner? = null,
        clazz: Class<T>?
    ) : super(context, theme) {
        mContext = context
        initCommon(data, owner, clazz)
    }

    constructor(context: Context, data: M?, theme: Int) : this(context, data, theme, null, null)

    constructor(context: Context) : this(context, R.style.Theme_GPRSCarLocation) {}

    constructor(context: Context, data: M) : this(context, data, null, null)

    constructor(context: Context, theme: Int) : this(context, null, theme) {}

    fun <K : View?> findViewById(id: Int, event: View.OnClickListener?): K? {
        val v: K = findViewById(id)
        if (v != null && event != null) {
            v.setOnClickListener(event)
        }
        return v
    }

    protected val storage: Storage
        protected get() = App.getInstance().getStorage()

    private fun initCommon(data: M?, owner: ViewModelStoreOwner?, clazz: Class<T>?) {
        mData = data
        setContentView(getLayoutId())
        mAnim = AnimationUtils.loadAnimation(App.getInstance(), R.anim.anim_state)
        mAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                onClickView(mView)
                isAnimEnd = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        if (owner != null && clazz != null) {
            mModel = ViewModelProvider(owner).get(clazz)
        }
        setCanceledOnTouchOutside(false)
        initViews()
    }

    protected open fun onClickView(mView: View?) {
        //do nothing
    }

    protected abstract fun initViews()
    protected abstract fun getLayoutId(): Int
    override fun onClick(view: View) {
        if (!isAnimEnd) return
        isAnimEnd = false
        mView = view
        view.startAnimation(mAnim)
    }

    override fun showWarnNoInternet() {
        Toast.makeText(mContext, "Mạng không khả dụng, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT)
            .show()
    }
}