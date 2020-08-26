package com.choiho.ulife.navigationUI.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var root: View
    private var lockRefreshProposalList = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        if (GlobalVariables.homeProposalList.isEmpty())
            GlobalVariables.functions.resetProposalList()
        createNewPage()
        linkMorePageListener()
        linkRefreshPageListener()
        createClassButton()
        setUi()
        return root
    }

    override fun onStop() {
        super.onStop()
        GlobalVariables.toolBarController.openToolbarMainButton(false)
        GlobalVariables.toolBarController.openAddProposalButton(false)
        GlobalVariables.toolBarController.openEventRecordButton(false)
    }

    private fun createNewPage() {
        if (!GlobalVariables.lockRefreshHomePage) {
            GlobalVariables.lockRefreshHomePage = true
            if (activity != null)root.swip_recycler_home.isRefreshing = true
            GlobalVariables.homeLayoutManager = GridLayoutManager(activity, 2)

            Thread {
                while (GlobalVariables.homeProposalList.isEmpty()) continue

                GlobalVariables.homeAdapter = CardAdapter(GlobalVariables.homeProposalList)
                if (activity != null) requireActivity().runOnUiThread {
                    root.recycler_home.apply {
                        setHasFixedSize(true)
                        layoutManager = GlobalVariables.homeLayoutManager
                        adapter = GlobalVariables.homeAdapter
                    }
                    GlobalVariables.homeLayoutManager.scrollToPosition(
                        GlobalVariables.homeCurrentPosition)
                }

                if (activity != null) requireActivity().runOnUiThread {
                    root.swip_recycler_home.isRefreshing = false
                }

                GlobalVariables.lockRefreshHomePage = false
            }.start()
        }
    }

    private fun isScrollToLastProposal(): Boolean {
        val visibleItemCount = GlobalVariables.homeLayoutManager.childCount
        val totalItemCount = GlobalVariables.homeLayoutManager.itemCount
        val pastVisibleItems = GlobalVariables.homeLayoutManager.findFirstVisibleItemPosition()
        return (visibleItemCount + pastVisibleItems) >= totalItemCount
    }

    private fun morePage() {
        val moreProposal = GlobalVariables.api.getFoodAll(
            GlobalVariables.homeProposalList.size + 2,
            GlobalVariables.homeAreaChoose
        )
        GlobalVariables.homeProposalList.addAll(moreProposal)

        if (moreProposal.isEmpty()) return
        if (activity != null) requireActivity().runOnUiThread {
            GlobalVariables.homeAdapter.notifyDataSetChanged()
        }
    }

    private fun linkMorePageListener() {
        root.recycler_home.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !GlobalVariables.lockRefreshHomePage && isScrollToLastProposal()) {
                    GlobalVariables.lockRefreshHomePage = true
                    if (activity != null)root.swip_recycler_home.isRefreshing = true
                    Thread {
                        morePage()
                        if (activity != null) requireActivity().runOnUiThread {
                            root.swip_recycler_home.isRefreshing = false
                        }

                        GlobalVariables.lockRefreshHomePage = false
                    }.start()
                }
            }
        })
    }

    private fun linkRefreshPageListener() {
        lockRefreshProposalList = false
        root.swip_recycler_home.setOnRefreshListener {
            if (!lockRefreshProposalList) {
                lockRefreshProposalList = true

                Thread {
                    GlobalVariables.functions.resetProposalList()
                    createNewPage()
                    lockRefreshProposalList = false
                }.start()
            }
        }
    }

    private fun createClassButton() {
        val classList:ArrayList<String> = ArrayList()
        classList.add("早午餐")
        classList.add("飲品")
        classList.add("台式")
        classList.add("鍋類")
        classList.add("中式")
        classList.add("日式")
        classList.add("韓式")
        classList.add("美式")
        classList.add("歐式")
        classList.add("泰式")
        classList.add("咖啡")
        classList.add("甜點")
        classList.add("麵包糕點")
        classList.add("酒吧")

        if (activity != null)
            root.recycler_class_button.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                adapter = CardClassButtonAdapter(classList, root)
            }
    }

    private fun setUi() {
        GlobalVariables.toolBarController.openToolBar(true)
        GlobalVariables.toolBarController.openNavView(true)
        GlobalVariables.toolBarController.setMenuName()
        GlobalVariables.toolBarController.openToolbarBackButton(false)
        GlobalVariables.toolBarController.openToolbarMainButton(true)

        if (GlobalVariables.userInfo.isShop()) GlobalVariables.toolBarController.openAddProposalButton(true)
        if (GlobalVariables.userInfo.isShop()) GlobalVariables.toolBarController.openEventRecordButton(true)
    }


}
