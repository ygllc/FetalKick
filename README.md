---
description: >-
  A simple Android app to track fetal kicks after each meal during pregnancy
  using a daily calendar, numeric counters and optional stopwatch tracking.
icon: baby
coverY: 0
layout:
  width: wide
  cover:
    visible: true
    size: full
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
  metadata:
    visible: true
  tags:
    visible: true
---

# Fetal Kick

**Track your baby’s kicks simply and beautifully.**

Fetal Kick helps you monitor your baby’s movements throughout the day. Just tap to count each kick during your daily sessions — no fuss, no complicated setup.

### How It Works

{% stepper %}
{% step %}
### Open the app

You’ll see the home screen with a “New Session” floating action button.
{% endstep %}

{% step %}
### Tap “New Session”

The app enters setup mode with meal and date selection enabled.
{% endstep %}

{% step %}
### Choose your meal

Select Breakfast, Lunch, Snacks, or Dinner.
{% endstep %}

{% step %}
### Pick a date

Use the date picker, then confirm with “OK”.
{% endstep %}

{% step %}
### Start counting

The app enters counting mode with your selected meal and date shown. You can:

* Tap the big button to count kicks (with or without the timer)
* Start, pause, or reset the timer
{% endstep %}

{% step %}
### Exit the session

Tap “Exit”. If you recorded any kicks, the session saves automatically. If the count is zero, the session is discarded.
{% endstep %}
{% endstepper %}

### Features

| Feature                 | What It Does                                    |
| ----------------------- | ----------------------------------------------- |
| **Meal-based tracking** | Log kicks that fit your routine                 |
| **Timed sessions**      | Set a duration and track kicks in real-time     |
| **Home screen widget**  | See today’s total at a glance                   |
| **History**             | Browse and manage past sessions                 |
| **Export your data**    | Download everything as a CSV file               |
| **Customizable goals**  | Set your target kicks (4-20) and session length |
| **Dark mode**           | Easy on the eyes, day or night                  |
| **Dynamic colors**      | Matches your phone’s theme on Android 12+       |

### Screenshots

<table><thead><tr><th width="309.5">History</th><th>Counter</th></tr></thead><tbody><tr><td></td><td></td></tr><tr><td>View past sessions</td><td>Tap to count kicks</td></tr></tbody></table>

### Why Track Kicks?

Monitoring fetal movements is a simple way to stay aware of your baby’s activity patterns. Most healthcare providers recommend tracking kicks starting around week 28 of pregnancy. Fetal Kick makes it easy to notice patterns and share data with your care team.

> **Note:** This app is for tracking purposes only. If you notice any changes in your baby’s movement patterns, please contact your healthcare provider.

### Get Started

#### For Users

1. Install the app on your Android device
2. Open `Fetal Kick` and tap `New Session`
3. Choose your meal and date, then confirm
4. Tap the button each time you feel a kick
5. Exit when done — kicks are saved automatically

#### For Developers

**Requirements**

* Android Studio Hedgehog or later
* JDK 17+
* Android SDK 37

**Build from source**

```bash
git clone https://github.com/your-repo/FetalKick.git
cd FetalKick
./gradlew assembleDebug
```

The APK will be in `app/build/outputs/apk/debug/`.

### Permissions

* **Storage** — For exporting your data to a file
* **Notifications** — Optional reminders to track kicks

### Support

Having issues or have a suggestion? Open an issue on GitHub.

**Made with care for expecting parents everywhere.**
