package com.excilys.shoofleurs.dashboard.controllers;

import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.comparators.DiaporamaComparator;
import com.excilys.shoofleurs.dashboard.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.model.entities.VideoContent;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This controller controls the diaporamas's display
 */
public class DiaporamaController {
    private static DiaporamaController S_INSTANCE;

    private DashboardActivity mDashboardActivity;
    /**
     * The layout encompassing the contents
     */
    private RelativeLayout mContentLayout;
    /**
     * The sorted diaporama queue
     */
    private Queue<Diaporama> mDiaporamaQueue;

    /**
     * The current diaporama to be displayed
     */
    private Diaporama mCurrentDiaporama;

    /**
     * To create interval beetween content
     */
    private Handler mHandler = new Handler();


    private int mCurrentContentIndex;

    public static DiaporamaController getInstance(DashboardActivity dashboardActivity) {
        if (S_INSTANCE == null) S_INSTANCE = new DiaporamaController(dashboardActivity);
        return S_INSTANCE;
    }

    private DiaporamaController(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mContentLayout = (RelativeLayout) mDashboardActivity.findViewById(R.id.current_content_layout);
        mDiaporamaQueue = new ArrayDeque<>();
    }


    public void addAllDiaporamas(Diaporama... diaporamas) {
        Log.i(getClass().getSimpleName(), "addAllDiaporamas: " + Arrays.asList(diaporamas));
        for (Diaporama d : diaporamas) {
            /**TODO donnés video de test*/
            d.getContents().add(new VideoContent("Video", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
            mDiaporamaQueue.offer(d);
        }


        if (mCurrentDiaporama != null) {
            if (!mDiaporamaQueue.peek().equals(mCurrentDiaporama)) {
                replaceDiaporama(mDiaporamaQueue.poll());
            }
        } else {
            replaceDiaporama(mDiaporamaQueue.poll());
        }
    }


    /**
     * Replace or create the current diaporama
     *
     * @param newDiaporama
     */
    public void replaceDiaporama(Diaporama newDiaporama) {
        this.mCurrentDiaporama = newDiaporama;
        Log.i(DashboardActivity.class.getSimpleName(), "replaceDiaporama: " + mCurrentDiaporama);
        startDiaporama();
    }


    public void stopDiaporama() {
    }

    public void pauseDiaporama() {
    }

    /**
     * Start the diaporama to display
     */
    public void startDiaporama() {
        Log.i(getClass().getSimpleName(), "startDiaporama " + mCurrentDiaporama);
        mCurrentContentIndex = 0;

        mDashboardActivity.stopProgressView();
        if (mCurrentDiaporama != null) {
            if (mCurrentDiaporama.getContents().size() > 0){
                AbstractContent firstContent = mCurrentDiaporama.getContents().get(mCurrentContentIndex);
                displayContent(firstContent);
            }
            
            else {
                Log.i(DiaporamaController.class.getSimpleName(), "startDiaporama: The contents are empty!!");
            }
        }
    }

    /**
     * Display the content and go to the next when it's completed.
     * @param content
     */
    private void displayContent(AbstractContent content) {
        Log.i(DiaporamaController.class.getSimpleName(), "displayContent: " + content);
        Displayable displayable = DisplayableFactory.create(content, new AbstractDisplayable.OnCompletionListener() {
            @Override
            public void onCompletion() {
                if (mCurrentDiaporama.getContents().size() != 1) {
                    nextContent();
                }
                else {
                    Log.i(DiaporamaController.class.getSimpleName(), "displayContent: The content is alone");
                }
            }
        });
        displayable.displayContent(mDashboardActivity, mContentLayout);
    }

    /**
     * Display the next content of the diaporama or
     * return to first if it's the last
     */
    public void nextContent() {
        ++mCurrentContentIndex;
        final AbstractContent preparedContent = mCurrentContentIndex < mCurrentDiaporama.getContents().size() ?
                mCurrentDiaporama.getContents().get(mCurrentContentIndex) :
                mCurrentDiaporama.getContents().get(mCurrentContentIndex = 0);
        Log.i(DiaporamaController.class.getSimpleName(), "nextContent: " + preparedContent);

        displayContent(preparedContent);
    }
}
