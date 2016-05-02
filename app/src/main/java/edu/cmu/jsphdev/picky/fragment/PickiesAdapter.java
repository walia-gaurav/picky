package edu.cmu.jsphdev.picky.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.ImageDownloaderTask;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderButtonCallback;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.PickyService;

class PickiesAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private Activity callerActivity;
    private List<Picky> pickies;
    private PickyServiceInterface pickyService;


    /**
     * Constructing the adapter fields.
     *
     * @param activity
     * @param pickies
     */
    public PickiesAdapter(Activity activity, List<Picky> pickies) {
        this.pickies = pickies;
        this.callerActivity = activity;
        pickyService = new PickyService();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public final PickiesAdapter getAdapter() {
        return this;
    }

    public List<Picky> getPickies() {
        return pickies;
    }

    @Override
    public int getCount() {
        return pickies.size();
    }

    @Override
    public Object getItem(int location) {
        return pickies.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_profile, null, false);
        }

        /*
        Referencing view tags.
         */
        Button leftButton = (Button) convertView.findViewById(R.id.leftChoiceButton);
        Button rightButton = (Button) convertView.findViewById(R.id.rightChoiceButton);
        TextView title = (TextView) convertView.findViewById(R.id.profilePickyTitle);
        TextView leftVotes = (TextView) convertView.findViewById(R.id.leftVotes);
        TextView rightVotes = (TextView) convertView.findViewById(R.id.rightVotes);
        Button deleteButton = (Button) convertView.findViewById(R.id.detelePickyButton);

        final Picky picky = (Picky) getItem(position);
        title.setText(picky.getTitle());

        /*
        Calculating voting percentages.
         */
        float total = picky.getLeftVotes() + picky.getRightVotes();
        if (total == 0) {
            leftVotes.setText(String.format(Locale.US, "0%% (Votes: %d)", picky.getLeftVotes()));
            rightVotes.setText(String.format(Locale.US, "0%% (Votes: %d)", picky.getRightVotes()));
        } else {
            leftVotes.setText(String.format(Locale.US, "%.2f %% (Votes: %d)", ((double) picky.getLeftVotes() *
                    100.0) /
                    total, picky.getLeftVotes()));
            rightVotes.setText(String.format(Locale.US, "%.2f%% (Votes: %d)", ((double) picky.getRightVotes() *
                    100.0) /
                    total, picky.getRightVotes()));
        }

        /*
        Downloading picky images.
         */
        ImageDownloaderButtonCallback buttonCallback = new ImageDownloaderButtonCallback(callerActivity.getResources(),
                leftButton, rightButton);
        ImageDownloaderTask<Button> imageDownloaderTask = new ImageDownloaderTask<>(buttonCallback);
        imageDownloaderTask.execute(picky.getLeftPhoto().getUrl(), picky.getRightPhoto().getUrl());

        /*
        Deletes a picky!
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback<Boolean> callback = new Callback<Boolean>() {
                    @Override
                    public void process(Boolean isSuccess) {
                        if (isSuccess) {
                            Toast.makeText(callerActivity.getApplicationContext(), "Picky deleted successfully!",
                                    Toast.LENGTH_LONG)
                                    .show();
                            pickies.remove(position);
                            getAdapter().notifyDataSetChanged();
                        } else {
                            Toast.makeText(callerActivity.getApplicationContext(), "Could not delete picky!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                        Toast.makeText(callerActivity.getApplicationContext(), (isSuccess ? "Picky deleted " +
                                "successfully!" : "Could not delete picky!"), Toast.LENGTH_LONG)
                                .show();
                    }
                };
                pickyService.delete(String.valueOf(picky.getId()), callback);
            }
        });

        return convertView;
    }
}