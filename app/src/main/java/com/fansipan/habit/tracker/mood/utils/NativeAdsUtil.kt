package com.fansipan.habit.tracker.mood.utils

object NativeAdsUtil {
//    fun loadNativeAds(context: Context, loadSuccess: (nativeAd: NativeAd?) -> Unit, loadFailed: () -> Unit) {
//        var nativeAd: NativeAd? = null
//        val adLoader: AdLoader?
//        val id = if (BuildConfig.DEBUG) {
//            Constant.NATIVE_AD_TEST
//        } else {
//            Constant.NATIVE_AD_LOAD1
//        }
//        val builder = AdLoader.Builder(context, id)
//        adLoader = builder.forNativeAd { nativeAds ->
//            nativeAd = nativeAds
//        }
//            .withAdListener(object : AdListener() {
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    Log.d("dddd", "loadAds1 success")
//                    loadSuccess.invoke(nativeAd)
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    super.onAdFailedToLoad(loadAdError)
//                    Log.e("dddd", "load native ad1 failed")
//                    reloadNativeAdWithId2(context, loadSuccess, loadFailed)
//                }
//            }).build()
//        adLoader.loadAd(AdRequest.Builder().build())
//    }
//
//    fun reloadNativeAdWithId2(context: Context, loadSuccess: (nativeAd: NativeAd?) -> Unit, loadFailed: () -> Unit) {
//        var nativeAd: NativeAd? = null
//        var adLoader: AdLoader?
//        Log.d("dddd", "loadAds2")
//        val id = if (BuildConfig.DEBUG) {
//            Constant.NATIVE_AD_TEST
//        } else {
//            Constant.NATIVE_AD_LOAD2
//        }
//        val builder = AdLoader.Builder(context, id)
//        adLoader = builder.forNativeAd { nativeAds: NativeAd? ->
//            nativeAd = nativeAds
//        }
//            .withAdListener(object : AdListener() {
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    loadSuccess.invoke(nativeAd)
//                    Log.d("dddd", "loadAds2 success")
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    Log.e("dddd", "load native ad2 failed")
//                    super.onAdFailedToLoad(loadAdError)
//                    loadFailed.invoke()
//                    adLoader = null
//                }
//            }).build()
//        adLoader?.loadAd(AdRequest.Builder().build())
//    }
//
//    fun populateNativeAdView(nativeAd: NativeAd?, adView: NativeAdView, binding: LayoutNativeAds2Binding?) {
//        if (nativeAd != null) {
//            adView.mediaView = binding?.adMedia
//            adView.headlineView = binding?.adHeadline
//            adView.bodyView = binding?.adBody
//            adView.callToActionView = binding?.adCallToAction
//            adView.iconView = binding?.adAppIcon
////                adView.priceView = binding?.adPrice
//            adView.starRatingView = binding?.adStars
//            adView.storeView = binding?.adStore
//            adView.advertiserView = binding?.adAdvertiser
//            (adView.headlineView as TextView?)?.text = nativeAd.headline
//            if (nativeAd.body == null) {
//                adView.bodyView?.visibility = View.INVISIBLE
//            } else {
//                adView.bodyView?.visibility = View.VISIBLE
//                (adView.bodyView as TextView?)?.text = nativeAd.body
//            }
//            if (nativeAd.callToAction == null) {
//                adView.callToActionView?.visibility = View.INVISIBLE
//            } else {
//                adView.callToActionView?.visibility = View.VISIBLE
//                (adView.callToActionView as Button?)?.text = nativeAd.callToAction
//            }
//            if (nativeAd.icon == null) {
//                adView.iconView?.visibility = View.GONE
//            } else {
//                (adView.iconView as ImageView?)?.setImageDrawable(
//                    nativeAd.icon?.drawable
//                )
//                adView.iconView?.visibility = View.VISIBLE
//            }
//            if (nativeAd.price == null) {
//                adView.priceView?.visibility = View.INVISIBLE
//            } else {
//                adView.priceView?.visibility = View.VISIBLE
//                (adView.priceView as TextView?)?.text = nativeAd.price
//            }
//            if (nativeAd.store == null) {
//                adView.storeView?.visibility = View.INVISIBLE
//            } else {
//                adView.storeView?.visibility = View.VISIBLE
//                (adView.storeView as TextView?)?.text = nativeAd.store
//            }
//            if (nativeAd.starRating == null) {
//                adView.starRatingView?.visibility = View.GONE
//                if (nativeAd.advertiser == null) {
//                    adView.advertiserView?.visibility = View.INVISIBLE
//                } else {
//                    (adView.advertiserView as TextView?)?.text = nativeAd.advertiser
//                    adView.advertiserView?.visibility = View.VISIBLE
//                }
//            } else {
//                (adView.starRatingView as RatingBar?)?.rating = nativeAd.starRating?.toFloat() ?: 0f
//                adView.starRatingView?.visibility = View.VISIBLE
//            }
//            adView.setNativeAd(nativeAd)
//        }
//    }
}