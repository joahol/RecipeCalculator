package discretesolutions.com.recipecalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class FileDialogFragment extends DialogFragment {
    String filenames[];
    String selectedFile="";




    public interface FileDialogInterface{
        /*

        @description raises when a file is selected
        @param the active DialogFragment
         */
         public void onSelectedFileEvent(String selectedFile);
         /*

         @description raises when the selection of file is canceld
         @param the active DialogFragment
          */
         public void onCanceldEvent();
    }

public FileDialogInterface fileDialogInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){

        final ArrayList<String> fileNames = getArguments().getStringArrayList ("filenames");
        AlertDialog.Builder aDialog = new AlertDialog.Builder(getActivity());
        this.filenames = fileNames.toArray(new String[0]);
        aDialog.setTitle("Select recipe")
              .setItems(filenames, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    selectedFile = filenames[i];
                    Log.v("FileDialog:",selectedFile);

                    fileDialogInterface.onSelectedFileEvent(selectedFile);
                  }
              });
        return aDialog.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            fileDialogInterface = (FileDialogInterface) context;
        }catch(Exception e){}
        }




}
