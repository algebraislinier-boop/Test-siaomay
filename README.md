# CamDrive Android prototype

A Xiaomi Home-inspired UI prototype for a Xiaomi C300 + Google Drive motion-clip workflow.

## Important status
- This is a UI prototype with sample motion events only.
- It does not connect to the Xiaomi C300, capture motion, authenticate with Google, upload files, or play cloud videos yet.
- The C300's accessible video source must be validated before true automatic recording can be implemented. Its lack of RTSP/ONVIF means this cannot be solved by simply pointing the app at a standard stream URL.
- No Google password or access token should be embedded in the APK. Use Google's OAuth flow in the app.

## Build APK using GitHub Actions
1. Create a new GitHub repository and upload the contents of this folder.
2. Open the repository's **Actions** tab.
3. Run **Build debug APK** (or push to `main`).
4. Download the `CamDrive-debug-apk` artifact from the completed workflow run.
5. Extract the ZIP, then install `app-debug.apk` on your Android phone. You may need to allow installation from that source.

The APK is a debug build for testing, not a Play Store release.
