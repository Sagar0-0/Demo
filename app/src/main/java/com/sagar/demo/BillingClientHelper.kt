package com.sagar.demo

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BillingClientHelper(
    private val context: Context,
    purchasesUpdatedListener: PurchasesUpdatedListener,
    billingClientListener: BillingClientListener? = null
) {
    private var billingClient: BillingClient? = null
    private var _purchasesUpdatedListener: PurchasesUpdatedListener? = null
    private var _billingClientListener: BillingClientListener? = null

    init {
        _purchasesUpdatedListener = purchasesUpdatedListener
        _billingClientListener = billingClientListener
        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                // Configure other settings.
                .build()
        }
    }

    fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _billingClientListener?.onBillingSetupFinished(billingResult)
                }
            }

            override fun onBillingServiceDisconnected() {
                _billingClientListener?.onBillingServiceDisconnected()
            }
        })
    }

    fun endConnection() {
        if (billingClient?.connectionState == ConnectionState.CONNECTED) {
            billingClient?.endConnection()
        }
    }

    suspend fun processPurchase(productId: String = "product_id", type: AppProductType = AppProductType.inapp) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder().setProductId(productId)
                .setProductType(type.name).build()
        )
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        // leverage queryProductDetails Kotlin extension function
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient?.queryProductDetails(params.build())
        }

        // Process the result.
        val productDetailsParamsList =
            listOf(productDetailsResult?.productDetailsList?.get(0)?.let {
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                    .setProductDetails(it)
                    // For One-time product, "setOfferToken" method shouldn't be called.
                    // For subscriptions, to get an offer token, call ProductDetails.subscriptionOfferDetails()
                    // for a list of offers that are available to the user
//                    .setOfferToken(it.subscriptionOfferDetails?.get(0)?.offerToken)
                    .build()
            })

        val billingFlowParams =
            BillingFlowParams
                .newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

        // Launch the billing flow
        val billingResult = withContext(Dispatchers.Main) {
            billingClient?.launchBillingFlow((context as Activity), billingFlowParams)
        }
        Log.e("TAG", "processPurchases: $billingResult")
    }

    enum class AppProductType {
        inapp, subs
    }

    interface BillingClientListener {
        fun onBillingSetupFinished(billingResult: BillingResult) { }
        fun onBillingServiceDisconnected() { }
    }

}