package expo.modules.medialibrary.contracts

import android.app.Activity
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult.Companion
import androidx.annotation.RequiresApi
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.activityresult.AppContextActivityResultContract
import expo.modules.kotlin.providers.AppContextProvider
import java.io.Serializable


class WriteContract(
  private val appContextProvider: AppContextProvider
): AppContextActivityResultContract<WriteContractInput, Boolean> {
  companion object {
    /**
     * An [Intent] action for making a request via the [Activity.startIntentSenderForResult]
     * API.
     */
    const val ACTION_INTENT_SENDER_REQUEST =
      "androidx.activity.result.contract.action.INTENT_SENDER_REQUEST"

    /**
     * Key for the extra containing the [IntentSenderRequest].
     *
     * @see ACTION_INTENT_SENDER_REQUEST
     */
    const val EXTRA_INTENT_SENDER_REQUEST =
      "androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST"

    /**
     * Key for the extra containing the [android.content.IntentSender.SendIntentException]
     * if the call to [Activity.startIntentSenderForResult] fails.
     */
    const val EXTRA_SEND_INTENT_EXCEPTION =
      "androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION"
  }
  private val contentResolver: ContentResolver
    get() = requireNotNull(appContextProvider.appContext.reactContext) {
      "React Application Context is null"
    }.contentResolver

  @RequiresApi(Build.VERSION_CODES.R)
  override fun createIntent(context: Context, input: WriteContractInput): Intent {
    val request = MediaStore.createWriteRequest(contentResolver, input.uris)
    val intentSenderRequest = IntentSenderRequest.Builder(request.intentSender).build()
    return Intent(ACTION_INTENT_SENDER_REQUEST).putExtra(EXTRA_INTENT_SENDER_REQUEST, intentSenderRequest)
  }

  override fun parseResult(input: WriteContractInput, resultCode: Int, intent: Intent?): Boolean {
    return resultCode == Activity.RESULT_OK
  }
}

data class WriteContractInput(val uris: List<Uri>): Serializable
