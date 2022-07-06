package it.digirolamopescetti.towatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;


public class RemoveDialog extends AppCompatDialogFragment {

    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention!").
                setMessage("Do you want to delete this movie?").
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.confirmRemove();
                    }
                });
        return builder.create();
    }

    public interface DialogListener{
        void confirmRemove();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            listener = (DialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context + "must implement DialogListener");
        }
    }
}
