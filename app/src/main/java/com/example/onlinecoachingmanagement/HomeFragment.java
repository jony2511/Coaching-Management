package com.example.onlinecoachingmanagement;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private CardView exam, routine, qna, material,leaderboard,branches;
    private ViewPager2 imageCarousel;
    private ImageCarouselAdapter carouselAdapter;
    private List<Integer> imageList;
    private Handler carouselHandler;
    private Runnable carouselRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        exam = view.findViewById(R.id.exmas);
        routine = view.findViewById(R.id.routine);
        qna = view.findViewById(R.id.qna);
        material = view.findViewById(R.id.materials);
        imageCarousel = view.findViewById(R.id.imageCarousel);
        leaderboard=view.findViewById(R.id.leaderboard);
        branches=view.findViewById(R.id.branches);

        imageList = Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3); // Add your drawable resources
        carouselAdapter = new ImageCarouselAdapter(imageList, getContext());
        imageCarousel.setAdapter(carouselAdapter);

        setupAutoScroll();

        animateEntrance(exam, 0);
        animateEntrance(material, 200);
        animateEntrance(routine, 400);
        animateEntrance(qna, 600);
        animateEntrance(leaderboard, 800);
        animateEntrance(branches, 1000);

        addClickAnimationAndNavigation(exam, UserActivity.class);
        addClickAnimationAndNavigation(routine, RoutineActivity.class);
        addClickAnimationAndNavigation(qna, CheckDoubt.class);
        addClickAnimationAndNavigation(material, ShowMaterials.class);
        addClickAnimationAndNavigation(leaderboard, LeaderboardActivity.class);
        addClickAnimationAndNavigation(branches, BranchesActivity.class);

        return view;
    }


    private void animateEntrance(CardView cardView, int startDelay) {
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(cardView, "translationY", 300f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(slideIn, fadeIn);
        animatorSet.setDuration(500); // Animation duration in milliseconds
        animatorSet.setStartDelay(startDelay); // Delay before animation starts
        animatorSet.start();
    }

    private void addClickAnimationAndNavigation(CardView cardView, Class<?> targetActivity) {
        cardView.setOnClickListener(v -> {
            ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 1.3f);
            ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.3f);
            ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(cardView, "scaleX", 1.3f, 1f);
            ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(cardView, "scaleY", 1.3f, 1f);

            AnimatorSet clickAnimatorSet = new AnimatorSet();
            clickAnimatorSet.play(scaleXUp).with(scaleYUp);
            clickAnimatorSet.play(scaleXDown).after(scaleXUp);
            clickAnimatorSet.play(scaleYDown).after(scaleYUp);
            clickAnimatorSet.setDuration(300);

            clickAnimatorSet.start();

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

    private void setupAutoScroll() {
        carouselHandler = new Handler(Looper.getMainLooper());
        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = imageCarousel.getCurrentItem();
                int nextItem = (currentItem + 1) % imageList.size(); // Loop back to the first image
                imageCarousel.setCurrentItem(nextItem, true);

                carouselHandler.postDelayed(this, 5000); // Change image every 5 seconds
            }
        };

        carouselHandler.postDelayed(carouselRunnable, 5000);

        imageCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // Pause auto-scrolling when user interacts
                    carouselHandler.removeCallbacks(carouselRunnable);
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    // Resume auto-scrolling with consistent timing
                    carouselHandler.removeCallbacks(carouselRunnable);
                    carouselHandler.postDelayed(carouselRunnable, 5000);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove callbacks to avoid memory leaks
        if (carouselHandler != null) {
            carouselHandler.removeCallbacks(carouselRunnable);
        }
    }
}

class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ViewHolder> {

    private List<Integer> imageList;
    private Context context;

    public ImageCarouselAdapter(List<Integer> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_carousel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImage);
        }
    }
}
