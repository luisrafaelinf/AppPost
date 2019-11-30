package com.itla.apppost.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.itla.apppost.R;
import com.itla.apppost.constant.TypePost;
import com.itla.apppost.response.Post;
import com.itla.apppost.util.DateUtil;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PostCommentAdapter extends PostAdapter {

	public PostCommentAdapter(List<Post> posts, Context context) {
		super(posts, context);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		 if (viewType == TypePost.TAG) {
			return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout, parent, false)) {};
		} else if (viewType == TypePost.COMMENT) {
			return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false)) {};
		} else {
			return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false)) {};
		}

	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

		final View view = holder.itemView;
		final Post post = posts.get(position);

		if (posts.get(position).getType() == TypePost.POST) {

			super.onBindViewHolder(holder, position);

			CardView cardView = view.findViewById(R.id.cardPost);
			cardView.setClickable(false);

			TextView txtPostContent = view.findViewById(R.id.txtPostContent);
			txtPostContent.setMaxLines(Integer.MAX_VALUE);

		} else if (posts.get(position).getType() == TypePost.COMMENT) {

			TextView txtAuthorComment = view.findViewById(R.id.txtAuthorComment);
			TextView txtDateComment = view.findViewById(R.id.txtDateComment);
			TextView txtCommentPost = view.findViewById(R.id.txtCommentPost);

			txtAuthorComment.setText(post.getUserName());

			String dateTime = DateUtil.longToDateTimeFormartter(Long.parseLong(post.getCreatedAt()));
			txtDateComment.setText(dateTime);

			txtCommentPost.setText(post.getBody());

		} else if (posts.get(position).getType() == TypePost.TAG) {

			final ChipGroup tagGroup = view.findViewById(R.id.tagGroup);
			tagGroup.removeAllViews();

			List<String> tags = Arrays.asList(post.getTags());
			for (String tagText : tags) {

				Chip tag = new Chip(tagGroup.getContext());
				tag.setText(tagText);
				tag.setCheckable(false);
				tag.setChipBackgroundColorResource(R.color.material_grey_100);
				tag.setChipStrokeWidth(2);
				tag.setChipStrokeColor(ColorStateList.valueOf(context.getResources().getColor(R.color.secondary_text_light)));
				tagGroup.addView(tag);

			}

		}

	}

	private void addTag(String s) {

	}

	@Override
	public int getItemViewType(int position) {

		if (posts.isEmpty()) return 0;

		return posts.get(position).getType();
	}

	@Override
	public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {}

}
