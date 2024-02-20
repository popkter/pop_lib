package com.popkter.poplib.fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter4.BaseQuickAdapter
import com.popkter.poplib.databinding.FragmentBaseListBinding
import com.popkter.poplib.utils.getViewModel
import com.popkter.poplib.viewmodel.PopListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class PopListFragment<VM : PopListViewModel> : Fragment() {
    companion object {
        private const val TAG = "PopListFragment"
    }

    private var systemUiVisible: Boolean = false
    val binding: FragmentBaseListBinding by lazy { FragmentBaseListBinding.inflate(layoutInflater) }
    var isImmersiveMode: Boolean = false
    var isRefreshOnCreate: Boolean = true
    lateinit var viewModel: VM
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        binding.refreshLayout.apply {
            setOnLoadMoreListener { load { viewModel.loadMore() } }
            setOnRefreshListener { load { viewModel.onRefresh() } }
        }
        initOnViewCreated()
        if (isImmersiveMode) {
            hideSystemBar()
        } else {
            showSystemBar()
        }
        viewModel.dataSet.observe(this@PopListFragment.viewLifecycleOwner) {
            (binding.recyclerview.adapter as BaseQuickAdapter<*, *>).submitList(it)
            binding.refreshLayout.finishRefresh()
        }
        viewModel.loadMoreDataSet.observe(this@PopListFragment.viewLifecycleOwner) {
            (binding.recyclerview.adapter as BaseQuickAdapter<*, *>).addAll(it)
            binding.refreshLayout.finishLoadMore()
        }
        if (isRefreshOnCreate) {
            load { viewModel.onRefresh() }
        }
    }

    /**
     * 初始化View
     */
    abstract fun initOnViewCreated()

    /**
     * 切换状态栏显示隐藏
     */
    fun toggleStatusBar() {
        if (systemUiVisible) {
            isImmersiveMode = false
            hideSystemBar()
        } else {
            isImmersiveMode = true
            showSystemBar()
        }
    }

    /**
     * 添加不吸顶的Header
     */
    fun addUnpinHeader(header: View) {
        addHeader(header, null)
    }

    /**
     * 添加吸顶Header
     */
    fun addPinHeader(header: View) {
        addHeader(null, header)
    }


    /**
     * 可刷新
     */
    fun setOnRefreshEnable(status: Boolean) {
        binding.refreshLayout.setEnableRefresh(status)
    }

    /**
     * 可加载
     */
    fun setLoadMoreEnable(status: Boolean) {
        binding.refreshLayout.setEnableLoadMore(status)
    }

    private fun addHeader(largeHeader: View?, pinHeader: View?) {

        largeHeader?.run {
            binding.titleContainerLarge.addView(this)
            post {
                binding.titleContainerLarge.layoutParams.apply {
                    height = this.width
                    width = this.height
                }
            }
        }

        pinHeader?.run {
            binding.titleContainerPin.addView(this)
            post {
                binding.titleContainerPin.layoutParams.apply {
                    height = this.width
                    width = this.height
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateStatusBarTextColor(newConfig.uiMode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.titleContainerLarge.removeAllViews()
        binding.titleContainerPin.removeAllViews()
        Log.e(TAG, "onDestroyView: ")
    }

    private fun showSystemBar() {
        systemUiVisible = true
        updateStatusBarTextColor(resources.configuration.uiMode)

        activity?.window?.run {
            statusBarColor = Color.TRANSPARENT
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController?.show(WindowInsets.Type.statusBars())
                insetsController?.show(WindowInsets.Type.navigationBars())
            } else {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE)
            }
        }
    }

    private fun hideSystemBar() {
        systemUiVisible = false
        updateStatusBarTextColor(resources.configuration.uiMode)
        activity?.window?.run {
            statusBarColor = Color.TRANSPARENT
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController?.hide(WindowInsets.Type.statusBars())
                insetsController?.hide(WindowInsets.Type.navigationBars())
            } else {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        }
    }

    /**
     * 更新状态栏字体颜色
     */
    private fun updateStatusBarTextColor(uiMode: Int) {
        val isNightMode =
            (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        activity?.window?.decorView?.run {
            if (isNightMode) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    systemUiVisibility =
                        (systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
                } else {
                    activity?.window?.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            } else {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    systemUiVisibility =
                        (systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    activity?.window?.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            }
        }
    }

}

fun PopListFragment<*>.load(block: suspend () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        block.invoke()
    }
}