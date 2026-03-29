---
description: >-
  A simple Android app to track fetal kicks after each meal during pregnancy
  using a daily calendar, numeric counters and optional stopwatch tracking.
icon: baby
coverY: 0
layout: landing
---

# Fetal Kick

Fetal Kick is a simple Android app for tracking fetal movements.

Count kicks by meal, review daily history, and export your records when needed.

{% hint style="warning" %}
Fetal Kick is a tracking tool. It does not replace medical advice. If your baby’s movement changes, contact your healthcare provider.
{% endhint %}

### Start here

* [Getting started](docs/getting-started.md)
* [Track a session](docs/track-a-session.md)
* [History and export](docs/history-and-export.md)
* [Settings, goals, and widget](docs/settings-goals-and-widget.md)

### What you can do

* Track sessions by meal and date
* Count kicks with an optional timer
* Review saved sessions in history
* Export data as `CSV`
* Check today’s total from a home screen widget
* Use dark mode and dynamic colors on Android `12+`

### Typical flow

{% stepper %}
{% step %}
### Start a new session

Tap `New Session`, then choose a meal and date.
{% endstep %}

{% step %}
### Count movements

Tap once for each kick. Use the timer if you want a fixed session length.
{% endstep %}

{% step %}
### Save and review

Exit the session to save it. Then review it later in history or export it as `CSV`.
{% endstep %}
{% endstepper %}

### Build from source

#### Requirements

* Android Studio Hedgehog or later
* JDK `17+`
* Android SDK `37`

#### Build

```bash
git clone https://github.com/your-repo/FetalKick.git
cd FetalKick
./gradlew assembleDebug
```

The debug APK is created in `app/build/outputs/apk/debug/`.
