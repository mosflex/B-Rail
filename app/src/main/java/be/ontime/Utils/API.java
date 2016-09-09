/*
package be.b_rail.Utils;

*/
/**
 * Created by Jawad on 10-02-16.
 *//*

public class API {

          public class AdapterRecyclerGroups
                extends AbstractExpandableItemAdapter<AdapterRecyclerGroups.MyGroupViewHolder, AdapterRecyclerGroups.MyChildViewHolder>
                implements ExpandableSwipeableItemAdapter<AdapterRecyclerGroups.MyGroupViewHolder, AdapterRecyclerGroups.MyChildViewHolder> {

            private GroupsDataProvider  mProvider;
            private static Context      mContext;

            // NOTE: Make accessible with short name
            private interface Expandable extends ExpandableItemConstants {
            }
            private interface Swipeable extends SwipeableItemConstants {
            }

            private static RecyclerViewExpandableItemManager mExpandableItemManager;

            private EventListener mEventListener;
            private View.OnClickListener mItemViewOnClickListener;
            private View.OnClickListener mSwipeableViewContainerOnClickListener;
            public interface EventListener {
                void onGroupItemAddContact(int groupPosition);
                void onGroupItemRemoved(int groupPosition);

                void onChildItemSended(int groupPosition, int childPosition);
                void onChildItemCalled(int groupPosition, int childPosition);

                void onItemViewClicked(View v);
            }

            private View.OnClickListener mItemOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemView(v);
                }
            };

            public static abstract class MyBaseViewHolder extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
                public LinearLayout     mContainer;
                private int             mExpandStateFlags;

                public MyBaseViewHolder(View v, View.OnClickListener clickListener) {
                    super(v);
                    mContainer = (LinearLayout) v.findViewById(R.id.container);

                    mContainer.setOnClickListener(clickListener);
                }
                @Override
                public int getExpandStateFlags() {
                    return mExpandStateFlags;
                }

                @Override
                public void setExpandStateFlags(int flag) {
                    mExpandStateFlags = flag;
                }

                @Override
                public View getSwipeableContainerView() {
                    return mContainer;
                }
            }

            public static class MyGroupViewHolder extends MyBaseViewHolder{

                public ExpandableItemIndicator mIndicator;

                public TextView mTextViewNameGroup;
                public TextView mTextViewCountGroup;

                public MyGroupViewHolder(View v, View.OnClickListener clickListener) {
                    super(v, clickListener);


                    mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);


                    mTextViewNameGroup   = (TextView) v.findViewById(R.id.txtNameGroup);
                    mTextViewCountGroup  = (TextView) v.findViewById(R.id.txtCountGroup);

                }
            }

            public static class MyChildViewHolder extends MyBaseViewHolder {

                public FloatingActionButton mButtonRemoveContact;
                public TextView             mTextViewNameContact;
                public TextView             mTextViewPhoneContact;
                public ImageView            imgContact;

                public MyChildViewHolder(View v, View.OnClickListener clickListener) {
                    super(v, clickListener);
                    mButtonRemoveContact  = (FloatingActionButton) v.findViewById(R.id.button_remove_contact);
                    mTextViewNameContact  = (TextView) v.findViewById(R.id.txtNameContact);
                    mTextViewPhoneContact = (TextView) v.findViewById(R.id.txtPhoneContact);
                    imgContact            = (ImageView) v.findViewById(R.id.imgContact);
                    mButtonRemoveContact.setOnClickListener(clickListener);

                }
            }
            public AdapterRecyclerGroups(Context context, RecyclerViewExpandableItemManager expandableItemManager) {

                mContext = context;
                mExpandableItemManager = expandableItemManager;
                mProvider = new GroupsDataProvider(context);
                mItemViewOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemViewClick(v);
                    }
                };
                mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSwipeableViewContainerClick(v);
                    }
                };

                // ExpandableItemAdapter requires stable ID, and also
                // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
                setHasStableIds(true);
            }
            private void onItemViewClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onItemViewClicked(v);  // true --- pinned
                }
            }
            @Override
            public int getGroupCount() {
                return mProvider.getGroupCount();
            }

            @Override
            public int getChildCount(int groupPosition) {
                return mProvider.getChildCount(groupPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return mProvider.getGroupItem(groupPosition).getGroupId();
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return mProvider.getChildItem(groupPosition, childPosition).getChildId();
            }

            @Override
            public int getGroupItemViewType(int groupPosition) {
                return 0;
            }

            @Override
            public int getChildItemViewType(int groupPosition, int childPosition) {
                return 0;
            }





        }
*/
