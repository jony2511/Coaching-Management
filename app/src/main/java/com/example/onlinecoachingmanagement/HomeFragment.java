package com.example.onlinecoachingmanagement;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    private CardView exam, routine, qna,material;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        exam = view.findViewById(R.id.exmas);
        routine = view.findViewById(R.id.routine);
        qna = view.findViewById(R.id.qna);
        material=view.findViewById(R.id.materials);

        // Trigger entrance animations
        animateEntrance(exam, 0);
        animateEntrance(material, 200);
        animateEntrance(routine, 400);
        animateEntrance(qna, 600);

        // Add click animations and navigation
        addClickAnimationAndNavigation(exam, UserActivity.class);
        addClickAnimationAndNavigation(routine, RoutineActivity.class);
        addClickAnimationAndNavigation(qna, CheckDoubt.class);

        return view;
    }

    /**
     * Animates the CardView with a sliding and fading effect.
     *
     * @param cardView The CardView to animate.
     * @param startDelay The delay before the animation starts, in milliseconds.
     */
    private void animateEntrance(CardView cardView, int startDelay) {
        // Slide in animation (translationY) and fade in (alpha)
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(cardView, "translationY", 300f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);

        // Combine animations into an AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(slideIn, fadeIn);
        animatorSet.setDuration(500); // Animation duration in milliseconds
        animatorSet.setStartDelay(startDelay); // Delay before animation starts
        animatorSet.start();
    }

    /**
     * Adds click animation and navigation functionality to a CardView.
     *
     * @param cardView The CardView to animate and navigate.
     * @param targetActivity The activity to navigate to on click.
     */
    private void addClickAnimationAndNavigation(CardView cardView, Class<?> targetActivity) {
        cardView.setOnClickListener(v -> {
            // Scale up and scale down animation
            ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 1.4f);
            ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.4f);
            ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(cardView, "scaleX", 1.4f, 1f);
            ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(cardView, "scaleY", 1.4f, 1f);

            AnimatorSet clickAnimatorSet = new AnimatorSet();
            clickAnimatorSet.play(scaleXUp).with(scaleYUp);
            clickAnimatorSet.play(scaleXDown).after(scaleXUp);
            clickAnimatorSet.play(scaleYDown).after(scaleYUp);
            clickAnimatorSet.setDuration(300); // Animation duration in milliseconds

            clickAnimatorSet.start();

            // Navigate to the respective activity after animation completes
            clickAnimatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    super.onAnimationEnd(animation);
                    Intent intent = new Intent(getActivity(), targetActivity);
                    startActivity(intent);
                }
            });
        });
    }
}
