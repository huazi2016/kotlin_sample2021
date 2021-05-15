package com.test.huazi.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.PolylineOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.RouteSearch.FromAndTo
import com.amap.api.services.route.RouteSearch.RideRouteQuery
import com.test.base.impl.CommonTitleClick
import com.test.base.utils.AppManager
import com.test.base.utils.showToast
import com.test.base.view.BaseActivity
import com.test.huazi.R
import com.test.huazi.databinding.ActivityMapBinding


class MapTestActivity : BaseActivity<ActivityMapBinding>() {

    private val mStartPoint = LatLonPoint(39.942295, 116.335891) //起点
    private val mEndPoint = LatLonPoint(39.995576, 116.481288) //终点


    companion object {
        public fun launchActivity(context: Context? = AppManager.getContext()) {
            val intent = Intent(context, MapTestActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getViewBinding() = ActivityMapBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
    }

    override fun initView() {
        binding.emptyTitleLayout.setTitle("地图")
        //val loadView = LoadViewHelper(binding.testRlLayout)
        //loadView.showPbLoading(R.string.loading)
        binding.emptyTitleLayout.setOnTitleClick(object : CommonTitleClick {
            override fun onLeftClick() {
                finish()
            }

            override fun onRightClick() {
                //loadView.restoreView()
                startMap()
                searchRouteResult(4, RouteSearch.RidingDefault)
            }
        })
        val map = binding.mapView.map
        map.isTrafficEnabled = true
        //map.mapType = AMap.MAP_TYPE_SATELLITE
    }

    private fun startMap() {
        val map = binding.mapView.map
        map.addMarker(
            MarkerOptions()
                .position(convertToLatLng(mStartPoint))
                .title("背景")
                .snippet("我要触发了")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_fire))
        )//.isDraggable = true
        map.addMarker(
            MarkerOptions()
                .position(convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_release_community))
        )
        //曲线
        map.addPolyline(
            PolylineOptions()
                .add(LatLng(39.942295, 116.335891), LatLng(39.995576, 116.481288))
                .setDottedLine(true)
                .geodesic(true).color(Color.RED)
        )
    }

    /**
     * 开始搜索路径规划方案
     */
    fun searchRouteResult(routeType: Int, mode: Int) {
        val mRouteSearch = RouteSearch(this)
        if (mStartPoint == null) {
            showToast("定位中，稍后再试...")
            return
        }
        if (mEndPoint == null) {
            showToast("终点未设置")
        }
        //showProgressDialog()
        val fromAndTo = FromAndTo(
            mStartPoint, mEndPoint
        )
        if (routeType == 4) { // 骑行路径规划
            val query = RideRouteQuery(fromAndTo, mode)
            mRouteSearch.calculateRideRouteAsyn(query) // 异步路径规划骑行模式查询
        }
    }

    private fun convertToLatLng(latLonPoint: LatLonPoint): LatLng {
        return LatLng(latLonPoint.latitude, latLonPoint.longitude)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}