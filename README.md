## Navify BottomNavigationView 
### 🍥 makes classic bottom menus a thing of the past! It brings life to modern applications with its animated interactions, focused icons and fully customizable structure.

[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat)](https://www.android.com/)
[![](https://jitpack.io/v/b3ddodev/Navify.svg)](https://jitpack.io/#b3ddodev/Navify)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)
[![Instagram Badge](https://img.shields.io/badge/Instagram-%20%40berdan.bdy-brightgreen)](https://www.instagram.com/berdan.bdy)


### Buy Me a Coffee <3
<img src="https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExMG9zaGl2Mjd2cHlydWc4enBqNHkzajJ3NnpydHN4YmNwMnltemg3ayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/7CjEqtZUm2OBmJ9eha/giphy.gif" width="300"/>

[![Buy Me a Coffee](https://img.buymeacoffee.com/button-api/?text=Buy+me+a+coffee&emoji=&slug=withb3ddodo&button_colour=FF5F5F&font_colour=ffffff&font_family=Poppins&outline_colour=000000&coffee_colour=FFDD00)](https://www.buymeacoffee.com/withb3ddodo)



### Jitpack

Add the JitPack repository to your `settings.gradle` file
```groovy
allprojects {
	repositories {
		...
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```
### Dependency
Add the dependency in your `build.gradle`

```groovy
dependencies {
    implementation 'com.github.b3ddodev:Navify:{release-version}'
}
```

### 🧩 Demo

<img src="https://github.com/b3ddodev/Navify/blob/master/GIF.gif" width="300"/>

---

### Using the library
Default XML Sample :
```
    <com.navify.Navify
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:mBackgroundColor="#FFFFFF"
        app:mBackgroundElevation="10dp"
        app:mItemCircleBackgroundColor="#F4511E"
        app:mItemCircleBackgroundElevation="10dp"
        app:mItemCircleAnimationDuration="300"
        app:mItemCircleAnimationInterpolator="NAVIFY_FAST_OUT_SLOW_IN"
        app:mItemSelectedIconColor="#FFFFFF"
        app:mItemSelectedIconSize="36dp"
        app:mItemUnSelectedIconColor="#CCF4511E"
        app:mItemUnSelectedIconSize="24dp"
        app:mItemTextSelectedColor="#000000"
        app:mItemTextSelectedSize="14sp"
        app:mItemTextUnSelectedColor="#000000"
        app:mItemTextUnSelectedSize="7sp"
        app:mItemTextSelectedExtraSpace="5dp"
        app:mItemTextBold="true"
        app:mItemTextAllCaps="true"
        app:mItemTextChooseFont="@font/poppins"
        app:mItemLabelVisibilityMode="1"
        app:mItemSelectedIndex="2"
        app:mMenuResource="@menu/menu"
        app:mHapticEnabled="true"
        app:mForceRTL="false" />
```
Default Programatic Sample :
```
        Navify mNavify = findViewById(R.id.mNavify)

        mNavify.setMenuResource(R.menu.menu);
        mNavify.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mNavify.setBackgroundElevation(10f);

        mNavify.setItemCircleBackgroundColor(Color.parseColor("#F4511E"));
        mNavify.setItemCircleBackgroundElevation(10);
        mNavify.setItemCircleAnimationDuration(300);
        mNavify.setItemCircleAnimationInterpolator(Navify.FAST_OUT_SLOW_IN);

        mNavify.setItemSelectedIconColor(Color.parseColor("#FFFFFF"));
        mNavify.setItemSelectedIconSize(36f);

        mNavify.setItemUnSelectedIconColor(Color.parseColor("#CCF4511E"));
        mNavify.setItemUnSelectedIconSize(24f);

        mNavify.setItemSelectedTextColor(Color.parseColor("#000000"));
        mNavify.setItemSelectedTextSize(14f);

        mNavify.setItemUnSelectedTextColor(Color.parseColor("#000000"));
        mNavify.setItemUnSelectedTextSize(7f);

        mNavify.setItemTextSelectedExtraSpace(5);
        mNavify.setItemTextBold(true);
        mNavify.setItemTextAllCaps(true);
        mNavify.setItemTextChooseFont(ResourcesCompat.getFont(this, R.font.poppins));

        mNavify.setItemLabelVisibilityMode(1); 
        mNavify.setItemSelectedIndex(2);

        mNavify.setHapticEnabled(true);
        mNavify.setForceRTL(false);
```
Manuel Add Items :
```
mNavify.ClearReloadItems();
mNavify.setAddItem(ContextCompat.getDrawable(this,R.drawable.ic_preview_home),"Home");
mNavify.setAddItem(ContextCompat.getDrawable(this,R.drawable.ic_preview_search),"Search");
mNavify.setAddItem(ContextCompat.getDrawable(this,R.drawable.ic_preview_reaction),"Reaction");
mNavify.setAddItem(ContextCompat.getDrawable(this,R.drawable.ic_preview_favorite),"Favorite");
mNavify.setAddItem(ContextCompat.getDrawable(this,R.drawable.ic_preview_fire),"Fire");
(MAX 5 ITEMS)
```
Listener :
```
        mNavify.setNavifyNormalSelectedItemListener(new INavifyNormalSelectedItemListener() {
            @Override
            public void onNavifyNormalSelectedItem(int mIndex) {
                switch (mIndex){
                    case 0:
                        Toast.makeText(MainActivity.this, "HOME = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "SEARCH = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "FIRE = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
```
      mNavify.setLongSelectedItemListener(new INavifyLongSelectedItemListener() {
            @Override
            public void onNavifyLongSelectedItem(int mIndex) {
                switch (mIndex) {
                    case 0:
                        Toast.makeText(MainActivity.this, "HOME = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "SEARCH = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "FIRE = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
```
      mNavify.setDoubleTapSelectedItemListener(new INavifyDoubleTapSelectedItemListener() {
            @Override
            public void onNavifyDoubleTapSelectedItem(int mIndex) {
                switch (mIndex) {
                    case 0:
                        Toast.makeText(MainActivity.this, "HOME = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "SEARCH = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "REACTION = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "FIRE = INDEX : " + mIndex, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
```
### Badge Controller  
      mNavify.setItemBadge(true);
        mNavify.setItemBadgeBackgroundColor(Color.parseColor("#FF0000"));
        mNavify.setItemBadgeMaxCount(99);
        mNavify.setItemBadgeAnimation(true);
        mNavify.setItemBadgeIndex(3);
        mNavify.setItemBadgePosition(Navify.BADGE_POSITION_END);
        mNavify.setItemBadgeSelectedTextColor(Color.WHITE);
        mNavify.setItemBadgeSelectedTextSize(8f);

        mNavify.setAddBadge(3, 99, true);

        mNavify.setBadgeBehaviorListener(false, true, new INavifyBadgeResetListener() {
            @Override
            public void onNavifyBadgeReset(int mIndex) {
                mNavify.setResetBadgeIndex(3, 0);
            }
        });        


### Nav Animation Type  
#### Nav animations you can adjust: `NavifyInterpolator`

Type | Description  
--------- | -----------  
`𝙇𝙄𝙉𝙀𝘼𝙍` | The circle moves at a constant speed with no acceleration or bounce — a straight, robotic transition.  
`𝘼𝘾𝘾𝙀𝙇𝙀𝙍𝘼𝙏𝙀` | The circle starts slowly and gains speed toward the end — a fast landing effect.  
`𝘿𝙀𝘾𝙀𝙇𝙀𝙍𝘼𝙏𝙀` | The circle starts fast and slows down smoothly as it reaches its target — a soft, graceful stop.  
`𝘼𝘾𝘾𝙀𝙇𝙀𝙍𝘼𝙏𝙀_𝘿𝙀𝘾𝙀𝙇𝙀𝙍𝘼𝙏𝙀` | The circle eases in and out — starting slow, speeding up, and slowing down again in a fluid motion.  
`𝙊𝙑𝙀𝙍𝙎𝙃𝙊𝙊𝙏` | The circle overshoots its target slightly and bounces back — creating a dynamic snap effect.  
`𝘽𝙊𝙐𝙉𝘾𝙀` | The circle lands with a bounce, like it's made of rubber — playful and energetic.  
`𝙉𝙊𝙉𝙀` | No animation is applied. The circle instantly jumps to its new position with no transition.  
`𝘼𝙉𝙏𝙄𝘾𝙄𝙋𝘼𝙏𝙀` | The circle slightly moves backward before quickly moving forward — giving a "wind-up" effect.  
`𝘼𝙉𝙏𝙄𝘾𝙄𝙋𝘼𝙏𝙀_𝙊𝙑𝙀𝙍𝙎𝙃𝙊𝙊𝙏` | The circle pulls back, shoots past the target, and settles in — dramatic and expressive.  
`𝙁𝘼𝙎𝙏_𝙊𝙐𝙏_𝙎𝙇𝙊𝙒_𝙄𝙉` | The circle enters quickly and slows down smoothly — a modern, natural-feeling transition.



## License

```
The MIT License (MIT)

Copyright (c) 2025 b3ddo dEV' (github.com/b3ddodev)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```



