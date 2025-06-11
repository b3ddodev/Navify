package com.navify;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import com.navify.Event.INavifyBadgeResetListener;
import com.navify.Event.INavifyDoubleTapSelectedItemListener;
import com.navify.Event.INavifyLongSelectedItemListener;
import com.navify.Event.INavifyNormalSelectedItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Navify extends View {
    /**
     * Path used to draw the background shape of the bar
     */
    private final Path mBackgroundPath = new Path();

    /**
     * Paint for the general background of the navigation bar
     */
    private Paint mBackgroundPaint;

    /**
     * Paint used for drawing the circle behind selected item
     */
    private Paint mCirclePaint;

    /**
     * Paint used for drawing item text
     */
    private Paint mTextPaint;

    /**
     * Animator for the circle selection animation
     */
    private ValueAnimator mCircleAnimator;

    /**
     * Width and height of the view
     */
    private int mViewWidth, mViewHeight;

    /**
     * Top Y coordinate of the bar and its height
     */
    private float mBarTopY, mBarHeight;

    /**
     * Circle radius and vertical center Y
     */
    private float mCircleRadius, mCircleCenterY;

    /**
     * X position used during animation transitions
     */
    private float mAnimCenterX;

    /**
     * Background color of the navigation bar
     */
    private int mBackgroundColor = Color.WHITE;

    /**
     * Elevation/shadow of the background bar
     */
    private float mBackgroundElevation = 10f;

    /**
     * Linear animation
     */
    public static final int LINEAR = 0;
    /**
     * Accelerate only animation
     */
    public static final int ACCELERATE = 1;
    /**
     * Decelerate only animation
     */
    public static final int DECELERATE = 2;
    /**
     * Accelerate then decelerate
     */
    public static final int ACCELERATE_DECELERATE = 3;
    /**
     * Overshoot animation effect
     */
    public static final int OVERSHOOT = 4;
    /**
     * Bouncy animation effect
     */
    public static final int BOUNCE = 5;
    /**
     * No animation
     */
    public static final int NONE = 6;
    /**
     * Animation starts backward before forward motion
     */
    public static final int ANTICIPATE = 7;
    /**
     * Anticipate and overshoot
     */
    public static final int ANTICIPATE_OVERSHOOT = 8;
    /**
     * Material-style fast-out-slow-in
     */
    public static final int FAST_OUT_SLOW_IN = 9;

    /**
     * Background color of the selected circle
     */
    private int mItemCircleBackgroundColor = Color.parseColor("#FF7043");

    /**
     * Elevation (shadow) of the circle
     */
    private float mItemCircleBackgroundElevation = 10f;

    /**
     * Animation duration for circle transitions
     */
    private int mItemCircleAnimationDuration = 400;

    /**
     * Interpolator used for the circle animation
     */
    private int mItemCircleAnimationInterpolator = FAST_OUT_SLOW_IN;

    /**
     * Color of selected icon
     */
    private int mItemSelectedIconColor = Color.WHITE;

    /**
     * Size of selected icon
     */
    private float mItemSelectedIconSize = 36f;

    /**
     * Color of unselected icon
     */
    private int mItemUnSelectedIconColor = Color.parseColor("#FF7043");

    /**
     * Size of unselected icon
     */
    private float mItemUnSelectedIconSize = 24f;


    /**
     * Color of selected text
     */
    private int mItemSelectedTextColor = Color.BLACK;

    /**
     * Size of selected text (sp)
     */
    private float mItemSelectedTextSize = 14f;

    /**
     * Color of unselected text
     */
    private int mItemUnSelectedTextColor = Color.GRAY;

    /**
     * Size of unselected text (sp)
     */
    private float mItemUnSelectedTextSize = 8f;

    /**
     * Extra spacing added to selected item text
     */
    private float mItemTextSelectedExtraSpace = 15f;

    /**
     * Whether selected item text should be bold
     */
    private boolean mItemTextBold = true;

    /**
     * Whether item text should be ALL CAPS
     */
    private boolean mItemTextAllCaps = true;

    /**
     * Font resource ID for item text
     */
    int mItemTextChooseFontResourcesID = 0;

    /**
     * Loaded Typeface object for item text
     */
    private Typeface mItemTextChooseFont = null;


    /**
     * Show label only for selected item
     */
    public static final int NAVIFY_SELECTED = 0;

    /**
     * Always show labels
     */
    public static final int NAVIFY_LABELED = 1;

    /**
     * Never show labels
     */
    public static final int NAVIFY_UNLABELED = 2;

    /**
     * Current label visibility mode
     */
    private int mItemLabelVisibilityMode = NAVIFY_SELECTED;

    /**
     * Currently selected item index
     */
    private int mItemSelectedIndex = 0;

    /**
     * Menu resource to inflate for navigation items
     */
    private int mMenuResource = 0;

    /**
     * Enable haptic feedback on item selection
     */
    private boolean mHapticEnabled = false;

    /**
     * Force RTL layout manually, if not null
     */
    private Boolean mForceRTL = null;

    /**
     * Number of navigation items
     */
    private int mNavifyItemCount = 0;

    /**
     * Max allowed items
     */
    private static final int NAVIFY_MAX_ITEMS_SIZE = 5;

    /**
     * Drawable icons for each nav item
     */
    private final Drawable[] mNavifyItemIcon = new Drawable[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Icon sizes for each nav item
     */
    private final float[] mNavifyItemIconSize = new float[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Icon alpha values
     */
    private final float[] mNavifyItemIconAlphas = new float[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Titles of each nav item
     */
    private final String[] mNavifyItemTitle = new String[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Text alpha values
     */
    private final float[] mNavifyItemTextAlphas = new float[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Y offset animations for text
     */
    private final float[] mNavifyTextOffsets = new float[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * How far text moves during animation (in px)
     */
    private float mNavifyItemTextAnimDistancePX;

    /**
     * Disable reelecting the same item
     */
    private boolean mDisableSelectedItem = false;

    /**
     * Listener for normal item selection
     */
    private INavifyNormalSelectedItemListener mNavifyNormalSelectedItemListener;

    /**
     * Listener for long press selection
     */
    private INavifyLongSelectedItemListener mLongSelectedItemListener;

    /**
     * Listener for double-tap selection
     */
    private INavifyDoubleTapSelectedItemListener mDoubleTapSelectedItemListener;

    /**
     * Time of last tap (used for double tap detection)
     */
    private long mLastTapTime = 0;

    /**
     * Double tap timeout threshold (ms)
     */
    private static final long DOUBLE_TAP_TIMEOUT = 300;

    /**
     * Last tapped item index
     */
    private int mLastTapIndex = -1;

    /**
     * Index of item currently being pressed
     */
    private int mPressedIndex = -1;

    /**
     * Whether badges are enabled
     */
    private boolean mItemBadge = false;

    /**
     * Background color of badge
     */
    private int mItemBadgeBackgroundColor = Color.RED;

    /**
     * Max count to display before showing “99+”
     */
    private int mItemBadgeMaxCount = 99;

    /**
     * Enable badge animation
     */
    private boolean mItemBadgeAnimation = false;

    /**
     * Index of badge-enabled item
     */
    private int mItemBadgeIndex = -1;

    /**
     * Position of badge on item (start or end)
     */
    private int mItemBadgePosition = BADGE_POSITION_END;

    /**
     * Currently selected badge count
     */
    private int mItemBadgeSelectedCount = 99;

    /**
     * Text color of badge
     */
    private int mItemBadgeSelectedTextColor = Color.WHITE;

    /**
     * Text size of badge (sp)
     */
    private float mItemBadgeSelectedTextSize = 8f;

    /**
     * Font resource for badge text
     */
    private int mItemBadgeSelectedTextChooseFont = 0;

    /**
     * Whether to skip showing badge on selected item
     */
    private boolean mSkipBadgeOnSelected = true;

    /**
     * Whether to auto-remove badge on select
     */
    private boolean mAutoRemoveBadgeOnSelect = true;

    /**
     * Paint object for drawing badge background
     */
    private Paint mBadgeBackgroundPaint;

    /**
     * Paint object for drawing badge text
     */
    private Paint mBadgeTextPaint;

    /**
     * Typeface used for badge text
     */
    private Typeface mBadgeChooseFont = null;

    /**
     * Boolean array to enable/disable badge per item
     */
    private final boolean[] mBadgeEnabled = new boolean[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Badge text content per item
     */
    private final String[] mBadgeText = new String[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Badge animation scale values
     */
    private final float[] mBadgeScale = new float[NAVIFY_MAX_ITEMS_SIZE];

    /**
     * Badge alignment: start
     */
    public static final int BADGE_POSITION_START = 0;

    /**
     * Badge alignment: end
     */
    public static final int BADGE_POSITION_END = 1;

    /**
     * Listener triggered when badge is reset
     */
    private INavifyBadgeResetListener mBadgeResetListener = null;

    /**
     * Popup window shown when badge updates
     */
    private PopupWindow mBadgePopupWindow;

    /**
     * Handler used to schedule popup window
     */
    private final Handler mBadgePopupWindowHandler = new Handler();

    /**
     * Background color of badge popup
     */
    private int mBadgePopupWindowBackgroundColor = Color.parseColor("#CC000000");

    /**
     * Fade-in animation duration (ms)
     */
    private long mBadgePopupWindowFadeInAnimationDuration = 200L;

    /**
     * Fade-out animation duration (ms)
     */
    private long mBadgePopupWindowFadeOutAnimationDuration = 200L;

    /**
     * Duration to show popup before auto-hide (ms)
     */
    private long mBadgePopupWindowShowAnimationDuration = 2000L;

    /**
     * Y offset position of popup (dp → px)
     */
    private float mBadgePopupWindowOffsetY = dpToPx(30f);

    /**
     * Text for new notification popup
     */
    private String mBadgePopupWindowNewText = "New Notifications!";

    /**
     * Text for now notification popup
     */
    private String mBadgePopupWindowNowText = "Now Notifications!";

    /**
     * Text color inside popup
     */
    private int mBadgePopupWindowTextColor = Color.WHITE;

    /**
     * Text size inside popup (sp)
     */
    private float mBadgePopupWindowTextSize = 12f;

    /**
     * Whether popup text is all caps
     */
    private boolean mBadgePopupWindowTextAllCaps = false;

    /**
     * Font used for popup text
     */
    private Typeface mBadgePopupWindowTextChooseFont = null;

    public Navify(Context mContext) {
        super(mContext);
        MervNavigationViewController(null);
    }

    public Navify(Context mContext, @Nullable AttributeSet mAttributeSet) {
        super(mContext, mAttributeSet);
        MervNavigationViewController(mAttributeSet);
    }

    public Navify(Context mContext, @Nullable AttributeSet mAttributeSet, int mDefStyleAttr) {
        super(mContext, mAttributeSet, mDefStyleAttr);
        MervNavigationViewController(mAttributeSet);
    }

    /**
     * Initializes all visual and behavioral configurations of the Navify Navigation View.
     * <p>
     * Loads custom XML attributes, calculates default display-related values,
     * prepares paint objects for drawing UI elements, sets icon/text defaults,
     * and processes menu, font, and badge-related settings.
     *
     * @param mAttributeSet AttributeSet passed from XML layout
     */
    private void MervNavigationViewController(@Nullable AttributeSet mAttributeSet) {
        // Calculate pixel values from dp/sp using screen density
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        float mNavifyDefaultBackgroundElevation = mBackgroundElevation * mDisplayMetrics.density;
        float mNavifyDefaultCircleBackgroundElevation = mItemCircleBackgroundElevation * mDisplayMetrics.density;
        float mNavifyDefaultItemSelectedIconSize = mItemSelectedIconSize * mDisplayMetrics.density;
        float mNavifyDefaultUnSelectedIconSize = mItemUnSelectedIconSize * mDisplayMetrics.density;
        float mNavifyDefaultItemSelectedTextSize = mItemSelectedTextSize * mDisplayMetrics.scaledDensity;
        float mNavifyDefaultItemUnSelectedTextSize = mItemUnSelectedTextSize * mDisplayMetrics.scaledDensity;

        // Default animation offset for text
        mNavifyItemTextAnimDistancePX = dpToPx(20f);

        // If view is inflated with XML attributes, read and apply them
        if (mAttributeSet != null) {
            TypedArray mTypedArray = getContext().obtainStyledAttributes(mAttributeSet, R.styleable.Navify);

            // Initialize badge-related values (default or XML override)
            BadgeController();

            // Background style
            mBackgroundColor = mTypedArray.getColor(R.styleable.Navify_mBackgroundColor, Color.WHITE);
            mBackgroundElevation = mTypedArray.getDimension(R.styleable.Navify_mBackgroundElevation, mNavifyDefaultBackgroundElevation);

            // Circle item background
            mItemCircleBackgroundColor = mTypedArray.getColor(R.styleable.Navify_mItemCircleBackgroundColor, Color.parseColor("#FF7043"));
            mItemCircleBackgroundElevation = mTypedArray.getDimension(R.styleable.Navify_mItemCircleBackgroundElevation, mNavifyDefaultCircleBackgroundElevation);
            mItemCircleAnimationDuration = mTypedArray.getInt(R.styleable.Navify_mItemCircleAnimationDuration, mItemCircleAnimationDuration);
            mItemCircleAnimationInterpolator = mTypedArray.getInt(R.styleable.Navify_mItemCircleAnimationInterpolator, mItemCircleAnimationInterpolator);

            // Icon styling
            mItemSelectedIconColor = mTypedArray.getColor(R.styleable.Navify_mItemSelectedIconColor, Color.WHITE);
            mItemSelectedIconSize = mTypedArray.getDimension(R.styleable.Navify_mItemSelectedIconSize, mNavifyDefaultItemSelectedIconSize);
            mItemUnSelectedIconColor = mTypedArray.getColor(R.styleable.Navify_mItemUnSelectedIconColor, Color.parseColor("#FF7043"));
            mItemUnSelectedIconSize = mTypedArray.getDimension(R.styleable.Navify_mItemUnSelectedIconSize, mNavifyDefaultUnSelectedIconSize);

            // Text styling
            mItemSelectedTextColor = mTypedArray.getColor(R.styleable.Navify_mItemTextSelectedColor, mItemSelectedTextColor);
            mItemSelectedTextSize = mTypedArray.getDimension(R.styleable.Navify_mItemTextSelectedSize, mNavifyDefaultItemSelectedTextSize);
            mItemUnSelectedTextColor = mTypedArray.getColor(R.styleable.Navify_mItemTextUnSelectedColor, mItemUnSelectedTextColor);
            mItemUnSelectedTextSize = mTypedArray.getDimension(R.styleable.Navify_mItemTextUnSelectedSize, mNavifyDefaultItemUnSelectedTextSize);
            mItemTextSelectedExtraSpace = mTypedArray.getDimension(R.styleable.Navify_mItemTextSelectedExtraSpace, mItemTextSelectedExtraSpace);
            mItemTextBold = mTypedArray.getBoolean(R.styleable.Navify_mItemTextBold, mItemTextBold);
            mItemTextAllCaps = mTypedArray.getBoolean(R.styleable.Navify_mItemTextAllCaps, mItemTextAllCaps);

            // Custom font handling
            mItemTextChooseFontResourcesID = mTypedArray.getResourceId(R.styleable.Navify_mItemTextChooseFont, 0);
            if (mItemTextChooseFontResourcesID != 0) {
                try {
                    Typeface mTypeFace = ResourcesCompat.getFont(getContext(), mItemTextChooseFontResourcesID);
                    mItemTextChooseFont = (mTypeFace != null) ? mTypeFace : Typeface.DEFAULT;
                } catch (Exception mException) {
                    Log.e("Navify", "Invalid font source: " + mException.getMessage());
                    mItemTextChooseFont = Typeface.DEFAULT;
                }
            } else {
                mItemTextChooseFont = Typeface.DEFAULT;
            }

            // Other configuration from XML
            mItemLabelVisibilityMode = mTypedArray.getInt(R.styleable.Navify_mItemLabelVisibilityMode, mItemLabelVisibilityMode);
            mItemSelectedIndex = mTypedArray.getInt(R.styleable.Navify_mItemSelectedIndex, mItemSelectedIndex);
            mMenuResource = mTypedArray.getResourceId(R.styleable.Navify_mMenuResource, mMenuResource);
            mHapticEnabled = mTypedArray.getBoolean(R.styleable.Navify_mHapticEnabled, mHapticEnabled);
            if (mTypedArray.hasValue(R.styleable.Navify_mForceRTL)) {
                mForceRTL = mTypedArray.getBoolean(R.styleable.Navify_mForceRTL, false);
            }

            mTypedArray.recycle();
        }

        // Initialize background paint with shadow
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setShadowLayer(mBackgroundElevation, 0, 0, Color.parseColor("#33000000"));

        // Circle paint (for selected item highlight)
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mItemCircleBackgroundColor);
        mCirclePaint.setShadowLayer(mItemCircleBackgroundElevation, 0, 0, Color.parseColor("#33000000"));

        // Text paint (for item labels)
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mItemSelectedTextColor);
        mTextPaint.setTextSize(mItemSelectedTextSize);
        mTextPaint.setTypeface(mItemTextChooseFont);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(mItemTextBold);

        // Initialize item slots with default states
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIcon[mI] = null;
            mNavifyItemTitle[mI] = null;
            mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
            mNavifyItemTextAlphas[mI] = 0f;
            mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
        }

        // Default item count and animation center
        mNavifyItemCount = 0;
        mAnimCenterX = -1;

        // Load menu if defined
        if (mMenuResource != 0) {
            setMenu(mMenuResource);
        }

        // Required for drawing shadows (on older APIs)
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * Initializes paint properties and visual settings for badges.
     * <p>
     * Sets up badge background paint, text paint (including font if available),
     * and resets badge scale values for all navigation items.
     */
    private void BadgeController() {
        // Get screen density to calculate proper text size
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();

        // Paint for badge background (circle)
        mBadgeBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        mBadgeBackgroundPaint.setColor(mItemBadgeBackgroundColor);

        // Paint for badge text
        mBadgeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBadgeTextPaint.setStyle(Paint.Style.FILL);
        mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
        mBadgeTextPaint.setColor(mItemBadgeSelectedTextColor);
        mBadgeTextPaint.setTextSize(mItemBadgeSelectedTextSize * mDisplayMetrics.scaledDensity);

        // Apply custom font to badge text (if defined)
        if (mItemBadgeSelectedTextChooseFont != 0) {
            try {
                mBadgeChooseFont = ResourcesCompat.getFont(getContext(), mItemBadgeSelectedTextChooseFont);
                if (mBadgeChooseFont != null) {
                    mBadgeTextPaint.setTypeface(mBadgeChooseFont);
                }
            } catch (Exception mException) {
                Log.w("Navify", "Badge font init failed: " + mException.getMessage());
            }
        }

        // Set default scale (no animation) for all badge items
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mBadgeScale[mI] = 1f;
        }
    }


    /**
     * Adds a new navigation item manually to the Navify view.
     * <p>
     * Only available when no menu XML has been assigned.
     * Configures visual state for the item (icon size, text alpha, offset),
     * sets badge state if applicable, and updates view layout.
     *
     * @param mIcon  Drawable icon for the navigation item
     * @param mTitle Title text for the navigation item
     * @throws IllegalStateException if menu has been assigned or max item limit reached
     */
    public void setAddItem(Drawable mIcon, String mTitle) {
        // Prevent manual item addition if a menu has already been set
        if (mMenuResource != 0) {
            throw new IllegalStateException("This component has previously been assigned a menu via XML or code. " + "Please clear the menu by calling clearMenu() first, then add items manually.");
        }

        // Limit the number of items that can be added
        if (mNavifyItemCount >= NAVIFY_MAX_ITEMS_SIZE) {
            throw new IllegalStateException("No more than the following can be added: " + NAVIFY_MAX_ITEMS_SIZE + " item. BottomNavigationView is allowed this much :)");
        }

        // Set icon and title for the new item
        mNavifyItemIcon[mNavifyItemCount] = mIcon;
        mNavifyItemTitle[mNavifyItemCount] = mTitle;

        // Configure the visual state based on whether this is the selected item
        if (mNavifyItemCount == mItemSelectedIndex) {
            mNavifyItemIconSize[mNavifyItemCount] = mItemSelectedIconSize;
            mNavifyItemTextAlphas[mNavifyItemCount] = 1f;
            mNavifyTextOffsets[mNavifyItemCount] = 0f;
        } else {
            mNavifyItemIconSize[mNavifyItemCount] = mItemUnSelectedIconSize;
            mNavifyItemTextAlphas[mNavifyItemCount] = 0f;
            mNavifyTextOffsets[mNavifyItemCount] = mNavifyItemTextAnimDistancePX;
        }

        // Move to next slot
        mNavifyItemCount++;

        // If this is the first item ever added, force it as selected by default
        if (mNavifyItemCount == 1) {
            mItemSelectedIndex = 0;
            mNavifyItemIconSize[0] = mItemSelectedIconSize;
            mNavifyItemTextAlphas[0] = 1f;
            mNavifyTextOffsets[0] = 0f;
        }

        // Set all icon alphas to visible (fully opaque)
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIconAlphas[mI] = 1f;
        }

        // Reapply badge if this item matches the badge index
        int mBadgeNewIndex = mNavifyItemCount - 1;
        if (mItemBadge && mBadgeNewIndex == mItemBadgeIndex) {
            setItemBadgeSelectedCount(mItemBadgeSelectedCount);
        }

        // Redraw view with new item
        requestLayout();
        invalidate();
    }


    /**
     * Loads and parses a menu resource XML to initialize Navify items.
     * <p>
     * Inflates the given menu, extracts valid items (with icon and title),
     * and sets up internal icon/text states accordingly.
     * <p>
     * This method cannot be used if manual items have already been added.
     *
     * @param mMenuResourcesID Resource ID of the menu XML (e.g. R.menu.bottom_menu)
     * @throws IllegalArgumentException if menu ID is 0
     * @throws IllegalStateException    if manual items exist or menu is empty/invalid
     */
    public void setMenu(int mMenuResourcesID) {
        // Validate menu resource ID
        if (mMenuResourcesID == 0) {
            throw new IllegalArgumentException("Menu resource ID cannot be 0. Please provide a valid menu resource.");
        }

        // Prevent setting menu if manual items have already been added
        if (mNavifyItemCount > 0 && mNavifyItemIcon[0] != null) {
            throw new IllegalStateException("A menu item was added manually. Please clear the manual items with clearItems() first and then call setMenu().");
        }

        // Inflate the menu using a dummy PopupMenu
        PopupMenu mPopupMenu = new PopupMenu(getContext(), this);
        mPopupMenu.getMenuInflater().inflate(mMenuResourcesID, mPopupMenu.getMenu());
        Menu mMenu = mPopupMenu.getMenu();

        // Count how many raw items were loaded
        int mRawCount = mMenu.size();
        if (mRawCount == 0) {
            throw new IllegalStateException("There are no items in the menu source. At least one item is required.");
        }

        // Prepare temp lists for valid menu items (with icon + title)
        List<Drawable> mValidIcons = new ArrayList<>();
        List<String> mValidTitles = new ArrayList<>();

        for (int mI = 0; mI < mRawCount; mI++) {
            MenuItem mItem = mMenu.getItem(mI);
            Drawable mIcon = mItem.getIcon();
            CharSequence mTitle = mItem.getTitle();

            // Skip invalid entries (missing icon or title)
            if (mIcon == null || mTitle == null || mTitle.toString().trim().isEmpty()) {
                Log.w("Navify", "menu item " + mI + " invalid. Skipping.");
                continue;
            }

            mValidIcons.add(mIcon);
            mValidTitles.add(mTitle.toString());
        }

        // Check final valid item count
        int mCount = mValidIcons.size();
        if (mCount == 0) {
            throw new IllegalStateException("No valid menu items found. Please add at least one item with icon+title.");
        }
        if (mCount > NAVIFY_MAX_ITEMS_SIZE) {
            throw new IllegalStateException("Too many valid items in menu source (" + mCount + "). Maximum allowed value " + NAVIFY_MAX_ITEMS_SIZE);
        }

        // Save count and clamp selected index
        mNavifyItemCount = mCount;
        if (mItemSelectedIndex < 0 || mItemSelectedIndex >= mCount) {
            mItemSelectedIndex = 0;
        }

        // Fill icon & title arrays with menu data
        for (int mI = 0; mI < mCount; mI++) {
            mNavifyItemIcon[mI] = mValidIcons.get(mI);
            mNavifyItemTitle[mI] = mValidTitles.get(mI);

            if (mI == mItemSelectedIndex) {
                mNavifyItemIconSize[mI] = mItemSelectedIconSize;
                mNavifyItemTextAlphas[mI] = 1f;
                mNavifyTextOffsets[mI] = 0f;
            } else {
                mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
                mNavifyItemTextAlphas[mI] = 0f;
                mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
            }
        }

        // Fill remaining item slots with defaults (nulls)
        for (int mI = mCount; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIcon[mI] = null;
            mNavifyItemTitle[mI] = null;
            mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
            mNavifyItemTextAlphas[mI] = 0f;
            mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
        }

        // Reset icon alpha values
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIconAlphas[mI] = 1f;
        }

        // Apply badge state if active and valid
        if (mItemBadge && mItemBadgeIndex >= 0 && mItemBadgeIndex < mNavifyItemCount) {
            setItemBadgeSelectedCount(mItemBadgeSelectedCount);
        }

        // Update animation center and redraw
        mAnimCenterX = ComputeCenterXForIndex(mItemSelectedIndex);
        requestLayout();
        invalidate();
    }


    /**
     * Measures the dimensions of the Navify view.
     * <p>
     * Calculates the required height by considering the selected icon size,
     * circle radius (for bump effect), text size, and padding.
     * Ensures a minimum height for usability.
     *
     * @param mWidthMeasureSpec  Horizontal space requirements as imposed by parent
     * @param mHeightMeasureSpec Vertical space requirements as imposed by parent
     */
    @Override
    protected void onMeasure(int mWidthMeasureSpec, int mHeightMeasureSpec) {
        // Get the total available width from the parent
        int mWidth = MeasureSpec.getSize(mWidthMeasureSpec);

        // Padding around the selected circle icon
        float mCirclePadding = dpToPx(8f);

        // Circle radius = iconSize/2 + padding
        mCircleRadius = (mItemSelectedIconSize / 2f) + mCirclePadding;

        // Extra bump radius to give the curve effect
        float mBumpExtra = dpToPx(4f);
        float mBumpRadius = mCircleRadius + mBumpExtra;

        // Max icon size between selected and unselected states
        float mMaxIconSize = Math.max(mItemSelectedIconSize, mItemUnSelectedIconSize);

        // Padding for top and bottom of bar
        float mBarPadding = dpToPx(10f);

        // Total bar height = icon + spacing + text + padding
        mBarHeight = mMaxIconSize + 10f + mItemSelectedTextSize + mBarPadding;

        // Padding at the very top of the bump area
        float mTopBarPadding = dpToPx(12f);

        // Calculate desired height with bump and top padding
        int mRawDesiredHeight = (int) Math.ceil(mBumpRadius + mBarHeight + mTopBarPadding);

        // Enforce a minimum height (48dp)
        int minHeightPx = (int) dpToPx(48f);
        int mDesiredHeight = Math.max(mRawDesiredHeight, minHeightPx);

        // Resolve height with parent constraints
        int mHeight = resolveSize(mDesiredHeight, mHeightMeasureSpec);

        // Save measured dimensions for use in drawing
        mViewWidth = mWidth;
        mViewHeight = mHeight;

        // Finalize measured size
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    /**
     * Called whenever the size of the view changes.
     * <p>
     * Recalculates layout metrics like bar height, circle position,
     * and animation center X coordinate.
     *
     * @param mNewWidth  New width of the view
     * @param mNewHeight New height of the view
     * @param mOldWidth  Previous width
     * @param mOldHeight Previous height
     */
    @Override
    protected void onSizeChanged(int mNewWidth, int mNewHeight, int mOldWidth, int mOldHeight) {
        // Save new view dimensions
        mViewWidth = mNewWidth;
        mViewHeight = mNewHeight;

        // Calculate circle radius based on selected icon size + padding
        float mCirclePadding = dpToPx(8f);
        mCircleRadius = (mItemSelectedIconSize / 2f) + mCirclePadding;

        // Total "bump" height for the curved top effect
        float mBumpExtra = dpToPx(4f);
        float mBumpTotalRadius = mCircleRadius + mBumpExtra;

        // Padding from top edge
        float mTopBarPadding = dpToPx(12f);

        // Top Y coordinate of the straight bar and circle center
        mBarTopY = mBumpTotalRadius + mTopBarPadding;
        mCircleCenterY = mBumpTotalRadius + mTopBarPadding;

        // If items exist, animate to selected index; else center
        if (mNavifyItemCount > 0) {
            mAnimCenterX = ComputeCenterXForIndex(mItemSelectedIndex);
        } else {
            mAnimCenterX = mViewWidth / 2f;
        }

        // Always call superclass implementation
        super.onSizeChanged(mNewWidth, mNewHeight, mOldWidth, mOldHeight);
    }

    /**
     * Draws the entire Navify component on the canvas.
     * <p>
     * Renders the background shape, selection circle,
     * and all navigation items (icons + texts).
     *
     * @param mCanvas Canvas object used for custom drawing
     */
    @Override
    protected void onDraw(@NonNull Canvas mCanvas) {
        super.onDraw(mCanvas);

        // If no items are available, nothing to draw
        if (mNavifyItemCount == 0) return;

        // Draw curved background bump
        DrawBackgroundBump(mCanvas);

        // Draw the circular background behind selected item
        mCanvas.drawCircle(mAnimCenterX, mCircleCenterY, mCircleRadius, mCirclePaint);

        // Draw navigation icons & texts
        DrawItems(mCanvas);
    }

    /**
     * Draws the curved background bump effect for the selected navigation item.
     * <p>
     * Uses cubic Bezier curves to create a smooth bump centered around the selected item.
     * Draws the background path from left to right, including the bump,
     * then closes the path to fill the entire nav area.
     *
     * @param mCanvas Canvas to draw on
     */
    private void DrawBackgroundBump(Canvas mCanvas) {
        // Reset previous path to start fresh
        mBackgroundPath.reset();

        // Cache frequently used values for readability
        float mViewWidthPx = mViewWidth;
        float mViewHeightPx = mViewHeight;
        float mBarTopYPx = mBarTopY;
        float mCircleRadiusPx = mCircleRadius;

        // If we have items, compute center X of selected item
        if (mNavifyItemCount > 0) {
            if (mAnimCenterX <= 0f || Float.isNaN(mAnimCenterX)) {
                mAnimCenterX = ComputeCenterXForIndex(mItemSelectedIndex);
            }
        } else {
            // Fallback to center if no items
            mAnimCenterX = mViewWidthPx / 2f;
        }

        float mCircleCenterXPx = mAnimCenterX;

        // Add a bit of extra height to bump effect
        float mBumpExtraPx = dpToPx(4f);
        float mBumpTotalRadiusPx = mCircleRadiusPx + mBumpExtraPx;

        // Calculate left and right X boundaries of the bump
        float mBumpLeftX = mCircleCenterXPx - mBumpTotalRadiusPx * 1.7f;
        float mBumpRightX = mCircleCenterXPx + mBumpTotalRadiusPx * 1.7f;

        // Start from left edge of bar
        mBackgroundPath.moveTo(0f, mBarTopYPx);
        mBackgroundPath.lineTo(mBumpLeftX, mBarTopYPx);

        // Draw first half of the bump with a cubic curve (upward arc)
        mBackgroundPath.cubicTo(mBumpLeftX + mBumpTotalRadiusPx * 0.95f, mBarTopYPx,                             // Control Point 1
                mCircleCenterXPx - mBumpTotalRadiusPx, mBarTopYPx - mBumpTotalRadiusPx * 0.95f,  // Control Point 2
                mCircleCenterXPx, mBarTopYPx - mBumpTotalRadiusPx                                // End Point (top center of bump)
        );

        // Second half of bump (downward arc)
        mBackgroundPath.cubicTo(mCircleCenterXPx + mBumpTotalRadiusPx, mBarTopYPx - mBumpTotalRadiusPx * 0.95f, mBumpRightX - mBumpTotalRadiusPx * 0.95f, mBarTopYPx, mBumpRightX, mBarTopYPx);

        // Finish drawing the rest of the nav bar (to right edge and down)
        mBackgroundPath.lineTo(mViewWidthPx, mBarTopYPx);
        mBackgroundPath.lineTo(mViewWidthPx, mViewHeightPx);
        mBackgroundPath.lineTo(0f, mViewHeightPx);
        mBackgroundPath.close();

        // Finally, draw the path using the background paint
        mCanvas.drawPath(mBackgroundPath, mBackgroundPaint);
    }

    /**
     * Draws all navigation items including icons, labels, and optional badges.
     * <p>
     * Calculates position and size based on selection state, label mode, and RTL layout.
     * Applies tint, alpha, and text transformations. Draws badges if enabled.
     *
     * @param mCanvas Canvas to draw items on
     */
    private void DrawItems(Canvas mCanvas) {
        // Don't draw if no items
        if (mNavifyItemCount == 0) return;

        // Calculate width for each section
        float mSectionWidth = mViewWidth / (float) mNavifyItemCount;
        float GAP_2_DP = dpToPx(2f); // gap between icon and text

        for (int mI = 0; mI < mNavifyItemCount; mI++) {
            // Adjust index if RTL is active
            int mDrawIndex = RTLIndex(mI);

            // Center X of the current item
            float mDrawCenterX = mSectionWidth * mDrawIndex + mSectionWidth / 2f;

            boolean mDrawSel = (mI == mItemSelectedIndex);
            Drawable mDrawIcon = mNavifyItemIcon[mI];
            float mDrawIconSize = mNavifyItemIconSize[mI];

            // Should we draw text label?
            boolean mDrawShowText = mItemLabelVisibilityMode == NAVIFY_LABELED || (mItemLabelVisibilityMode == NAVIFY_SELECTED && mDrawSel);

            // Configure text paint for this item
            float mDrawTextSize = mDrawSel ? mItemSelectedTextSize : mItemUnSelectedTextSize;
            mTextPaint.setTextSize(mDrawTextSize);
            float mDrawTextHeight = mDrawShowText ? (mTextPaint.descent() - mTextPaint.ascent()) : 0f;
            float mDrawExtraSelGap = mDrawSel ? dpToPx(mItemTextSelectedExtraSpace) : 0f;

            // Calculate block height for icon + text
            float mDrawBlockHeight = mDrawIconSize + (mDrawShowText ? GAP_2_DP : 0f) + mDrawTextHeight + mDrawExtraSelGap;

            // Icon Y position: higher if selected (to align with bump)
            float mDrawIconTopY = mDrawSel ? (mCircleCenterY - mDrawIconSize / 2f) : (mBarTopY + (mBarHeight - mDrawBlockHeight) / 2f);

            // --- Draw Icon ---
            if (mDrawIcon != null) {
                float mDrawLeftX = mDrawCenterX - mDrawIconSize / 2f;
                mDrawIcon.setBounds((int) mDrawLeftX, (int) mDrawIconTopY, (int) (mDrawLeftX + mDrawIconSize), (int) (mDrawIconTopY + mDrawIconSize));
                mDrawIcon.setTint(mDrawSel ? mItemSelectedIconColor : mItemUnSelectedIconColor);
                int nDrawIconAlpha = (int) (255 * mNavifyItemIconAlphas[mI]);
                mDrawIcon.setAlpha(nDrawIconAlpha);
                mDrawIcon.draw(mCanvas);
            }

            // --- Draw Text Label ---
            if (mDrawShowText) {
                String mDrawTitle = mNavifyItemTitle[mI];
                if (mDrawTitle != null && !mDrawTitle.isEmpty()) {
                    if (mItemTextAllCaps) {
                        mDrawTitle = mDrawTitle.toUpperCase(Locale.getDefault());
                    }

                    mTextPaint.setTextSize(mDrawTextSize);
                    mTextPaint.setColor(mDrawSel ? mItemSelectedTextColor : mItemUnSelectedTextColor);
                    mTextPaint.setAlpha(255);

                    float mDrawTextY = mDrawIconTopY + mDrawIconSize + GAP_2_DP + mDrawExtraSelGap - mTextPaint.ascent();
                    mCanvas.drawText(mDrawTitle, mDrawCenterX, mDrawTextY, mTextPaint);
                }
            }

            // --- Skip badge if selected item and skipping is enabled ---
            if (mSkipBadgeOnSelected && mI == mItemSelectedIndex) continue;

            // --- Draw Badge ---
            if (mBadgeEnabled[mI] && mBadgeText[mI] != null) {
                float mBadgeRadius = dpToPx(8f) * mBadgeScale[mI];
                float mIconHalf = mDrawIconSize / 2f;

                // Calculate badge position depending on direction (START/END)
                float mBX = mDrawCenterX + (mItemBadgePosition == BADGE_POSITION_END ? mIconHalf : -mIconHalf) + (mItemBadgePosition == BADGE_POSITION_END ? mBadgeRadius * 0.3f : -mBadgeRadius * 0.3f);

                float mBY = mDrawIconTopY - mBadgeRadius * 0.3f;

                // Draw badge circle
                mCanvas.drawCircle(mBX, mBY, mBadgeRadius, mBadgeBackgroundPaint);

                // Draw badge text centered in circle
                String mTaBadgeText = mBadgeText[mI];
                Paint.FontMetrics mFontMetrics = mBadgeTextPaint.getFontMetrics();
                float mTextY = mBY - (mFontMetrics.ascent + mFontMetrics.descent) / 2;
                mCanvas.drawText(mTaBadgeText, mBX, mTextY, mBadgeTextPaint);
            }
        }
    }

    /**
     * Handles touch interaction for navigation item selection.
     * <p>
     * Supports single tap, long press, and double tap gestures.
     * Triggers respective listeners or opens badge popups.
     *
     * @param mMotionEvent The motion event from touch
     * @return true if the touch was handled, false otherwise
     */
    @Override
    public boolean onTouchEvent(MotionEvent mMotionEvent) {
        // If selection is disabled or no items, ignore interaction
        if (mDisableSelectedItem) return false;
        if (mNavifyItemCount == 0) return super.onTouchEvent(mMotionEvent);

        // Get touch X position and determine which item was tapped
        float mTouchX = mMotionEvent.getX();
        float mSectionWidthPx = mViewWidth / (float) mNavifyItemCount;
        int mDisplayIDX = (int) (mTouchX / mSectionWidthPx);

        // Adjust for RTL layout if needed
        int mTappedIndex = getForceRTL() ? (mNavifyItemCount - 1 - mDisplayIDX) : mDisplayIDX;

        switch (mMotionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                // Handle press down on a valid index
                if (mTappedIndex >= 0 && mTappedIndex < mNavifyItemCount) {
                    mPressedIndex = mTappedIndex;
                    setPressed(true);

                    // Handle long press with delay
                    postDelayed(() -> {
                        if (isPressed() && mPressedIndex == mTappedIndex) {
                            if (mBadgeEnabled[mPressedIndex]) {
                                // Show badge popup if badge is active
                                BadgePopupWindow(mPressedIndex);
                            } else if (mLongSelectedItemListener != null) {
                                // Trigger long press listener
                                mLongSelectedItemListener.onNavifyLongSelectedItem(mPressedIndex);
                            }
                        }
                    }, ViewConfiguration.getLongPressTimeout());
                }
                return true;
            }

            case MotionEvent.ACTION_UP: {
                setPressed(false);
                if (mTappedIndex == mPressedIndex && mTappedIndex >= 0 && mTappedIndex < mNavifyItemCount) {
                    long mNow = System.currentTimeMillis();

                    // Handle double tap
                    if (mTappedIndex == mLastTapIndex && (mNow - mLastTapTime) < DOUBLE_TAP_TIMEOUT) {
                        if (mDoubleTapSelectedItemListener != null) {
                            mDoubleTapSelectedItemListener.onNavifyDoubleTapSelectedItem(mTappedIndex);
                        }
                    } else {
                        // Handle single tap
                        if (mTappedIndex != mItemSelectedIndex) {
                            AnimationSelectionToIndex(mTappedIndex); // animate to new index
                        } else {
                            // Notify normal (same) item tap
                            if (mNavifyNormalSelectedItemListener != null) {
                                mNavifyNormalSelectedItemListener.onNavifyNormalSelectedItem(mItemSelectedIndex);
                            }
                        }
                        performClick(); // accessibility
                    }
                    // Save last tap info for double-tap logic
                    mLastTapIndex = mTappedIndex;
                    mLastTapTime = mNow;
                }
                mPressedIndex = -1;
                return true;
            }

            case MotionEvent.ACTION_CANCEL: {
                setPressed(false);
                mPressedIndex = -1;
                return super.onTouchEvent(mMotionEvent);
            }

            default:
                return super.onTouchEvent(mMotionEvent);
        }
    }


    /**
     * Handles accessibility click events.
     * <p>
     * Ensures that custom click actions are reported for accessibility tools
     * like TalkBack or UI testing frameworks.
     *
     * @return always true, indicating the click was handled
     */
    @Override
    public boolean performClick() {
        super.performClick(); // call to ensure accessibility compatibility
        return true;
    }

    /**
     * Returns a corresponding TimeInterpolator based on animation type code.
     * <p>
     * Used to define how item selection animations behave visually over time.
     *
     * @param mType the animation type constant (e.g. LINEAR, BOUNCE)
     * @return corresponding TimeInterpolator instance
     */
    private TimeInterpolator NavifyInterpolar(int mType) {
        switch (mType) {
            case LINEAR:
                return new LinearInterpolator(); // constant speed
            case ACCELERATE:
                return new AccelerateInterpolator(); // starts slow, ends fast
            case DECELERATE:
                return new DecelerateInterpolator(); // starts fast, ends slow
            case ACCELERATE_DECELERATE:
                return new AccelerateDecelerateInterpolator(); // ease-in-out
            case OVERSHOOT:
                return new OvershootInterpolator(); // goes beyond target, then snaps
            case BOUNCE:
                return new BounceInterpolator(); // bouncy finish
            case NONE:
                return input -> 1f; // instant, no animation
            case ANTICIPATE:
                return new AnticipateInterpolator(); // starts backward before forward
            case ANTICIPATE_OVERSHOOT:
                return new AnticipateOvershootInterpolator(); // both anticipate & overshoot
            case FAST_OUT_SLOW_IN:
                return AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in); // Material motion curve
            default:
                return new OvershootInterpolator(); // fallback
        }
    }

    /**
     * Animates the transition between the previously selected and the newly selected navigation item.
     * This includes:
     * - Moving the selection circle
     * - Resizing icons
     * - Fading in/out text and icons
     * - Adjusting text position
     * It also optionally removes the badge on selection and triggers callbacks.
     *
     * @param mNewSelectedIndex The index of the newly selected item
     */
    private void AnimationSelectionToIndex(final int mNewSelectedIndex) {
        // Return early if index is invalid
        if (mNewSelectedIndex < 0 || mNewSelectedIndex >= mNavifyItemCount) return;
        if (mNavifyItemCount == 0) return;

        final int mPreviousSelectedIndex = mItemSelectedIndex;
        mItemSelectedIndex = mNewSelectedIndex;

        // Reset all icon alphas
        for (int mI = 0; mI < mNavifyItemCount; mI++) {
            mNavifyItemIconAlphas[mI] = 1f;
        }

        // Start new selected icon fully transparent
        mNavifyItemIconAlphas[mNewSelectedIndex] = 0f;

        // Reset other items (not previous or new) to unselected state
        for (int mItemIndex = 0; mItemIndex < mNavifyItemCount; mItemIndex++) {
            if (mItemIndex != mPreviousSelectedIndex && mItemIndex != mNewSelectedIndex) {
                mNavifyItemIconSize[mItemIndex] = mItemUnSelectedIconSize;
                mNavifyItemTextAlphas[mItemIndex] = 0f;
                mNavifyTextOffsets[mItemIndex] = mNavifyItemTextAnimDistancePX;
            }
        }

        // Set previous item to selected state (as animation's starting point)
        mNavifyItemIconSize[mPreviousSelectedIndex] = mItemSelectedIconSize;
        mNavifyItemTextAlphas[mPreviousSelectedIndex] = 1f;
        mNavifyTextOffsets[mPreviousSelectedIndex] = 0f;

        final float mStartX = mAnimCenterX;
        final float mEndX = ComputeCenterXForIndex(mNewSelectedIndex);

        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }

        // Move the circle to new selected item
        ValueAnimator mCircleMoveAnimator = ValueAnimator.ofFloat(mStartX, mEndX);
        mCircleMoveAnimator.setDuration(mItemCircleAnimationDuration);
        mCircleMoveAnimator.setInterpolator(NavifyInterpolar(mItemCircleAnimationInterpolator));
        mCircleMoveAnimator.addUpdateListener(animation -> {
            mAnimCenterX = (float) animation.getAnimatedValue();
            invalidate();
        });
        mCircleAnimator = mCircleMoveAnimator;

        // Animate shrinking old icon
        ValueAnimator mOldIconShrinkAnimator = ValueAnimator.ofFloat(mItemSelectedIconSize, mItemUnSelectedIconSize);
        mOldIconShrinkAnimator.setDuration(mItemCircleAnimationDuration);
        mOldIconShrinkAnimator.setInterpolator(NavifyInterpolar(mItemCircleAnimationInterpolator));
        mOldIconShrinkAnimator.addUpdateListener(animation -> {
            mNavifyItemIconSize[mPreviousSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Animate growing new icon
        ValueAnimator mNewIconGrowAnimator = ValueAnimator.ofFloat(mItemUnSelectedIconSize, mItemSelectedIconSize);
        mNewIconGrowAnimator.setDuration(mItemCircleAnimationDuration);
        mNewIconGrowAnimator.setInterpolator(NavifyInterpolar(mItemCircleAnimationInterpolator));
        mNewIconGrowAnimator.addUpdateListener(animation -> {
            mNavifyItemIconSize[mNewSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Fade out old text
        ValueAnimator mOldTextAlphaAnimator = ValueAnimator.ofFloat(1f, 0f);
        mOldTextAlphaAnimator.setDuration(mItemCircleAnimationDuration);
        mOldTextAlphaAnimator.addUpdateListener(animation -> {
            mNavifyItemTextAlphas[mPreviousSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Slide old text down
        ValueAnimator mOldTextOffsetAnimator = ValueAnimator.ofFloat(0f, mNavifyItemTextAnimDistancePX);
        mOldTextOffsetAnimator.setDuration(mItemCircleAnimationDuration);
        mOldTextOffsetAnimator.addUpdateListener(animation -> {
            mNavifyTextOffsets[mPreviousSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Fade in new text
        ValueAnimator mNewTextAlphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        mNewTextAlphaAnimator.setDuration(mItemCircleAnimationDuration);
        mNewTextAlphaAnimator.addUpdateListener(animation -> {
            mNavifyItemTextAlphas[mNewSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Slide new text up
        ValueAnimator mNewTextOffsetAnimator = ValueAnimator.ofFloat(mNavifyItemTextAnimDistancePX, 0f);
        mNewTextOffsetAnimator.setDuration(mItemCircleAnimationDuration);
        mNewTextOffsetAnimator.addUpdateListener(animation -> {
            mNavifyTextOffsets[mNewSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Fade in new icon
        ValueAnimator mIconAlphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        mIconAlphaAnimator.setDuration(mItemCircleAnimationDuration);
        mIconAlphaAnimator.setInterpolator(NavifyInterpolar(mItemCircleAnimationInterpolator));
        mIconAlphaAnimator.addUpdateListener(animation -> {
            mNavifyItemIconAlphas[mNewSelectedIndex] = (float) animation.getAnimatedValue();
            invalidate();
        });

        // Combine all animations
        AnimatorSet mCombinedAnimatorSet = new AnimatorSet();
        mCombinedAnimatorSet.setInterpolator(NavifyInterpolar(mItemCircleAnimationInterpolator));
        mCombinedAnimatorSet.playTogether(mCircleMoveAnimator, mOldIconShrinkAnimator, mNewIconGrowAnimator, mOldTextAlphaAnimator, mOldTextOffsetAnimator, mNewTextAlphaAnimator, mNewTextOffsetAnimator, mIconAlphaAnimator);

        // After animation ends: reset badge (if needed), trigger listeners, and refresh layout
        mCombinedAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator mAnimation) {
                mNavifyItemIconSize[mNewSelectedIndex] = mItemSelectedIconSize;
                mNavifyItemTextAlphas[mNewSelectedIndex] = 1f;
                mNavifyTextOffsets[mNewSelectedIndex] = 0f;
                mNavifyItemIconAlphas[mNewSelectedIndex] = 1f;

                if (mAutoRemoveBadgeOnSelect && mBadgeEnabled[mNewSelectedIndex]) {
                    mBadgeEnabled[mNewSelectedIndex] = false;
                    if (mBadgeResetListener != null) {
                        mBadgeResetListener.onNavifyBadgeReset(mNewSelectedIndex);
                    }
                }

                requestLayout();
                invalidate();

                if (mNavifyNormalSelectedItemListener != null) {
                    mNavifyNormalSelectedItemListener.onNavifyNormalSelectedItem(mNewSelectedIndex);
                }
            }
        });

        mCombinedAnimatorSet.start();
    }

    /**
     * Converts density-independent pixels (dp) to pixels (px).
     *
     * @param mDP The value in dp to convert
     * @return The corresponding pixel value
     */
    private int dpToPx(float mDP) {
        if (mDP < 0) return 0;
        return (int) (mDP * getResources().getDisplayMetrics().density + 0.5f);
    }


    /**
     * Converts scale-independent pixels (sp) to pixels (px), usually used for text sizes.
     *
     * @param mSP The value in sp to convert
     * @return The corresponding pixel value
     */
    private int spToPx(float mSP) {
        return (int) (mSP * getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }


    /**
     * Calculates the horizontal center X position for a given navigation item index.
     * <p>
     * Takes into account right-to-left (RTL) layouts using {@link #RTLIndex(int)}.
     *
     * @param mIndex The logical item index
     * @return The X coordinate of the center of that item
     */
    private float ComputeCenterXForIndex(int mIndex) {
        int drawIndex = RTLIndex(mIndex);
        float mSectionWidth = ((float) mViewWidth) / mNavifyItemCount;
        return mSectionWidth * drawIndex + mSectionWidth / 2f;
    }


    /**
     * Clears all previously added items and resets the navigation view state.
     * <p>
     * This method resets:
     * - Menu resource reference
     * - Icons, titles, sizes, alphas
     * - Badge/text visuals
     */
    public void ClearReloadItems() {
        // Reset menu reference
        mMenuResource = 0;

        // Clear all item visuals
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIcon[mI] = null;
            mNavifyItemTitle[mI] = null;
            mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
            mNavifyItemTextAlphas[mI] = 0f;
            mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
        }

        // Reset item count
        mNavifyItemCount = 0;

        // Reset icon alphas
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIconAlphas[mI] = 1f;
        }

        // Redraw the view
        invalidate();
    }


    /**
     * Converts the given index to RTL format if RTL mode is enabled.
     *
     * @param mRTLIndex The original LTR index
     * @return The RTL adjusted index
     */
    private int RTLIndex(int mRTLIndex) {
        if (!getForceRTL()) return mRTLIndex;
        return mNavifyItemCount - 1 - mRTLIndex;
    }

    /**
     * Gets the background color of the navigation bar.
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Sets the background color of the navigation bar.
     *
     * @param sBackgroundColor The color to apply
     */
    public void setBackgroundColor(int sBackgroundColor) {
        mBackgroundColor = sBackgroundColor;
        mBackgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    /**
     * Gets the elevation (shadow) of the navigation background.
     */
    public float getBackgroundElevation() {
        return mBackgroundElevation;
    }

    /**
     * Sets the elevation (shadow) of the navigation background.
     *
     * @param sBackgroundElevation The elevation in pixels
     */
    public void setBackgroundElevation(float sBackgroundElevation) {
        mBackgroundElevation = sBackgroundElevation;
        setElevation(sBackgroundElevation);
        invalidate();
    }

    /**
     * Gets the color of the selected item’s circular background.
     */
    public int getItemCircleBackgroundColor() {
        return mItemCircleBackgroundColor;
    }

    /**
     * Sets the color of the selected item’s circular background.
     *
     * @param sItemCircleBackgroundColor The color to use
     */
    public void setItemCircleBackgroundColor(int sItemCircleBackgroundColor) {
        mItemCircleBackgroundColor = sItemCircleBackgroundColor;
        mCirclePaint.setColor(sItemCircleBackgroundColor);
        invalidate();
    }

    /**
     * Gets the elevation (shadow) for the item circle.
     */
    public float getItemCircleBackgroundElevation() {
        return mItemCircleBackgroundElevation;
    }

    /**
     * Sets the elevation (shadow) for the item circle.
     *
     * @param sItemCircleBackgroundElevation The elevation value
     */
    public void setItemCircleBackgroundElevation(float sItemCircleBackgroundElevation) {
        mItemCircleBackgroundElevation = sItemCircleBackgroundElevation;
        mCirclePaint.setShadowLayer(sItemCircleBackgroundElevation, 0, 0, Color.parseColor("#33000000"));
        invalidate();
    }

    /**
     * Gets the animation duration for the item circle transition.
     */
    public int getItemCircleAnimationDuration() {
        return mItemCircleAnimationDuration;
    }

    /**
     * Sets the animation duration for the item circle transition.
     *
     * @param sItemCircleAnimationDuration Duration in milliseconds
     */
    public void setItemCircleAnimationDuration(int sItemCircleAnimationDuration) {
        mItemCircleAnimationDuration = sItemCircleAnimationDuration;
    }

    /**
     * Gets the interpolator type used for item circle animations.
     */
    public int getItemCircleAnimationInterpolator() {
        return mItemCircleAnimationInterpolator;
    }

    /**
     * Sets the interpolator type for the item circle animation.
     *
     * @param sItemCircleAnimationInterpolator The interpolator constant
     */
    public void setItemCircleAnimationInterpolator(int sItemCircleAnimationInterpolator) {
        mItemCircleAnimationInterpolator = sItemCircleAnimationInterpolator;
        if (mCircleAnimator != null) {
            mCircleAnimator.setInterpolator(NavifyInterpolar(sItemCircleAnimationInterpolator));
        }
    }

    /**
     * Gets the color of the selected item’s icon.
     */
    public int getItemSelectedIconColor() {
        return mItemSelectedIconColor;
    }

    /**
     * Sets the color of the selected item’s icon.
     *
     * @param sItemSelectedIconColor The color to apply
     */
    public void setItemSelectedIconColor(int sItemSelectedIconColor) {
        mItemSelectedIconColor = sItemSelectedIconColor;
    }

    /**
     * Gets the size of the selected item’s icon in pixels.
     */
    public float getItemSelectedIconSize() {
        return mItemSelectedIconSize;
    }

    /**
     * Sets the size of the selected item’s icon (dp).
     *
     * @param sItemSelectedIconSize Icon size in dp
     */
    public void setItemSelectedIconSize(float sItemSelectedIconSize) {
        mItemSelectedIconSize = dpToPx(sItemSelectedIconSize);
        requestLayout();
        invalidate();
    }

    /**
     * Gets the color of the unselected item icons.
     */
    public int getItemUnSelectedIconColor() {
        return mItemUnSelectedIconColor;
    }

    /**
     * Sets the color of the unselected item icons.
     *
     * @param sItemUnselectedIconColor Color to set
     */
    public void setItemUnSelectedIconColor(int sItemUnselectedIconColor) {
        mItemUnSelectedIconColor = sItemUnselectedIconColor;
        invalidate();
    }

    /**
     * Gets the size of the unselected icons in pixels.
     */
    public float getItemUnSelectedIconSize() {
        return mItemUnSelectedIconSize;
    }

    /**
     * Sets the size of the unselected icons (dp).
     *
     * @param sItemUnSelectedIconSize Size in dp
     */
    public void setItemUnSelectedIconSize(float sItemUnSelectedIconSize) {
        mItemUnSelectedIconSize = dpToPx(sItemUnSelectedIconSize);
        requestLayout();
        invalidate();
    }

    /**
     * Gets the text color of the selected item.
     */
    public int getItemSelectedTextColor() {
        return mItemSelectedTextColor;
    }

    /**
     * Sets the text color of the selected item.
     *
     * @param sItemSelectedTextColor Color value
     */
    public void setItemSelectedTextColor(int sItemSelectedTextColor) {
        mItemSelectedTextColor = sItemSelectedTextColor;
        invalidate();
    }

    /**
     * Gets the selected item's text size in pixels.
     */
    public float getItemSelectedTextSize() {
        return mItemSelectedTextSize;
    }

    /**
     * Sets the selected item's text size (sp).
     *
     * @param sItemSelectedTextSize Size in sp
     */
    public void setItemSelectedTextSize(float sItemSelectedTextSize) {
        mItemSelectedTextSize = spToPx(sItemSelectedTextSize);
        requestLayout();
        invalidate();
    }

    /**
     * Gets the unselected text color.
     */
    public int getItemUnSelectedTextColor() {
        return mItemUnSelectedTextColor;
    }

    /**
     * Sets the unselected text color.
     *
     * @param sItemUnSelectedTextColor Color value
     */
    public void setItemUnSelectedTextColor(int sItemUnSelectedTextColor) {
        mItemUnSelectedTextColor = sItemUnSelectedTextColor;
        invalidate();
    }

    /**
     * Gets the unselected text size in pixels.
     */
    public float getItemUnSelectedTextSize() {
        return mItemUnSelectedTextSize;
    }

    /**
     * Sets the unselected text size (sp).
     *
     * @param smItemUnSelectedTextSize Size in sp
     */
    public void setItemUnSelectedTextSize(float smItemUnSelectedTextSize) {
        mItemUnSelectedTextSize = spToPx(smItemUnSelectedTextSize);
        requestLayout();
        invalidate();
    }

    /**
     * Gets the extra spacing applied below the selected item’s text.
     */
    public float getItemTextSelectedExtraSpace() {
        return mItemTextSelectedExtraSpace;
    }

    /**
     * Sets the extra spacing applied below the selected item’s text.
     *
     * @param sItemTextSelectedExtraSpace Space in dp
     */
    public void setItemTextSelectedExtraSpace(float sItemTextSelectedExtraSpace) {
        mItemTextSelectedExtraSpace = sItemTextSelectedExtraSpace;
        requestLayout();
        invalidate();
    }

    /**
     * Returns whether the item text is bold.
     */
    public boolean getItemTextBold() {
        return mItemTextBold;
    }

    /**
     * Enables or disables bold text for item labels.
     *
     * @param sItemTextBold True to enable bold
     */
    public void setItemTextBold(boolean sItemTextBold) {
        mItemTextBold = sItemTextBold;
        mTextPaint.setFakeBoldText(sItemTextBold);
        invalidate();
    }

    /**
     * Returns whether the item text is displayed in all caps.
     */
    public boolean getItemTextAllCaps() {
        return mItemTextAllCaps;
    }

    /**
     * Enables or disables all caps style for item text.
     *
     * @param sItemTextAllCaps True to enable all caps
     */
    public void setItemTextAllCaps(boolean sItemTextAllCaps) {
        mItemTextAllCaps = sItemTextAllCaps;
    }

    /**
     * Gets the currently selected font for item text.
     */
    public Typeface getItemTextChooseFont() {
        return mItemTextChooseFont;
    }

    /**
     * Sets the font used for item text from a font resource.
     *
     * @param sItemTextChooseFont Font resource ID
     */
    public void setItemTextChooseFont(@FontRes int sItemTextChooseFont) {
        mItemTextChooseFontResourcesID = sItemTextChooseFont;
        try {
            mItemTextChooseFont = ResourcesCompat.getFont(getContext(), sItemTextChooseFont);
            if (mTextPaint != null && mItemTextChooseFont != null) {
                mTextPaint.setTypeface(mItemTextChooseFont);
            }
        } catch (Exception mException) {
            Log.w("Navify", "Invalid font source for Item Text: " + mException.getMessage());
        }
        invalidate();
    }

    /**
     * Sets the label visibility mode for navigation items.
     *
     * @param sItemLabelVisibilityMode Mode constant: NAVIFY_SELECTED, NAVIFY_LABELED, NAVIFY_UNLABELED
     */
    public void setItemLabelVisibilityMode(int sItemLabelVisibilityMode) {
        if (sItemLabelVisibilityMode < NAVIFY_SELECTED || sItemLabelVisibilityMode > NAVIFY_UNLABELED)
            return;
        mItemLabelVisibilityMode = sItemLabelVisibilityMode;
        invalidate();
    }

    /**
     * Gets the current item label visibility mode.
     *
     * @return Mode constant
     */
    public int getItemLabelVisibilityMode() {
        return mItemLabelVisibilityMode;
    }

    /**
     * Gets the currently selected item's index.
     *
     * @return Index of selected item
     */
    public int getItemSelectedIndex() {
        return mItemSelectedIndex;
    }

    /**
     * Sets the selected item's index with animation.
     *
     * @param mItemSelectedIndex New selected index
     */
    public void setItemSelectedIndex(int mItemSelectedIndex) {
        if (mItemSelectedIndex < 0 || mItemSelectedIndex >= mNavifyItemCount) return;
        for (int mI = 0; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemTextAlphas[mI] = 0f;
            mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
        }
        AnimationSelectionToIndex(mItemSelectedIndex);
    }

    /**
     * Gets the currently assigned menu resource ID.
     *
     * @return Menu resource ID
     */
    public int getMenuResource() {
        return mMenuResource;
    }

    /**
     * Sets the menu from a resource and applies it to the view.
     *
     * @param mMenuResource Resource ID of the menu
     */
    public void setMenuResource(int mMenuResource) {
        if (mMenuResource == 0) {
            throw new IllegalArgumentException("Menu resource ID cannot be 0. Please provide a valid menu resource.");
        }

        if (mNavifyItemCount > 0 && mNavifyItemIcon[0] != null) {
            throw new IllegalStateException("A menu item was added manually. Please clear the manual items with clearItems() first and then call setMenu().");
        }

        PopupMenu mPopupMenu = new PopupMenu(getContext(), this);
        mPopupMenu.getMenuInflater().inflate(mMenuResource, mPopupMenu.getMenu());
        android.view.Menu mMenu = mPopupMenu.getMenu();

        int mRawCount = mMenu.size();
        if (mRawCount == 0) {
            throw new IllegalStateException("There are no items in the menu source. At least one item is required.");
        }

        List<Drawable> mValidIcons = new ArrayList<>();
        List<String> mValidTitles = new ArrayList<>();

        for (int mI = 0; mI < mRawCount; mI++) {
            MenuItem mItem = mMenu.getItem(mI);
            Drawable mIcon = mItem.getIcon();
            CharSequence mTitle = mItem.getTitle();
            if (mIcon == null || mTitle == null || mTitle.toString().trim().isEmpty()) {
                Log.w("Navify", "menu item " + mI + " invalid. Skipping.");
                continue;
            }
            mValidIcons.add(mIcon);
            mValidTitles.add(mTitle.toString());
        }

        int mCount = mValidIcons.size();
        if (mCount == 0) {
            throw new IllegalStateException("No valid menu items found. Please add at least one item with icon+title.");
        }
        if (mCount > NAVIFY_MAX_ITEMS_SIZE) {
            throw new IllegalStateException("Too many valid items in menu source (" + mCount + "). Maximum allowed value " + NAVIFY_MAX_ITEMS_SIZE);
        }

        mNavifyItemCount = mCount;
        if (mItemSelectedIndex < 0 || mItemSelectedIndex >= mCount) {
            mItemSelectedIndex = 0;
        }

        for (int mI = 0; mI < mCount; mI++) {
            mNavifyItemIcon[mI] = mValidIcons.get(mI);
            mNavifyItemTitle[mI] = mValidTitles.get(mI);
            if (mI == mItemSelectedIndex) {
                mNavifyItemIconSize[mI] = mItemSelectedIconSize;
                mNavifyItemTextAlphas[mI] = 1f;
                mNavifyTextOffsets[mI] = 0f;
            } else {
                mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
                mNavifyItemTextAlphas[mI] = 0f;
                mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
            }
        }

        for (int mI = mCount; mI < NAVIFY_MAX_ITEMS_SIZE; mI++) {
            mNavifyItemIcon[mI] = null;
            mNavifyItemTitle[mI] = null;
            mNavifyItemIconSize[mI] = mItemUnSelectedIconSize;
            mNavifyItemTextAlphas[mI] = 0f;
            mNavifyTextOffsets[mI] = mNavifyItemTextAnimDistancePX;
        }

        mAnimCenterX = ComputeCenterXForIndex(mItemSelectedIndex);
        requestLayout();
        invalidate();
    }

    /**
     * Gets whether haptic feedback is enabled.
     *
     * @return True if enabled, false otherwise
     */
    public boolean getHapticEnabled() {
        return mHapticEnabled;
    }

    /**
     * Sets whether haptic feedback is enabled.
     *
     * @param sHapticEnabled True to enable
     */
    public void setHapticEnabled(boolean sHapticEnabled) {
        mHapticEnabled = sHapticEnabled;
    }

    /**
     * Checks if RTL layout direction is being enforced.
     *
     * @return True if RTL is forced
     */
    private boolean getForceRTL() {
        if (mForceRTL != null) {
            return mForceRTL;
        }
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    /**
     * Forces layout direction to RTL or resets to default.
     *
     * @param sForceRTL True to force RTL, false to follow system default
     */
    public void setForceRTL(Boolean sForceRTL) {
        mForceRTL = sForceRTL;
        invalidate();
    }


    /**
     * Enables or disables selection interaction for items.
     *
     * @param sDisableSelectedItem True to disable selection
     */
    public void setDisableSelectedItem(boolean sDisableSelectedItem) {
        mDisableSelectedItem = sDisableSelectedItem;
        invalidate();
    }

    /**
     * Configures whether the decor view should fit system windows (for edge-to-edge layout).
     *
     * @param mWindow  The target window
     * @param mEnabled True to enable decor fitting system windows
     */
    public void setDecorFitsSystemWindows(Window mWindow, boolean mEnabled) {
        WindowCompat.setDecorFitsSystemWindows(mWindow, mEnabled);
    }

    /**
     * Returns whether item selection is currently disabled.
     *
     * @return True if disabled
     */
    public boolean getDisableSelectedItem() {
        return mDisableSelectedItem;
    }

    /**
     * Sets badge behavior on selection.
     *
     * @param sSkipOnSelected     True to hide badge on selected item
     * @param sAutoRemoveOnSelect True to automatically remove badge after selection
     */
    public void setBadgeBehaviorListener(boolean sSkipOnSelected, boolean sAutoRemoveOnSelect) {
        mSkipBadgeOnSelected = sSkipOnSelected;
        mAutoRemoveBadgeOnSelect = sAutoRemoveOnSelect;
        invalidate();
    }

    /**
     * Internal logic to show badge with animation.
     *
     * @param mIndex      Index of item
     * @param mBadgeCount Badge count
     */
    private void InternalBadgeController(int mIndex, int mBadgeCount) {
        if (!mItemBadge) return;
        if (mIndex < 0 || mIndex >= mNavifyItemCount) return;

        mBadgeCount = Math.max(0, Math.min(mBadgeCount, mItemBadgeMaxCount));

        mBadgeEnabled[mIndex] = mBadgeCount > 0;
        mBadgeText[mIndex] = mBadgeCount == 0 ? "" : (mBadgeCount == mItemBadgeMaxCount ? (mItemBadgeMaxCount + "+") : String.valueOf(mBadgeCount));

        if (mItemBadgeAnimation && mBadgeCount > 0) {
            mBadgeScale[mIndex] = 0f;
            ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
            mValueAnimator.setDuration(200);
            mValueAnimator.addUpdateListener(a -> {
                mBadgeScale[mIndex] = (float) a.getAnimatedValue();
                invalidate();
            });
            mValueAnimator.start();
        }
        invalidate();
    }

    /**
     * Adds a badge with given count.
     *
     * @param mIndex      Item index
     * @param mBadgeCount Badge count
     */
    public void setAddBadge(int mIndex, int mBadgeCount) {
        InternalBadgeController(mIndex, mBadgeCount);
    }

    /**
     * Adds a badge with count, optionally showing zero.
     *
     * @param mIndex      Item index
     * @param mBadgeCount Badge count
     * @param mShowZero   True to show badge even if count is zero
     */
    public void setAddBadge(int mIndex, int mBadgeCount, boolean mShowZero) {
        if (!mItemBadge) return;
        if (mIndex < 0 || mIndex >= mNavifyItemCount) return;

        mBadgeCount = Math.max(0, Math.min(mBadgeCount, mItemBadgeMaxCount));

        if (mBadgeCount == 0 && mShowZero) {
            mBadgeEnabled[mIndex] = true;
            mBadgeText[mIndex] = "0";
            invalidate();
        } else {
            InternalBadgeController(mIndex, mBadgeCount);
        }
    }

    /**
     * Removes badge from a specific item.
     *
     * @param mIndex Index of the item
     */
    public void setRemoveBadge(int mIndex) {
        if (mIndex < 0 || mIndex >= mNavifyItemCount) return;
        mBadgeEnabled[mIndex] = false;
        mBadgeText[mIndex] = "";
        mBadgeScale[mIndex] = 1f;
        invalidate();
    }

    /**
     * Returns whether badge system is enabled.
     *
     * @return True if badge is enabled
     */
    public boolean getItemBadge() {
        return mItemBadge;
    }

    /**
     * Enables or disables the badge system.
     *
     * @param sItemBadge True to enable badge
     */
    public void setItemBadge(boolean sItemBadge) {
        mItemBadge = sItemBadge;
        invalidate();
    }

    /**
     * Gets badge background color.
     *
     * @return Color int
     */
    public int getItemBadgeBackgroundColor() {
        return mItemBadgeBackgroundColor;
    }

    /**
     * Sets badge background color.
     *
     * @param sItemColor Color int
     */
    public void setItemBadgeBackgroundColor(int sItemColor) {
        mItemBadgeBackgroundColor = sItemColor;
        if (mBadgeBackgroundPaint != null) {
            mBadgeBackgroundPaint.setColor(sItemColor);
        }
        invalidate();
    }

    /**
     * Returns the max count allowed for badge.
     *
     * @return Max count
     */
    public int getItemBadgeMaxCount() {
        return mItemBadgeMaxCount;
    }

    /**
     * Sets the max badge count, clamped between 0-99.
     *
     * @param sItemMaxCount Maximum count
     */
    public void setItemBadgeMaxCount(int sItemMaxCount) {
        sItemMaxCount = Math.max(0, Math.min(sItemMaxCount, 99));
        mItemBadgeMaxCount = sItemMaxCount;
        if (mItemBadgeSelectedCount >= 0) {
            setItemBadgeSelectedCount(mItemBadgeSelectedCount);
        }
        invalidate();
    }

    /**
     * Returns whether badge animation is enabled.
     *
     * @return True if enabled
     */
    public boolean getItemBadgeAnimation() {
        return mItemBadgeAnimation;
    }

    /**
     * Enables or disables badge animation.
     *
     * @param sItemBadgeAnimation True to enable
     */
    public void setItemBadgeAnimation(boolean sItemBadgeAnimation) {
        mItemBadgeAnimation = sItemBadgeAnimation;
    }

    /**
     * Gets the index of item with badge by default.
     *
     * @return Index
     */
    public int getItemBadgeIndex() {
        return mItemBadgeIndex;
    }

    /**
     * Sets the index of the item to show badge initially.
     *
     * @param sItemBadgeIndex Index value (-1 to disable)
     */
    public void setItemBadgeIndex(int sItemBadgeIndex) {
        if (sItemBadgeIndex < -1 || sItemBadgeIndex >= NAVIFY_MAX_ITEMS_SIZE) return;
        mItemBadgeIndex = sItemBadgeIndex;
        invalidate();
    }

    /**
     * Gets the position of badge (start or end).
     *
     * @return Constant: BADGE_POSITION_START or BADGE_POSITION_END
     */
    public int getItemBadgePosition() {
        return mItemBadgePosition;
    }

    /**
     * Sets the badge position relative to the icon.
     *
     * @param sItemBadgePosition Position constant
     */
    public void setItemBadgePosition(int sItemBadgePosition) {
        if (sItemBadgePosition == BADGE_POSITION_START || sItemBadgePosition == BADGE_POSITION_END) {
            mItemBadgePosition = sItemBadgePosition;
            invalidate();
        }
    }

    /**
     * Gets the badge count assigned to the selected item.
     *
     * @return Badge count
     */
    public int getItemBadgeSelectedCount() {
        return mItemBadgeSelectedCount;
    }


    /**
     * Sets the badge count for the selected item.
     * Limits the count to the configured max and updates the badge display accordingly.
     */
    public void setItemBadgeSelectedCount(int sItemBadgeSelectedCount) {
        sItemBadgeSelectedCount = Math.max(0, Math.min(sItemBadgeSelectedCount, mItemBadgeMaxCount));
        mItemBadgeSelectedCount = sItemBadgeSelectedCount;

        String mTXT = (sItemBadgeSelectedCount == 0 ? "" : sItemBadgeSelectedCount == mItemBadgeMaxCount ? (mItemBadgeMaxCount + "+") : String.valueOf(sItemBadgeSelectedCount));
        if (mItemBadgeIndex >= 0 && mItemBadgeIndex < NAVIFY_MAX_ITEMS_SIZE) {
            mBadgeText[mItemBadgeIndex] = mTXT;
            mBadgeEnabled[mItemBadgeIndex] = sItemBadgeSelectedCount > 0;
        }
        invalidate();
    }

    /**
     * Returns the text color used for the selected badge.
     */
    public int getItemBadgeSelectedTextColor() {
        return mItemBadgeSelectedTextColor;
    }

    /**
     * Sets the text color of the selected badge.
     */
    public void setItemBadgeSelectedTextColor(int sItemBadgeSelectedTextColor) {
        mItemBadgeSelectedTextColor = sItemBadgeSelectedTextColor;
        if (mBadgeTextPaint != null) {
            mBadgeTextPaint.setColor(sItemBadgeSelectedTextColor);
        }
        invalidate();
    }

    /**
     * Returns the size of the badge text (in sp units).
     */
    public float getItemBadgeSelectedTextSize() {
        return mItemBadgeSelectedTextSize;
    }

    /**
     * Sets the text size of the selected badge.
     */
    public void setItemBadgeSelectedTextSize(float sItemBadgeSelectedTextSize) {
        mItemBadgeSelectedTextSize = sItemBadgeSelectedTextSize;
        if (mBadgeTextPaint != null) {
            float px = sItemBadgeSelectedTextSize * getResources().getDisplayMetrics().scaledDensity;
            mBadgeTextPaint.setTextSize(px);
        }
        requestLayout();
        invalidate();
    }

    /**
     * Returns the resource ID of the selected badge text font.
     */
    public int getItemBadgeSelectedTextChooseFont() {
        return mItemBadgeSelectedTextChooseFont;
    }

    /**
     * Sets a custom font for the selected badge text using a font resource ID.
     */
    public void setItemBadgeSelectedTextChooseFont(@FontRes int sItemBadgeSelectedTextChooseFont) {
        mItemBadgeSelectedTextChooseFont = sItemBadgeSelectedTextChooseFont;
        try {
            mBadgeChooseFont = ResourcesCompat.getFont(getContext(), sItemBadgeSelectedTextChooseFont);
            if (mBadgeTextPaint != null && mBadgeChooseFont != null) {
                mBadgeTextPaint.setTypeface(mBadgeChooseFont);
            }
        } catch (Exception mException) {
            Log.w("Navify", "Invalid font source for Badge: " + mException.getMessage());
        }
        invalidate();
    }

    /**
     * Resets the badge count of a specific item to a given count.
     */
    public void setResetBadgeIndex(int index, int count) {
        setAddBadge(index, count, true);
    }

    /**
     * Sets the listener for long-press selection on an item.
     */
    public void setLongSelectedItemListener(INavifyLongSelectedItemListener sLongSelectedListener) {
        mLongSelectedItemListener = sLongSelectedListener;
    }

    /**
     * Sets the listener for double-tap selection on an item.
     */
    public void setDoubleTapSelectedItemListener(INavifyDoubleTapSelectedItemListener sDoubleTapSelectedListener) {
        mDoubleTapSelectedItemListener = sDoubleTapSelectedListener;
    }

    /**
     * Sets the listener for normal (single-tap) item selection.
     */
    public void setNavifyNormalSelectedItemListener(INavifyNormalSelectedItemListener sNavifyNormalSelectedItemListener) {
        mNavifyNormalSelectedItemListener = sNavifyNormalSelectedItemListener;
    }

    /**
     * Configures the badge behavior when an item is selected.
     * - sSkipOnSelected: whether to skip badge rendering for selected item.
     * - sAutoRemoveOnSelect: whether to automatically remove the badge when selected.
     * - sNavifyBadgeResetListener: callback when a badge is reset/removed.
     */
    public void setBadgeBehaviorListener(boolean sSkipOnSelected, boolean sAutoRemoveOnSelect, @Nullable INavifyBadgeResetListener sNavifyBadgeResetListener) {
        mSkipBadgeOnSelected = sSkipOnSelected;
        mAutoRemoveBadgeOnSelect = sAutoRemoveOnSelect;
        mBadgeResetListener = sNavifyBadgeResetListener;
        invalidate();
    }

    /**
     * Sets the background color for the badge popup window.
     */
    public void setBadgePopupWindowBackgroundColor(int sBadgePopupWindowBackgroundColor) {
        mBadgePopupWindowBackgroundColor = sBadgePopupWindowBackgroundColor;
    }

    /**
     * Returns the background color of the badge popup window.
     */
    public int getBadgePopupWindowBackgroundColor() {
        return mBadgePopupWindowBackgroundColor;
    }

    /**
     * Sets the fade-in animation duration for the badge popup window.
     */
    public void setBadgePopupWindowFadeInAnimationDuration(long sBadgePopupWindowFadeInAnimationDuration) {
        mBadgePopupWindowFadeInAnimationDuration = sBadgePopupWindowFadeInAnimationDuration;
    }

    /**
     * Returns the fade-in duration of the badge popup window.
     */
    public long getBadgePopupWindowFadeInAnimationDuration() {
        return mBadgePopupWindowFadeInAnimationDuration;
    }

    /**
     * Sets the fade-out animation duration for the badge popup window.
     */
    public void setBadgePopupWindowFadeOutAnimationDuration(long sBadgePopupWindowFadeOutAnimationDuration) {
        mBadgePopupWindowFadeOutAnimationDuration = sBadgePopupWindowFadeOutAnimationDuration;
    }

    /**
     * Returns the fade-out duration of the badge popup window.
     */
    public long getBadgePopupWindowFadeOutAnimationDuration() {
        return mBadgePopupWindowFadeOutAnimationDuration;
    }

    /**
     * Sets how long the popup stays visible before hiding.
     */
    public void setBadgePopupWindowShowAnimationDuration(long sBadgePopupWindowShowAnimationDuration) {
        mBadgePopupWindowShowAnimationDuration = sBadgePopupWindowShowAnimationDuration;
    }

    /**
     * Returns the total duration the badge popup remains shown.
     */
    public long getBadgePopupWindowShowAnimationDuration() {
        return mBadgePopupWindowShowAnimationDuration;
    }

    /**
     * Sets the vertical offset (in dp) for the popup window.
     */
    public void setBadgePopupWindowOffsetY(float sBadgePopupWindowOffsetY) {
        mBadgePopupWindowOffsetY = dpToPx(sBadgePopupWindowOffsetY);
    }

    /**
     * Gets the vertical offset of the popup window in dp.
     */
    public float getBadgePopupWindowOffsetY() {
        return mBadgePopupWindowOffsetY / getResources().getDisplayMetrics().density;
    }

    /**
     * Sets the text shown when a new badge is triggered.
     */
    public void setBadgePopupWindowNewText(String sBadgePopupWindowText) {
        mBadgePopupWindowNewText = sBadgePopupWindowText;
    }

    /**
     * Gets the configured "new" badge popup text.
     */
    public String getBadgePopupWindowNewText() {
        return mBadgePopupWindowNewText;
    }

    /**
     * Gets the current text displayed in the popup window.
     */
    public String getBadgePopupWindowNowText() {
        return mBadgePopupWindowNowText;
    }

    /**
     * Updates the current text shown in the badge popup.
     */
    public void setBadgePopupWindowNowText(String sBadgePopupWindowNowText) {
        mBadgePopupWindowNowText = sBadgePopupWindowNowText;
    }

    /**
     * Sets the text color of the badge popup window.
     */
    public void setBadgePopupWindowTextColor(int sBadgePopupWindowTextColor) {
        mBadgePopupWindowTextColor = sBadgePopupWindowTextColor;
    }

    /**
     * Gets the text color of the badge popup window.
     */
    public int getBadgePopupWindowTextColor() {
        return mBadgePopupWindowTextColor;
    }

    /**
     * Sets the text size of the badge popup window (sp).
     */
    public void setBadgePopupWindowTextSize(float sBadgePopupWindowTextSize) {
        mBadgePopupWindowTextSize = sBadgePopupWindowTextSize;
    }

    /**
     * Gets the text size of the badge popup window.
     */
    public float getBadgePopupWindowTextSize() {
        return mBadgePopupWindowTextSize;
    }

    /**
     * Sets whether the badge popup text should be displayed in ALL CAPS.
     */
    public void setBadgePopupWindowTextAllCaps(boolean sBadgePopupWindowTextAllCaps) {
        mBadgePopupWindowTextAllCaps = sBadgePopupWindowTextAllCaps;
    }

    /**
     * Returns whether the popup text is displayed in ALL CAPS.
     */
    public boolean getBadgePopupWindowTextAllCaps() {
        return mBadgePopupWindowTextAllCaps;
    }


    /**
     * Sets the custom font for the badge popup text.
     *
     * @param sBadgePopupWindowTextChooseFont The font resource ID to be applied.
     */
    public void setBadgePopupWindowTextChooseFont(@FontRes int sBadgePopupWindowTextChooseFont) {
        try {
            Typeface mTypeFace = ResourcesCompat.getFont(getContext(), sBadgePopupWindowTextChooseFont);
            if (mTypeFace != null) mBadgePopupWindowTextChooseFont = mTypeFace;
        } catch (Exception e) {
            Log.w("Navify", "Invalid font source for Popup: " + e.getMessage());
        }
    }

    /**
     * Gets the current typeface used for the badge popup window text.
     *
     * @return The selected Typeface or null.
     */
    @Nullable
    public Typeface getBadgePopupWindowTextChooseFont() {
        return mBadgePopupWindowTextChooseFont;
    }

    /**
     * Shows a temporary popup window above the badge displaying its value or description.
     *
     * @param mIndex The index of the item for which the badge popup will be shown.
     */
    private void BadgePopupWindow(int mIndex) {
        if (!mBadgeEnabled[mIndex] || mBadgeText[mIndex].isEmpty()) return;

        String mDefaultText = "0".equals(mBadgeText[mIndex]) ? mBadgePopupWindowNowText : mBadgeText[mIndex] + " " + mBadgePopupWindowNewText;

        if (mBadgePopupWindowTextAllCaps) {
            mDefaultText = mDefaultText.toUpperCase(Locale.getDefault());
        }

        // Build the popup text view
        TextView mPopupVindowTextView = new TextView(getContext());
        mPopupVindowTextView.setText(mDefaultText);
        mPopupVindowTextView.setAllCaps(mBadgePopupWindowTextAllCaps);
        mPopupVindowTextView.setTextColor(mBadgePopupWindowTextColor);
        mPopupVindowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBadgePopupWindowTextSize);
        mPopupVindowTextView.setTypeface(mBadgePopupWindowTextChooseFont);
        int mPadHeight = (int) dpToPx(8f), mPadWeight = (int) dpToPx(4f);
        mPopupVindowTextView.setPadding(mPadHeight, mPadWeight, mPadHeight, mPadWeight);

        // Background
        GradientDrawable mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(mBadgePopupWindowBackgroundColor);
        mGradientDrawable.setCornerRadius(dpToPx(4f));
        mPopupVindowTextView.setBackground(mGradientDrawable);
        mPopupVindowTextView.setAlpha(0f); // Start transparent

        // Create and show popup window
        PopupWindow mPopupWindow = new PopupWindow(mPopupVindowTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setElevation(dpToPx(4f));
        mBadgePopupWindow = mPopupWindow;

        // Positioning calculations
        int[] mLocationOnScreen = new int[2];
        getLocationOnScreen(mLocationOnScreen);

        float mSectionWeight = mViewWidth / (float) mNavifyItemCount;
        int mDrawIDX = RTLIndex(mIndex);
        float mCenterX = mSectionWeight * mDrawIDX + mSectionWeight / 2f;

        mPopupVindowTextView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int mPopupWindowWeight = mPopupVindowTextView.getMeasuredWidth();
        int mPH = mPopupVindowTextView.getMeasuredHeight();

        int mX = mLocationOnScreen[0] + (int) (mCenterX - mPopupWindowWeight / 2f);
        int mScreenWeight = getResources().getDisplayMetrics().widthPixels;
        int mMargin = (int) dpToPx(4f);
        mX = Math.max(mMargin, Math.min(mX, mScreenWeight - mPopupWindowWeight - mMargin));

        float mBadgeR = dpToPx(8f) * mBadgeScale[mIndex];
        float mIconTopY = (mItemSelectedIndex == mIndex) ? (mCircleCenterY - mNavifyItemIconSize[mIndex] / 2f) : (mBarTopY + (mBarHeight - mNavifyItemIconSize[mIndex]) / 2f);
        float mBY = mIconTopY - mBadgeR * 0.3f;
        int mY = mLocationOnScreen[1] + (int) (mBY - mPH - mBadgePopupWindowOffsetY);

        mPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, mX, mY);

        // Show animation
        mPopupVindowTextView.animate().alpha(1f).setDuration(mBadgePopupWindowFadeInAnimationDuration).start();

        // Hide after delay
        mBadgePopupWindowHandler.postDelayed(() -> mPopupVindowTextView.animate().alpha(0f).setDuration(mBadgePopupWindowFadeOutAnimationDuration).withEndAction(() -> {
            if (mBadgePopupWindow != null && mBadgePopupWindow.isShowing()) {
                mBadgePopupWindow.dismiss();
            }
        }).start(), mBadgePopupWindowShowAnimationDuration);
    }

    /**
     * Lifecycle method triggered when the view is attached to a window.
     * Automatically adjusts system window insets to ensure layout behaves correctly with navigation bars.
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (getContext() instanceof Activity) {
            Window mWindow = ((Activity) getContext()).getWindow();
            WindowCompat.setDecorFitsSystemWindows(mWindow, false);
        }

        ViewCompat.setOnApplyWindowInsetsListener(this, (mView, mInsets) -> {
            int mBottom = mInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            ViewGroup.MarginLayoutParams mViewGroup = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
            mViewGroup.bottomMargin = mBottom;
            mView.setLayoutParams(mViewGroup);
            return WindowInsetsCompat.CONSUMED;
        });

        ViewCompat.requestApplyInsets(this);
    }

    /**
     * Lifecycle method triggered when the view is detached from the window.
     * No-op currently, but useful for cleanup if needed in future.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

