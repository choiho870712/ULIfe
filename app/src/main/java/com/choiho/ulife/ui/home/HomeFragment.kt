package com.choiho.ulife.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choiho.ulife.GlobalVariables
import com.choiho.ulife.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        createNewPage(root)
        linkMorePageListener(root)
        linkRefreshPageListener(root)
        createClassButton(root)

        return root
    }

    override fun onResume() {
        super.onResume()
        setUi()
    }

    override fun onStop() {
        super.onStop()
        if (activity != null)
            requireActivity().toolbar.button_toolbar_main.visibility = View.GONE

        GlobalVariables.uiController.openAddProposalButton(false)
        GlobalVariables.uiController.openEventRecordButton(false)
    }

    private fun createNewPage(root:View) {
        if (!GlobalVariables.isRefreshingHomePage) {
            GlobalVariables.isRefreshingHomePage = true
            root.swip_recycler_home.isRefreshing = true
            GlobalVariables.homeLayoutManager = GridLayoutManager(activity, 2)

            Thread {
                while (true) {
                    if (GlobalVariables.homePageProposalCount > 0)
                        break
                }

                GlobalVariables.homeAdapter = CardAdapter(GlobalVariables.homeProposalList)
                if (activity != null) {
                    requireActivity().runOnUiThread(Runnable {
                        root.recycler_home.apply {
                            setHasFixedSize(true)
                            layoutManager = GlobalVariables.homeLayoutManager
                            adapter = GlobalVariables.homeAdapter
                        }
                        root.swip_recycler_home.isRefreshing = false
                    })
                    GlobalVariables.isRefreshingHomePage = false
                }
                else {
                    root.swip_recycler_home.isRefreshing = false
                    GlobalVariables.isRefreshingHomePage = false
                }
            }.start()
        }
    }

    private fun linkMorePageListener(root:View) {
        root.recycler_home.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0)
                {
                    val visibleItemCount = GlobalVariables.homeLayoutManager.childCount
                    val totalItemCount = GlobalVariables.homeLayoutManager.itemCount
                    val pastVisibleItems = GlobalVariables.homeLayoutManager.findFirstVisibleItemPosition()

                    if (!GlobalVariables.isRefreshingHomePage) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            GlobalVariables.isRefreshingHomePage = true
                            root.swip_recycler_home.isRefreshing = true
                            Thread {
                                GlobalVariables.homeProposalList.addAll(
                                    GlobalVariables.api.getFoodAll(
                                        GlobalVariables.homePageProposalCount + 1,
                                        GlobalVariables.homeAreaChoose))

                                GlobalVariables.homePageProposalCount += 10
                                if (activity != null) {
                                    activity!!.runOnUiThread(Runnable {
                                        GlobalVariables.homeAdapter.notifyDataSetChanged()
                                        root.swip_recycler_home.isRefreshing = false
                                    })
                                    GlobalVariables.isRefreshingHomePage = false
                                }
                                else {
                                    root.swip_recycler_home.isRefreshing = false
                                    GlobalVariables.isRefreshingHomePage = false
                                }
                            }.start()
                        }
                    }
                }
            }
        })
    }

    private fun linkRefreshPageListener(root:View) {
        root.swip_recycler_home.setOnRefreshListener {
            if (!GlobalVariables.isRefreshingHomePage) {
                GlobalVariables.isRefreshingHomePage = true

                Thread {
                    GlobalVariables.homeProposalList = GlobalVariables.api.getFoodAll(1, GlobalVariables.homeAreaChoose)
                    GlobalVariables.homePageProposalCount = 10
                    GlobalVariables.homeAdapter = CardAdapter(GlobalVariables.homeProposalList)
                    GlobalVariables.homeLayoutManager = GridLayoutManager(GlobalVariables.activity, 2)

                    if (activity != null) {
                        requireActivity().runOnUiThread(Runnable {
                            root.recycler_home.apply {
                                setHasFixedSize(true)
                                layoutManager = GlobalVariables.homeLayoutManager
                                adapter = GlobalVariables.homeAdapter
                            }
                            root.swip_recycler_home.isRefreshing = false
                        })

                        GlobalVariables.isRefreshingHomePage = false
                    }
                    else {
                        root.swip_recycler_home.isRefreshing = false
                        GlobalVariables.isRefreshingHomePage = false
                    }
                }.start()
            }
        }
    }

    private fun createClassButton(root:View) {
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

        if (activity != null) {
            root.recycler_class_button.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                adapter = CardClassButtonAdapter(classList, root)
            }
        }
    }

    private fun setUi() {
        GlobalVariables.uiController.openToolBar(true)
        GlobalVariables.uiController.openNavView(true)
        GlobalVariables.uiController.setNavViewButton()
        GlobalVariables.uiController.setMenuName()
        GlobalVariables.uiController.openToolbarBackButton(false)
        GlobalVariables.uiController.openToolbarMainButton(true)

        if (GlobalVariables.userInfo.isShop()) GlobalVariables.uiController.openAddProposalButton(true)
        if (GlobalVariables.userInfo.isShop()) GlobalVariables.uiController.openEventRecordButton(true)
    }


}
