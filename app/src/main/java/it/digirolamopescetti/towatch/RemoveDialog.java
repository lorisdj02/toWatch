package it.digirolamopescetti.towatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

//Do you want to delete this movie? Yes/Not
public class RemoveDialog extends AppCompatDialogFragment {

    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.attention)).
                setMessage(getString(R.string.confirmRem)).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.confirmRemove();
                    }
                });
        return builder.create();
    }

    public interface DialogListener{
        //this is important to implement function in MainActivity
        void confirmRemove();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        try{
            listener = (DialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context + "must implement DialogListener");
        }
    }
}
