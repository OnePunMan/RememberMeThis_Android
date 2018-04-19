package onepunman.remembermethis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UIManager {
    public static void createConfirmationPopup(
            Context context, String msgTitle, String msgBody, int icon,
            String positiveButtonText, String negativeButtonText, String neutralButtonText,
            final Runnable onPositiveClicked, final Runnable onNegativeClicked, final Runnable onNeutralClicked)
    {
        AlertDialog.Builder msg = new AlertDialog.Builder(context);
        msg.setTitle(msgTitle).setMessage(msgBody);
        msg.setIcon(icon);

        if (positiveButtonText != null) {
            msg.setPositiveButton(positiveButtonText, (dialog, which) -> {
                if (onPositiveClicked != null) {
                    onPositiveClicked.run();
                }
            });
        }

        if (negativeButtonText != null) {
            msg.setNegativeButton(negativeButtonText, (dialog, which) -> {
                if (onNegativeClicked != null) {
                    onNegativeClicked.run();
                }
                dialog.dismiss();
            });
        }

        if (neutralButtonText != null) {
            msg.setNeutralButton(neutralButtonText, (dialog, which) -> {
                if (onNeutralClicked != null) {
                    onNeutralClicked.run();
                }
            });
        }
        msg.create().show();
    }
}
