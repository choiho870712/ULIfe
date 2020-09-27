package com.choiho.ulife.navigationUI.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var root: View
    private var lockRefreshProposalList = false
    private var lockCreateNewPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        GlobalVariables.toolBarController.setTextColor(R.color.colorWhite)
        setUi()

        createNewPage()
        linkMorePageListener()
        linkRefreshPageListener()
        createClassButton()

        return root
    }

    private fun createNewPage() {
        if (!lockCreateNewPage) {
            lockCreateNewPage = true

            Thread {
                while (!GlobalVariables.homeProposalListIsReady)
                    Thread.sleep(500)

                if (GlobalVariables.homeProposalList.isEmpty()) {
                    if (activity != null) requireActivity().runOnUiThread {
                        root.text_no_home_proposal.visibility = View.VISIBLE
                    }
                }
                else {
                    if (activity != null) requireActivity().runOnUiThread {
                        root.text_no_home_proposal.visibility = View.GONE
                    }

                    GlobalVariables.homeLayoutManager = GridLayoutManager(activity, 2)
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
                    lockCreateNewPage = false
                }
            }.start()
        }
    }

    private fun isScrollToLastProposal(): Boolean {
        val visibleItemCount = GlobalVariables.homeLayoutManager.childCount
        val totalItemCount = GlobalVariables.homeLayoutManager.itemCount
        val pastVisibleItems = GlobalVariables.homeLayoutManager.findFirstVisibleItemPosition()
        return (visibleItemCount + pastVisibleItems) >= totalItemCount - 20
    }

    private fun morePage() {
        var moreProposal:ArrayList<Proposal>? = null
        if (GlobalVariables.homeClassChoose == "") {
            moreProposal = GlobalVariables.api.getFoodAll(
                GlobalVariables.homeProposalNumber,
                GlobalVariables.homeAreaChoose
            )
        } // if
        else {
            moreProposal = GlobalVariables.api.getFoodByClass(
                GlobalVariables.homeProposalNumber,
                GlobalVariables.homeClassChoose,
                GlobalVariables.homeAreaChoose
            )
        } //

        GlobalVariables.homeProposalNumber += 10
        GlobalVariables.homeProposalList.addAll(moreProposal)
        if (moreProposal.isEmpty()) GlobalVariables.isHomeProposalEnd = true

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
                    if (!GlobalVariables.isHomeProposalEnd) {
                        GlobalVariables.lockRefreshHomePage = true
                        Log.d(">>>>>>>>>>>>>>>>>>>", "call more page")
                        Thread {
                            morePage()
                            GlobalVariables.lockRefreshHomePage = false
                        }.start()
                    }
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
        classList.add("全部")
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
        GlobalVariables.activity.nav_host_fragment
            .findNavController()
            .addOnDestinationChangedListener { _, _, _ ->
                GlobalVariables.toolBarController.openToolbarMainButton(false)
                GlobalVariables.toolBarController.openAddProposalButton(false)
                GlobalVariables.toolBarController.openEventRecordButton(false)

                GlobalVariables.activity.nav_host_fragment
                    .findNavController()
                    .addOnDestinationChangedListener { _, _, _ ->

                    }
            }

        GlobalVariables.toolBarController.openToolBar(true)
        GlobalVariables.toolBarController.openNavView(true)
        GlobalVariables.toolBarController.setMenuName()
        GlobalVariables.toolBarController.openToolbarBackButton(false)
        GlobalVariables.toolBarController.openToolbarMainButton(true)

        if (GlobalVariables.userInfo.isShop()) GlobalVariables.toolBarController.openAddProposalButton(true)
        if (GlobalVariables.userInfo.isShop()) GlobalVariables.toolBarController.openEventRecordButton(true)

    }


}
