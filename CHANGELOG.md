# [0.155.0-dev.2](https://github.com/anddea/revanced-integrations/compare/v0.155.0-dev.1...v0.155.0-dev.2) (2024-07-31)


### Bug Fixes

* No longer hiding `Settings` ([f291c2c](https://github.com/anddea/revanced-integrations/commit/f291c2cc41e085713c41a76bcff2b35f57f5665f))
* **YouTube - Hide feed components:** Sometimes `Hide carousel shelf` setting doesn't work ([c7476ec](https://github.com/anddea/revanced-integrations/commit/c7476ec90cd0aca35c9b9f557128f98258df185c))


### Features

* **YouTube Music - Player components:** Add settings `Disable miniplayer gesture`, `Disable player gesture` ([a2a6d4d](https://github.com/anddea/revanced-integrations/commit/a2a6d4dd0bdd82fde67188aa67b3f22c89e58ef9))

# [0.155.0-dev.1](https://github.com/anddea/revanced-integrations/compare/v0.154.0...v0.155.0-dev.1) (2024-07-29)


### Bug Fixes

* **YouTube - Client spoof:** Some side effects of iOS client ([37a814a](https://github.com/anddea/revanced-integrations/commit/37a814aa05f6c20cc479565963b88965fc88e25d))
* **YouTube - Disable auto captions:** Turning on `Disable forced auto captions` will disable subtitles ([5d31427](https://github.com/anddea/revanced-integrations/commit/5d31427e75e692c2a0b4d7e9121255c4beb5f3b7))
* **YouTube - Feed components:** `Hide recommended videos` setting does not work in home feed ([ddf22bb](https://github.com/anddea/revanced-integrations/commit/ddf22bb61ce6f38645e7586ea0fa981265ceed81))
* **YouTube - Hide feed components:** `Hide carousel shelf` setting sometimes hides the Watch history in the You tab ([0732b8c](https://github.com/anddea/revanced-integrations/commit/0732b8ceda67cb5ffdcbbc30b17700cf5bcd5dff))
* **YouTube - SponsorBlock:** The new SponsorBlock segment popup doesn't show minutes in the timestamp ([aec368b](https://github.com/anddea/revanced-integrations/commit/aec368b779549356a825cef82bc8a02713e50541))
* **YouTube - Toolbar components:** Turning on the `Hide voice search button` setting makes the margin of the searchbar 0 ([26a4aa9](https://github.com/anddea/revanced-integrations/commit/26a4aa9ba5723ccd6a826585b22c65f14073b807))


### Features

* **YouTube - Description components:** Add `Hide Contents section` setting ([2065aad](https://github.com/anddea/revanced-integrations/commit/2065aadf62b38b0f2ce47ee506bfc94f9beb6679))
* **YouTube - Player components:** Add `Hide zoom overlay` setting ([253c5d2](https://github.com/anddea/revanced-integrations/commit/253c5d29afdaa9db5874f29209955ee48903a267))
* **YouTube - Video playback:** Show AlertDialog when changing `Skip preloaded buffer` setting ([1db72fb](https://github.com/anddea/revanced-integrations/commit/1db72fbba636f011e422ee923b2557c08d988722))
* **YouTube Music - Hide layout components:** Add `Hide settings menu` setting ([262d068](https://github.com/anddea/revanced-integrations/commit/262d068a0d195d9d73e0217ffc7a71dde76f05fe))
* **YouTube:** Add `Watch history` patch ([5963ac1](https://github.com/anddea/revanced-integrations/commit/5963ac14cd2350f243d056efcf436589db636c5b))

# [0.154.0](https://github.com/anddea/revanced-integrations/compare/v0.153.0...v0.154.0) (2024-07-25)


### Bug Fixes

* **Reddit - Remove subreddit dialog:** Patch sometimes fails to close subreddit dialog ([7bcceb3](https://github.com/anddea/revanced-integrations/commit/7bcceb33e026c328a4babb09ffece8406445b3e9))
* **Shorts components:** `Hide sound button` doesn't work (A/B tests) ([af0f2e3](https://github.com/anddea/revanced-integrations/commit/af0f2e30b0e966fbb13860da33bb03ab7171c3c1))
* **YouTube - Client spoof:** StoryboardRenderer always gets 404 response on `Android` client ([6fdcb8f](https://github.com/anddea/revanced-integrations/commit/6fdcb8fa67e6d5c4aa54be96fde31b33dd0ad507))
* **YouTube - Hide ads:** New ads are not blocked ([2383172](https://github.com/anddea/revanced-integrations/commit/23831723fd0bce8b23906b553d7d71e34bea562c))
* **YouTube - Hide ads:** Patch closes fullscreen ads too quickly, so fullscreen ads are shown repeatedly ([475739c](https://github.com/anddea/revanced-integrations/commit/475739ce04b4f5585a0421f8a3798c638afcc3cf))
* **YouTube - Hide feed components:** `Hide carousel shelf` setting sometimes hides the Watch history in the You tab ([97ea082](https://github.com/anddea/revanced-integrations/commit/97ea082994644341d2ea3953200277e8e9c0d9e1))
* **YouTube - Hide feed components:** Detect if a keyword filter hides all videos ([960f35c](https://github.com/anddea/revanced-integrations/commit/960f35c525660f41ec5218a8fb325b91dadc2d18))
* **YouTube - Hide feed components:** Do not hide flyout menu ([1310d7b](https://github.com/anddea/revanced-integrations/commit/1310d7b5c3713c0dd57cc76c7cc9b4f0f32460ea))
* **YouTube - Hide feed components:** Remove `MINIMUM_KEYWORD_LENGTH` ([4e4c104](https://github.com/anddea/revanced-integrations/commit/4e4c1044be3a43c27366d7f24c35e9c2fab3ad3d))
* **YouTube - Hide feed components:** Update pattern for `Match full word` keyword filter ([8c1cb35](https://github.com/anddea/revanced-integrations/commit/8c1cb35c0819a9d57466eecc83a4917ef3c4b0af))
* **YouTube - Hide feed components:** Video filters do not work properly on accounts with A/B testing applied ([67dcfe7](https://github.com/anddea/revanced-integrations/commit/67dcfe71fc7d4b2c06d66e635c72a99eb8fa0304))
* **YouTube - Settings:** Toolbar added twice to RVX settings ([b050be9](https://github.com/anddea/revanced-integrations/commit/b050be95468ce35151492b904ab49b7ef5f5122e))
* **YouTube - SponsorBlock:** Skip segments when casting ([6d2d33d](https://github.com/anddea/revanced-integrations/commit/6d2d33d816f7bd33499ea4a5828341cc75c7145d))
* **YouTube - Spoof client:** Delay when the video starts ([d43d139](https://github.com/anddea/revanced-integrations/commit/d43d1399cfe6a6a44faf293d320ce671bbf5e6b2))
* **YouTube - Video playback:** Default video quality does not apply ([4c080ec](https://github.com/anddea/revanced-integrations/commit/4c080ecb1e7cf906e6789e5c99907170939606e0))
* **YouTube - VIsual preferences icons:** Revert ([848dee5](https://github.com/anddea/revanced-integrations/commit/848dee578e3755434831bbc53e73979fc83032a0))
* **YouTube:** Add a restart delay on first install ([3a3ae56](https://github.com/anddea/revanced-integrations/commit/3a3ae5616d99e04d1f457325b4dc33e8e0247313))


### Features

* Merge RVX v1.11.2-dev.1 ([50ebf3a](https://github.com/anddea/revanced-integrations/commit/50ebf3a124e1d60a5c371689a2ba2ff8a16ab1db))
* Merge the `Custom package name` patch into the `GmsCore support` patch and add patch options `GmsCoreVendorGroupId`, `CheckGmsCore`, `PackageNameYouTube`, `PackageNameYouTubeMusic` ([5465751](https://github.com/anddea/revanced-integrations/commit/5465751913168b3ed1e51d47cde088fa25de62d7))
* **Reddit - Hide navigation buttons:** Reflects changes in the latest revanced-patches-arsclib ([7a5481c](https://github.com/anddea/revanced-integrations/commit/7a5481c280d4cef4cc107e855744d7dc11c47344))
* **YouTube - Alternative thumbnails:** Add alternative domain ([c9f47c2](https://github.com/anddea/revanced-integrations/commit/c9f47c22636b2951a2c22e1e9319b598764574ec))
* **YouTube - Bypass image region restrictions:** Bring back setting to change alternative domain ([df5d392](https://github.com/anddea/revanced-integrations/commit/df5d3921cdb529540d3e41fc4e102da3cd6f1150))
* **YouTube - Client spoof:** Downgrade hardcoded client version ([bb5e7a6](https://github.com/anddea/revanced-integrations/commit/bb5e7a60de31fdd8624287276fab201f04861513))
* **YouTube - Hide feed components:** Add `Match full word` option for keyword filtering ([8323c0f](https://github.com/anddea/revanced-integrations/commit/8323c0fc2628b02ef58e6b52e9c59f299d37330f))
* **YouTube - Miniplayer:** Add `Enable double-tap action` setting ([766925d](https://github.com/anddea/revanced-integrations/commit/766925dde8eb6089ef932f968a73820eef7211ad))
* **YouTube - Overlay buttons:** Add `Mute Video` button ([#22](https://github.com/anddea/revanced-integrations/issues/22)) ([4a9f4a6](https://github.com/anddea/revanced-integrations/commit/4a9f4a603c7eda695dc5d2708c89b0ff71e5b460))
* **YouTube - Player components:** `Hide player popup panels` setting now hides the products panel ([9f689f3](https://github.com/anddea/revanced-integrations/commit/9f689f32f32727ce7e897932734207cc59d566d7))
* **YouTube - Searchbar:** Add RVXSettingsMenuName in the searchbar hint ([b1a1dd9](https://github.com/anddea/revanced-integrations/commit/b1a1dd90ead7e2325993f2314270e5ca9c1c93ba))
* **YouTube - Settings:** Show AlertDialog when changing some settings value (matches ReVanced) ([3b7f255](https://github.com/anddea/revanced-integrations/commit/3b7f255f3f197269c588b4de768646d2cfc22ea5))
* **YouTube - Shorts components:** Add `Double-tap animation` settings ([2e4c173](https://github.com/anddea/revanced-integrations/commit/2e4c173c8d8bd66aa0f1d73b3fa4940dcd18e04a))
* **YouTube - Shorts components:** Add `Enable timestamps`, `Timestamp long press action`, `Meta panel bottom margin` settings ([be36036](https://github.com/anddea/revanced-integrations/commit/be360364b5ecd4166b9d100e4088ff22839edbd6))
* **YouTube - Shorts components:** Add `Hide paused header` setting ([80b8058](https://github.com/anddea/revanced-integrations/commit/80b8058aedaf1135850a032095873f1f04e69f98))
* **YouTube - Swipe controls:** Add `Enable save and restore brightness` setting (Match to ReVanced) ([1400597](https://github.com/anddea/revanced-integrations/commit/140059749d83603daf8e4c5a83dbe793e0e0217c))
* **YouTube - Toolbar components:** Add `Hide image search button` settings ([543d269](https://github.com/anddea/revanced-integrations/commit/543d269e16f6e8ee0016015087cf9a0467278fdc))
* **YouTube Music - Thumbnails:** Add `Alternative domain` patch ([15ae875](https://github.com/anddea/revanced-integrations/commit/15ae8755a6458529781bee38b9361a238d718b5f))
* **YouTube Music:** Integrate `Hide double tap overlay filter` patch into the `Player components` patch ([7628ed1](https://github.com/anddea/revanced-integrations/commit/7628ed153d06ff3e593eea1ee3e6056d048efe3d))
* **YouTube:** Add content descriptions to improve accessibility ([538e262](https://github.com/anddea/revanced-integrations/commit/538e2626c655f885d961077768114e81cdcfc2ec))
* **YouTube:** Add support version `19.25.39`, drop support version `19.23.40` ([dd95309](https://github.com/anddea/revanced-integrations/commit/dd95309bef8a7972513cc583e8c39bb4254091bf))
* **YouTube:** DeArrow alternative domain ([#20](https://github.com/anddea/revanced-integrations/issues/20)) ([dbf5b0a](https://github.com/anddea/revanced-integrations/commit/dbf5b0a02d2c85e035944ac754d7ba37928ecb32))
* **YouTube:** Integrate `Hide double tap overlay filter` patch into the `Player components` patch ([ccc20d8](https://github.com/anddea/revanced-integrations/commit/ccc20d8032abd7be243f57c84a5ae61a1f7b4589))
* **YouTube:** Merge RVX v1.11.1 ([#23](https://github.com/anddea/revanced-integrations/issues/23)) ([bcc5c29](https://github.com/anddea/revanced-integrations/commit/bcc5c29189191fe5759b01891899945d36fd1f95))
* **YouTube:** Separate the `Bypass image region restrictions` patch from the `Alternative thumbnails` patch (Reflecting changes in ReVanced) ([c4c4b65](https://github.com/anddea/revanced-integrations/commit/c4c4b65d3afc0efb28bc65d2c99cbbd5ba6e9861))


### Reverts

* **YouTube - Hide feed components:** `Hide expandable chip under videos` setting does not work (as support version has been rolled back to YouTube 19.16.39) ([7c52971](https://github.com/anddea/revanced-integrations/commit/7c5297187fc52700ba525d6c281d4d9ecd2c1b00))

# [0.154.0-dev.9](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.8...v0.154.0-dev.9) (2024-07-22)


### Features

* **YouTube - Bypass image region restrictions:** Bring back setting to change alternative domain ([df5d392](https://github.com/anddea/revanced-integrations/commit/df5d3921cdb529540d3e41fc4e102da3cd6f1150))

# [0.154.0-dev.8](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.7...v0.154.0-dev.8) (2024-07-18)


### Bug Fixes

* **YouTube - Client spoof:** StoryboardRenderer always gets 404 response on `Android` client ([6fdcb8f](https://github.com/anddea/revanced-integrations/commit/6fdcb8fa67e6d5c4aa54be96fde31b33dd0ad507))
* **YouTube - Hide feed components:** `Hide carousel shelf` setting sometimes hides the Watch history in the You tab ([97ea082](https://github.com/anddea/revanced-integrations/commit/97ea082994644341d2ea3953200277e8e9c0d9e1))
* **YouTube - Hide feed components:** Do not hide flyout menu ([1310d7b](https://github.com/anddea/revanced-integrations/commit/1310d7b5c3713c0dd57cc76c7cc9b4f0f32460ea))
* **YouTube:** Add a restart delay on first install ([3a3ae56](https://github.com/anddea/revanced-integrations/commit/3a3ae5616d99e04d1f457325b4dc33e8e0247313))


### Features

* Merge the `Custom package name` patch into the `GmsCore support` patch and add patch options `GmsCoreVendorGroupId`, `CheckGmsCore`, `PackageNameYouTube`, `PackageNameYouTubeMusic` ([5465751](https://github.com/anddea/revanced-integrations/commit/5465751913168b3ed1e51d47cde088fa25de62d7))
* **Reddit - Hide navigation buttons:** Reflects changes in the latest revanced-patches-arsclib ([7a5481c](https://github.com/anddea/revanced-integrations/commit/7a5481c280d4cef4cc107e855744d7dc11c47344))
* **YouTube - Player components:** `Hide player popup panels` setting now hides the products panel ([9f689f3](https://github.com/anddea/revanced-integrations/commit/9f689f32f32727ce7e897932734207cc59d566d7))
* **YouTube - Swipe controls:** Add `Enable save and restore brightness` setting (Match to ReVanced) ([1400597](https://github.com/anddea/revanced-integrations/commit/140059749d83603daf8e4c5a83dbe793e0e0217c))
* **YouTube:** Separate the `Bypass image region restrictions` patch from the `Alternative thumbnails` patch (Reflecting changes in ReVanced) ([c4c4b65](https://github.com/anddea/revanced-integrations/commit/c4c4b65d3afc0efb28bc65d2c99cbbd5ba6e9861))


### Reverts

* **YouTube - Hide feed components:** `Hide expandable chip under videos` setting does not work (as support version has been rolled back to YouTube 19.16.39) ([7c52971](https://github.com/anddea/revanced-integrations/commit/7c5297187fc52700ba525d6c281d4d9ecd2c1b00))

# [0.154.0-dev.7](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.6...v0.154.0-dev.7) (2024-07-15)


### Bug Fixes

* **YouTube - VIsual preferences icons:** Revert ([848dee5](https://github.com/anddea/revanced-integrations/commit/848dee578e3755434831bbc53e73979fc83032a0))

# [0.154.0-dev.6](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.5...v0.154.0-dev.6) (2024-07-14)


### Bug Fixes

* **YouTube - SponsorBlock:** Skip segments when casting ([6d2d33d](https://github.com/anddea/revanced-integrations/commit/6d2d33d816f7bd33499ea4a5828341cc75c7145d))
* **YouTube - Spoof client:** Delay when the video starts ([d43d139](https://github.com/anddea/revanced-integrations/commit/d43d1399cfe6a6a44faf293d320ce671bbf5e6b2))
* **YouTube - Video playback:** Default video quality does not apply ([4c080ec](https://github.com/anddea/revanced-integrations/commit/4c080ecb1e7cf906e6789e5c99907170939606e0))


### Features

* Merge RVX v1.11.2-dev.1 ([50ebf3a](https://github.com/anddea/revanced-integrations/commit/50ebf3a124e1d60a5c371689a2ba2ff8a16ab1db))
* **YouTube - Shorts components:** Add `Hide paused header` setting ([80b8058](https://github.com/anddea/revanced-integrations/commit/80b8058aedaf1135850a032095873f1f04e69f98))
* **YouTube Music - Thumbnails:** Add `Alternative domain` patch ([15ae875](https://github.com/anddea/revanced-integrations/commit/15ae8755a6458529781bee38b9361a238d718b5f))

# [0.154.0-dev.5](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.4...v0.154.0-dev.5) (2024-07-06)


### Bug Fixes

* **YouTube - Hide ads:** New ads are not blocked ([2383172](https://github.com/anddea/revanced-integrations/commit/23831723fd0bce8b23906b553d7d71e34bea562c))


### Features

* **YouTube - Alternative thumbnails:** Add alternative domain ([c9f47c2](https://github.com/anddea/revanced-integrations/commit/c9f47c22636b2951a2c22e1e9319b598764574ec))
* **YouTube - Client spoof:** Downgrade hardcoded client version ([bb5e7a6](https://github.com/anddea/revanced-integrations/commit/bb5e7a60de31fdd8624287276fab201f04861513))
* **YouTube - Miniplayer:** Add `Enable double-tap action` setting ([766925d](https://github.com/anddea/revanced-integrations/commit/766925dde8eb6089ef932f968a73820eef7211ad))
* **YouTube - Shorts components:** Add `Enable timestamps`, `Timestamp long press action`, `Meta panel bottom margin` settings ([be36036](https://github.com/anddea/revanced-integrations/commit/be360364b5ecd4166b9d100e4088ff22839edbd6))
* **YouTube:** Add support version `19.25.39`, drop support version `19.23.40` ([dd95309](https://github.com/anddea/revanced-integrations/commit/dd95309bef8a7972513cc583e8c39bb4254091bf))
* **YouTube:** Merge RVX v1.11.1 ([#23](https://github.com/anddea/revanced-integrations/issues/23)) ([bcc5c29](https://github.com/anddea/revanced-integrations/commit/bcc5c29189191fe5759b01891899945d36fd1f95))

# [0.154.0-dev.4](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.3...v0.154.0-dev.4) (2024-07-05)


### Bug Fixes

* **YouTube - Hide feed components:** Update pattern for `Match full word` keyword filter ([8c1cb35](https://github.com/anddea/revanced-integrations/commit/8c1cb35c0819a9d57466eecc83a4917ef3c4b0af))


### Features

* **YouTube - Overlay buttons:** Add `Mute Video` button ([#22](https://github.com/anddea/revanced-integrations/issues/22)) ([4a9f4a6](https://github.com/anddea/revanced-integrations/commit/4a9f4a603c7eda695dc5d2708c89b0ff71e5b460))

# [0.154.0-dev.3](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.2...v0.154.0-dev.3) (2024-07-04)


### Features

* **YouTube:** Add content descriptions to improve accessibility ([538e262](https://github.com/anddea/revanced-integrations/commit/538e2626c655f885d961077768114e81cdcfc2ec))

# [0.154.0-dev.2](https://github.com/anddea/revanced-integrations/compare/v0.154.0-dev.1...v0.154.0-dev.2) (2024-07-03)


### Bug Fixes

* **YouTube - Hide feed components:** Remove `MINIMUM_KEYWORD_LENGTH` ([4e4c104](https://github.com/anddea/revanced-integrations/commit/4e4c1044be3a43c27366d7f24c35e9c2fab3ad3d))


### Features

* **YouTube - Hide feed components:** Add `Match full word` option for keyword filtering ([8323c0f](https://github.com/anddea/revanced-integrations/commit/8323c0fc2628b02ef58e6b52e9c59f299d37330f))

# [0.154.0-dev.1](https://github.com/anddea/revanced-integrations/compare/v0.153.0...v0.154.0-dev.1) (2024-07-01)


### Bug Fixes

* **Reddit - Remove subreddit dialog:** Patch sometimes fails to close subreddit dialog ([7bcceb3](https://github.com/anddea/revanced-integrations/commit/7bcceb33e026c328a4babb09ffece8406445b3e9))
* **Shorts components:** `Hide sound button` doesn't work (A/B tests) ([af0f2e3](https://github.com/anddea/revanced-integrations/commit/af0f2e30b0e966fbb13860da33bb03ab7171c3c1))
* **YouTube - Hide ads:** Patch closes fullscreen ads too quickly, so fullscreen ads are shown repeatedly ([475739c](https://github.com/anddea/revanced-integrations/commit/475739ce04b4f5585a0421f8a3798c638afcc3cf))
* **YouTube - Hide feed components:** Detect if a keyword filter hides all videos ([960f35c](https://github.com/anddea/revanced-integrations/commit/960f35c525660f41ec5218a8fb325b91dadc2d18))
* **YouTube - Hide feed components:** Video filters do not work properly on accounts with A/B testing applied ([67dcfe7](https://github.com/anddea/revanced-integrations/commit/67dcfe71fc7d4b2c06d66e635c72a99eb8fa0304))
* **YouTube - Settings:** Toolbar added twice to RVX settings ([b050be9](https://github.com/anddea/revanced-integrations/commit/b050be95468ce35151492b904ab49b7ef5f5122e))


### Features

* **YouTube - Searchbar:** Add RVXSettingsMenuName in the searchbar hint ([b1a1dd9](https://github.com/anddea/revanced-integrations/commit/b1a1dd90ead7e2325993f2314270e5ca9c1c93ba))
* **YouTube - Settings:** Show AlertDialog when changing some settings value (matches ReVanced) ([3b7f255](https://github.com/anddea/revanced-integrations/commit/3b7f255f3f197269c588b4de768646d2cfc22ea5))
* **YouTube - Shorts components:** Add `Double-tap animation` settings ([2e4c173](https://github.com/anddea/revanced-integrations/commit/2e4c173c8d8bd66aa0f1d73b3fa4940dcd18e04a))
* **YouTube - Toolbar components:** Add `Hide image search button` settings ([543d269](https://github.com/anddea/revanced-integrations/commit/543d269e16f6e8ee0016015087cf9a0467278fdc))
* **YouTube Music:** Integrate `Hide double tap overlay filter` patch into the `Player components` patch ([7628ed1](https://github.com/anddea/revanced-integrations/commit/7628ed153d06ff3e593eea1ee3e6056d048efe3d))
* **YouTube:** DeArrow alternative domain ([#20](https://github.com/anddea/revanced-integrations/issues/20)) ([dbf5b0a](https://github.com/anddea/revanced-integrations/commit/dbf5b0a02d2c85e035944ac754d7ba37928ecb32))
* **YouTube:** Integrate `Hide double tap overlay filter` patch into the `Player components` patch ([ccc20d8](https://github.com/anddea/revanced-integrations/commit/ccc20d8032abd7be243f57c84a5ae61a1f7b4589))

# [0.153.0](https://github.com/anddea/revanced-integrations/compare/v0.152.0...v0.153.0) (2024-06-26)


### Bug Fixes

* **GmsCore support:** Spoof package name ([90cd8fd](https://github.com/anddea/revanced-integrations/commit/90cd8fd1fc7c136ef061a0b8b7ececea94ed9853))
* **Hide ads:** app crashes in the old client ([bf3cdc9](https://github.com/anddea/revanced-integrations/commit/bf3cdc962b8e043f8597d87870bdf2f69ce5e3b7))
* **YouTube - Hide feed components:** `Hide carousel shelf` setting does not work (A/B tests) ([8bac6f9](https://github.com/anddea/revanced-integrations/commit/8bac6f9287f23eef1a53675d53b0daa7ee0d78e0))
* **YouTube - Hide feed components:** `Hide expandable chip under videos` setting does not work (A/B tests) ([f33e4b6](https://github.com/anddea/revanced-integrations/commit/f33e4b6ac56fd77e3014c1e18abf4fbe01a82d55))
* **YouTube - Hide feed components:** `Keyword filter`, `Hide low views video`, `Hide recommended videos by views` setting does not work (A/B tests) ([acaebc0](https://github.com/anddea/revanced-integrations/commit/acaebc0ab6dc4c6261c38d0a724203025ecd05ac))
* **YouTube - Hide feed components:** Some keywords broke interface ([c90673c](https://github.com/anddea/revanced-integrations/commit/c90673c37ee6989e09a656740844aaf0fe857e85))
* **YouTube - Miniplayer:** `Hide expand and close buttons` setting is not disabled in `Modern 1` on YouTube 19.20.35+ ([a8ad2aa](https://github.com/anddea/revanced-integrations/commit/a8ad2aa062b8e4c607b721641b06da40a4160569))
* **YouTube - Player components:** `Hide Open mix playlist button` and `Hide Open playlist button` did not work ([f69091c](https://github.com/anddea/revanced-integrations/commit/f69091ce939a9f7f3cc490614b93432204cddb4f))
* **YouTube - Return YouTube Dislike:** No longer hides glow animation ([a80a157](https://github.com/anddea/revanced-integrations/commit/a80a15756c94c339b5b55736055e4a786ccf8092))
* **YouTube - Shorts components:** Better filtering for disabled comment button ([5cd6c0c](https://github.com/anddea/revanced-integrations/commit/5cd6c0c61c2db00e46da04dfb8c1d41ca2ece93a))
* **YouTube - Shorts components:** Improve pattern for disabled comments button ([a74fd78](https://github.com/anddea/revanced-integrations/commit/a74fd78853649ae6b5527affb1358acad2641229))
* **YouTube - Shorts components:** More robust pattern for disabled comment button ([386ba9b](https://github.com/anddea/revanced-integrations/commit/386ba9bdf00067b301786c6dfc304a7a7cdaf91a))
* **YouTube - Spoof client:** Player gestures not working when spoofing with `Android VR` client ([067f0ec](https://github.com/anddea/revanced-integrations/commit/067f0ec416c77d98673d736a9b26e0370f0fee62))
* **YouTube - Spoof client:** Seekbar thumbnail not shown in `Android Testsuite` client ([0960b91](https://github.com/anddea/revanced-integrations/commit/0960b916c20a6c07e39ac18974cbaf90a4b7fefb))
* **YouTube - Toolbar components:** Add support for Cairo icon ([319dc19](https://github.com/anddea/revanced-integrations/commit/319dc19131f0b6bd865897eb3fda01692f112222))
* **YouTube Music:** The app crashes on older clients ([2a781c4](https://github.com/anddea/revanced-integrations/commit/2a781c4adc10aec649a2d019c56904f6be374fe6))
* **YouTube/Hide ads:** toasts are shown multiple times ([f89390f](https://github.com/anddea/revanced-integrations/commit/f89390feacec3dd613c7e44f910e05cd87e584dc))
* **YouTube/Hide feed components:** `Hide Visit store button` setting does not work ([f844ff8](https://github.com/anddea/revanced-integrations/commit/f844ff8597c785a30d10daae00fe899dd11df17e))
* **YouTube/Hide feed components:** `Hide Visit store button` setting does not work ([a09a33d](https://github.com/anddea/revanced-integrations/commit/a09a33dac2a717d1c8c78bb2ee87fc892d639fe3))
* **YouTube/Hide feed components:** community posts are not hidden ([64b8814](https://github.com/anddea/revanced-integrations/commit/64b88146efd11f5430e5ee91ac6f563cd820a250))
* **YouTube/Return YouTube Channel Name:** Correctly handle exception ([4eeebf4](https://github.com/anddea/revanced-integrations/commit/4eeebf45c65dc7939020a453df6171fa4af67cd2))
* **YouTube/Spoof client:** first video is always spoofed as a client of Shorts, Clips ([14c22a5](https://github.com/anddea/revanced-integrations/commit/14c22a50d21d728a54570a31ac889e6e6c1a45a9))
* **YouTube/Spoof client:** restore playback speed menu when spoofing to an iOS, Android TV, Android Testsuite client ([fcc7510](https://github.com/anddea/revanced-integrations/commit/fcc75101f963cd2f4815c4cba09f6fb21ea76e76))
* **YouTube:** rename `Enable minimized playback` patch to `Remove background playback restrictions` ([b3e0c9f](https://github.com/anddea/revanced-integrations/commit/b3e0c9f54a26e09381aa6f161db1f312cd3e1e41))


### Features

* **GmsCore support:** Add `Don't show again` option for battery optimization dialog ([ecb5836](https://github.com/anddea/revanced-integrations/commit/ecb5836ce209a60cde36c02ada06b237c243971d))
* **Hide ads:** add `Close fullscreen ads` settings ([2707232](https://github.com/anddea/revanced-integrations/commit/2707232104b98f7c4471b063354c7bda3af5494d))
* **PlayerRoutes:** update hardcoded client version ([605fdcd](https://github.com/anddea/revanced-integrations/commit/605fdcd8ed82f72b795efdd883c356dfc781740c))
* **Reddit:** Add `Hide recommended communities shelf` patch ([a09af41](https://github.com/anddea/revanced-integrations/commit/a09af417edeaf191ef7b9bc16ba00e15c34b59c0))
* **YouTube - Description components:** Separate the `Hide Key concepts section` setting from the `Hide Chapters section` setting ([edbf474](https://github.com/anddea/revanced-integrations/commit/edbf47462002dbeb186844be84d9f7bdaad03653))
* **YouTube - Miniplayer:** Add `Enable drag and drop` setting (YouTube 19.23.40+) ([5d43c29](https://github.com/anddea/revanced-integrations/commit/5d43c29d31b02464127dbfd23832d4e7bea427fd))
* **YouTube - Navigation bar components:** Add `Enable translucent navigation bar` setting ([75ba63b](https://github.com/anddea/revanced-integrations/commit/75ba63ba89ac8250f9662eda1d451b19e769d597))
* **YouTube - Searchbar:** Restyle ([#17](https://github.com/anddea/revanced-integrations/issues/17)) ([cc5e023](https://github.com/anddea/revanced-integrations/commit/cc5e023a7b1f70b07ca0e6a9f30b6945ec78066e))
* **YouTube - Seekbar components:** Add `Enable Cairo seekbar` setting (YouTube 19.23.40+) ([2e79b43](https://github.com/anddea/revanced-integrations/commit/2e79b435b91bafcf520fd1538ab56d09cd908507))
* **YouTube - Spoof client:** Add `Show in Stats for nerds` settings ([6c5ff0c](https://github.com/anddea/revanced-integrations/commit/6c5ff0c1b6176b2c15ca64b42693b518eb004225))
* **YouTube - Spoof client:** Selectively spoof client for general video / livestreams / Shorts / fallback (unplayable video) ([c8e31ec](https://github.com/anddea/revanced-integrations/commit/c8e31ec07069d649aeab9da3c4900bb063270e2c))
* **YouTube Music:** Add `Enable Cairo splash animation` patch (YouTube Music 7.06.53+) ([255e1fc](https://github.com/anddea/revanced-integrations/commit/255e1fc64f5d923ef9ab11153817e1be28bf2e0a))
* **YouTube Music:** change default value ([cd072a3](https://github.com/anddea/revanced-integrations/commit/cd072a31f2bb8ce0b63d00a1fc9f74e05012a1f1))
* **YouTube Music:** Remove `Replace Cast button` patch ([f887f24](https://github.com/anddea/revanced-integrations/commit/f887f24c029c04f5d7eea9e8486003228b42695d))
* **YouTube/Hide action buttons:** add `Disable Like and Dislike button glow` setting ([ffb9a08](https://github.com/anddea/revanced-integrations/commit/ffb9a08c182cf68bc4c15cf4a4517372ad9a5179))
* **YouTube/Hide feed components:** separate the `Hide low views video` settings from `Hide recommended videos` settings ([730c58c](https://github.com/anddea/revanced-integrations/commit/730c58ccae8edbba08acf406f5f6ef43beffd5dc))
* **YouTube/Settings:** unify toast key format ([a3ce453](https://github.com/anddea/revanced-integrations/commit/a3ce453f778ef18eb4e449548c402ce0da808f36))
* **YouTube/Shorts components:** add `Hide Super Thanks button` settings ([6addcae](https://github.com/anddea/revanced-integrations/commit/6addcae7a36c0774365cdffcdae1266ffd234c85))
* **YouTube:** add `Enable OPUS codec` patch ([31578d0](https://github.com/anddea/revanced-integrations/commit/31578d01210a70cc46aeaf6347ac16cd62b47f77))
* **YouTube:** add `Miniplayer` patch (Replaces `Enable tablet mini player` patch) ([465a638](https://github.com/anddea/revanced-integrations/commit/465a6380690d89b28e1cc7c69c716cce5af6b4aa))

# [0.153.0-dev.10](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.9...v0.153.0-dev.10) (2024-06-26)


### Features

* **YouTube - Searchbar:** Restyle ([#17](https://github.com/anddea/revanced-integrations/issues/17)) ([cc5e023](https://github.com/anddea/revanced-integrations/commit/cc5e023a7b1f70b07ca0e6a9f30b6945ec78066e))

# [0.153.0-dev.9](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.8...v0.153.0-dev.9) (2024-06-23)


### Bug Fixes

* **GmsCore support:** Spoof package name ([90cd8fd](https://github.com/anddea/revanced-integrations/commit/90cd8fd1fc7c136ef061a0b8b7ececea94ed9853))
* **YouTube - Hide feed components:** `Hide carousel shelf` setting does not work (A/B tests) ([8bac6f9](https://github.com/anddea/revanced-integrations/commit/8bac6f9287f23eef1a53675d53b0daa7ee0d78e0))
* **YouTube - Hide feed components:** `Hide expandable chip under videos` setting does not work (A/B tests) ([f33e4b6](https://github.com/anddea/revanced-integrations/commit/f33e4b6ac56fd77e3014c1e18abf4fbe01a82d55))
* **YouTube - Hide feed components:** `Keyword filter`, `Hide low views video`, `Hide recommended videos by views` setting does not work (A/B tests) ([acaebc0](https://github.com/anddea/revanced-integrations/commit/acaebc0ab6dc4c6261c38d0a724203025ecd05ac))
* **YouTube - Miniplayer:** `Hide expand and close buttons` setting is not disabled in `Modern 1` on YouTube 19.20.35+ ([a8ad2aa](https://github.com/anddea/revanced-integrations/commit/a8ad2aa062b8e4c607b721641b06da40a4160569))
* **YouTube - Player components:** `Hide Open mix playlist button` and `Hide Open playlist button` did not work ([f69091c](https://github.com/anddea/revanced-integrations/commit/f69091ce939a9f7f3cc490614b93432204cddb4f))
* **YouTube - Spoof client:** Seekbar thumbnail not shown in `Android Testsuite` client ([0960b91](https://github.com/anddea/revanced-integrations/commit/0960b916c20a6c07e39ac18974cbaf90a4b7fefb))
* **YouTube - Toolbar components:** Add support for Cairo icon ([319dc19](https://github.com/anddea/revanced-integrations/commit/319dc19131f0b6bd865897eb3fda01692f112222))
* **YouTube Music:** The app crashes on older clients ([2a781c4](https://github.com/anddea/revanced-integrations/commit/2a781c4adc10aec649a2d019c56904f6be374fe6))


### Features

* **Reddit:** Add `Hide recommended communities shelf` patch ([a09af41](https://github.com/anddea/revanced-integrations/commit/a09af417edeaf191ef7b9bc16ba00e15c34b59c0))
* **YouTube - Description components:** Separate the `Hide Key concepts section` setting from the `Hide Chapters section` setting ([edbf474](https://github.com/anddea/revanced-integrations/commit/edbf47462002dbeb186844be84d9f7bdaad03653))
* **YouTube - Miniplayer:** Add `Enable drag and drop` setting (YouTube 19.23.40+) ([5d43c29](https://github.com/anddea/revanced-integrations/commit/5d43c29d31b02464127dbfd23832d4e7bea427fd))
* **YouTube - Navigation bar components:** Add `Enable translucent navigation bar` setting ([75ba63b](https://github.com/anddea/revanced-integrations/commit/75ba63ba89ac8250f9662eda1d451b19e769d597))
* **YouTube - Seekbar components:** Add `Enable Cairo seekbar` setting (YouTube 19.23.40+) ([2e79b43](https://github.com/anddea/revanced-integrations/commit/2e79b435b91bafcf520fd1538ab56d09cd908507))
* **YouTube Music:** Add `Enable Cairo splash animation` patch (YouTube Music 7.06.53+) ([255e1fc](https://github.com/anddea/revanced-integrations/commit/255e1fc64f5d923ef9ab11153817e1be28bf2e0a))

# [0.153.0-dev.8](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.7...v0.153.0-dev.8) (2024-06-20)


### Bug Fixes

* **YouTube - Shorts components:** Improve pattern for disabled comments button ([a74fd78](https://github.com/anddea/revanced-integrations/commit/a74fd78853649ae6b5527affb1358acad2641229))

# [0.153.0-dev.7](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.6...v0.153.0-dev.7) (2024-06-19)


### Bug Fixes

* **YouTube - Shorts components:** More robust pattern for disabled comment button ([386ba9b](https://github.com/anddea/revanced-integrations/commit/386ba9bdf00067b301786c6dfc304a7a7cdaf91a))

# [0.153.0-dev.6](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.5...v0.153.0-dev.6) (2024-06-17)


### Bug Fixes

* **YouTube - Shorts components:** Better filtering for disabled comment button ([5cd6c0c](https://github.com/anddea/revanced-integrations/commit/5cd6c0c61c2db00e46da04dfb8c1d41ca2ece93a))

# [0.153.0-dev.5](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.4...v0.153.0-dev.5) (2024-06-14)


### Bug Fixes

* **YouTube - Hide feed components:** Some keywords broke interface ([c90673c](https://github.com/anddea/revanced-integrations/commit/c90673c37ee6989e09a656740844aaf0fe857e85))

# [0.153.0-dev.4](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.3...v0.153.0-dev.4) (2024-06-14)


### Bug Fixes

* **Hide ads:** app crashes in the old client ([bf3cdc9](https://github.com/anddea/revanced-integrations/commit/bf3cdc962b8e043f8597d87870bdf2f69ce5e3b7))
* **YouTube/Hide ads:** toasts are shown multiple times ([f89390f](https://github.com/anddea/revanced-integrations/commit/f89390feacec3dd613c7e44f910e05cd87e584dc))
* **YouTube/Hide feed components:** `Hide Visit store button` setting does not work ([f844ff8](https://github.com/anddea/revanced-integrations/commit/f844ff8597c785a30d10daae00fe899dd11df17e))
* **YouTube/Hide feed components:** community posts are not hidden ([64b8814](https://github.com/anddea/revanced-integrations/commit/64b88146efd11f5430e5ee91ac6f563cd820a250))
* **YouTube/Return YouTube Channel Name:** Correctly handle exception ([4eeebf4](https://github.com/anddea/revanced-integrations/commit/4eeebf45c65dc7939020a453df6171fa4af67cd2))
* **YouTube:** rename `Enable minimized playback` patch to `Remove background playback restrictions` ([b3e0c9f](https://github.com/anddea/revanced-integrations/commit/b3e0c9f54a26e09381aa6f161db1f312cd3e1e41))


### Features

* **YouTube/Hide action buttons:** add `Disable Like and Dislike button glow` setting ([ffb9a08](https://github.com/anddea/revanced-integrations/commit/ffb9a08c182cf68bc4c15cf4a4517372ad9a5179))
* **YouTube/Settings:** unify toast key format ([a3ce453](https://github.com/anddea/revanced-integrations/commit/a3ce453f778ef18eb4e449548c402ce0da808f36))
* **YouTube:** add `Miniplayer` patch (Replaces `Enable tablet mini player` patch) ([465a638](https://github.com/anddea/revanced-integrations/commit/465a6380690d89b28e1cc7c69c716cce5af6b4aa))

# [0.153.0-dev.3](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.2...v0.153.0-dev.3) (2024-06-11)


### Bug Fixes

* **YouTube - Return YouTube Dislike:** No longer hides glow animation ([a80a157](https://github.com/anddea/revanced-integrations/commit/a80a15756c94c339b5b55736055e4a786ccf8092))
* **YouTube/Hide feed components:** `Hide Visit store button` setting does not work ([a09a33d](https://github.com/anddea/revanced-integrations/commit/a09a33dac2a717d1c8c78bb2ee87fc892d639fe3))
* **YouTube/Spoof client:** first video is always spoofed as a client of Shorts, Clips ([14c22a5](https://github.com/anddea/revanced-integrations/commit/14c22a50d21d728a54570a31ac889e6e6c1a45a9))
* **YouTube/Spoof client:** restore playback speed menu when spoofing to an iOS, Android TV, Android Testsuite client ([fcc7510](https://github.com/anddea/revanced-integrations/commit/fcc75101f963cd2f4815c4cba09f6fb21ea76e76))


### Features

* **Hide ads:** add `Close fullscreen ads` settings ([2707232](https://github.com/anddea/revanced-integrations/commit/2707232104b98f7c4471b063354c7bda3af5494d))
* **PlayerRoutes:** update hardcoded client version ([605fdcd](https://github.com/anddea/revanced-integrations/commit/605fdcd8ed82f72b795efdd883c356dfc781740c))
* **YouTube Music:** change default value ([cd072a3](https://github.com/anddea/revanced-integrations/commit/cd072a31f2bb8ce0b63d00a1fc9f74e05012a1f1))
* **YouTube Music:** Remove `Replace Cast button` patch ([f887f24](https://github.com/anddea/revanced-integrations/commit/f887f24c029c04f5d7eea9e8486003228b42695d))
* **YouTube/Hide feed components:** separate the `Hide low views video` settings from `Hide recommended videos` settings ([730c58c](https://github.com/anddea/revanced-integrations/commit/730c58ccae8edbba08acf406f5f6ef43beffd5dc))
* **YouTube/Shorts components:** add `Hide Super Thanks button` settings ([6addcae](https://github.com/anddea/revanced-integrations/commit/6addcae7a36c0774365cdffcdae1266ffd234c85))
* **YouTube:** add `Enable OPUS codec` patch ([31578d0](https://github.com/anddea/revanced-integrations/commit/31578d01210a70cc46aeaf6347ac16cd62b47f77))

# [0.153.0-dev.2](https://github.com/anddea/revanced-integrations/compare/v0.153.0-dev.1...v0.153.0-dev.2) (2024-06-05)


### Features

* **GmsCore support:** Add `Don't show again` option for battery optimization dialog ([ecb5836](https://github.com/anddea/revanced-integrations/commit/ecb5836ce209a60cde36c02ada06b237c243971d))

# [0.153.0-dev.1](https://github.com/anddea/revanced-integrations/compare/v0.152.0...v0.153.0-dev.1) (2024-05-31)


### Bug Fixes

* **YouTube - Spoof client:** Player gestures not working when spoofing with `Android VR` client ([067f0ec](https://github.com/anddea/revanced-integrations/commit/067f0ec416c77d98673d736a9b26e0370f0fee62))


### Features

* **YouTube - Spoof client:** Add `Show in Stats for nerds` settings ([6c5ff0c](https://github.com/anddea/revanced-integrations/commit/6c5ff0c1b6176b2c15ca64b42693b518eb004225))
* **YouTube - Spoof client:** Selectively spoof client for general video / livestreams / Shorts / fallback (unplayable video) ([c8e31ec](https://github.com/anddea/revanced-integrations/commit/c8e31ec07069d649aeab9da3c4900bb063270e2c))

# [0.152.0](https://github.com/anddea/revanced-integrations/compare/v0.151.0...v0.152.0) (2024-05-29)


### Bug Fixes

* App crashes when resource not found ([6abcabc](https://github.com/anddea/revanced-integrations/commit/6abcabcd6142b264e93ff31c1740497410a08d8b))
* **YouTube - Append timestamp information:** Ensure correct RTL layout ([027b948](https://github.com/anddea/revanced-integrations/commit/027b9484a82e59b9b375dac6aa612be99d6e25cb))
* **YouTube - Default video quality:** Revert back ([d6da339](https://github.com/anddea/revanced-integrations/commit/d6da33920cab54c7efc893d31b9e2defcc07cc73))
* **YouTube - Hide action buttons:** Some action buttons are not hidden properly ([c37f238](https://github.com/anddea/revanced-integrations/commit/c37f238d9b6baad5b202f192433d0dc773f14b9c))
* **YouTube - Hide feed components:** `Hide mix playlists` setting hides components in channel profile ([dc151ae](https://github.com/anddea/revanced-integrations/commit/dc151ae6d9d1c886f7a746f9f17a92b786e46d13))
* **YouTube - Hide keyword content:** Filter Shorts found in horizontal shelves ([25465e2](https://github.com/anddea/revanced-integrations/commit/25465e24038ed563b88b473c382bc86a210ddf42))
* **YouTube - Hide Shorts components:** Channel headers were hidden if Shorts is disabled ([5451719](https://github.com/anddea/revanced-integrations/commit/54517192a9fd3855544bf79d5ee5c125219cc26d))
* **YouTube - Hide Shorts components:** Exclude `Hide sound metadata label` and `Hide video link label` from `Hide info panel` ([a510788](https://github.com/anddea/revanced-integrations/commit/a510788f68e146acbe05f4e804bb56e9206f99e7))
* **YouTube - Hide Shorts components:** Hide Shorts in search result horizontal shelves ([2be49fc](https://github.com/anddea/revanced-integrations/commit/2be49fca45157e0dd4e91c13be52c6dad6ea5b42))
* **YouTube - Return YouTube Dislike:** Disabling `Show dislikes in Shorts` disabled dislikes everywhere ([#11](https://github.com/anddea/revanced-integrations/issues/11)) ([1b317db](https://github.com/anddea/revanced-integrations/commit/1b317db4aaf5db3bfe43999b28442d4d70b4c08e))
* **YouTube - Return YouTube Dislike:** Dislike counts showing in views ([83bf7b2](https://github.com/anddea/revanced-integrations/commit/83bf7b25cfed359a520f2d81efb9a196072706c1))
* **YouTube - Return YouTube Dislike:** Fix some issues ([281dbe3](https://github.com/anddea/revanced-integrations/commit/281dbe3e54ba4663a6c20756a8cf52b1d027bd27))
* **YouTube - Return YouTube Dislike:** Subscribe icon is hidden ([95b34d4](https://github.com/anddea/revanced-integrations/commit/95b34d463c820fe35d83e6cff0c1ecb252bf9d83))
* **YouTube - Searchbar:** Hide searchbar in RYD and Sponsorblock sections ([#13](https://github.com/anddea/revanced-integrations/issues/13)) ([ed60bea](https://github.com/anddea/revanced-integrations/commit/ed60bead5cde292e7f264a2db9ab7a1b96c52d95))
* **YouTube - Searchbar:** Prevent searchbar appearing briefly when switching between fragments ([#14](https://github.com/anddea/revanced-integrations/issues/14)) ([584e6e3](https://github.com/anddea/revanced-integrations/commit/584e6e38f2344a511fb39b9a6c99d5dba21feca8))
* **YouTube - Searchbar:** Restore original settings page if query is empty ([#12](https://github.com/anddea/revanced-integrations/issues/12)) ([9592bd5](https://github.com/anddea/revanced-integrations/commit/9592bd5449ff5c92f407790f0802da11bb461900))
* **YouTube - Shorts components:** Update pattern for hide disabled comments ([c7b2fd4](https://github.com/anddea/revanced-integrations/commit/c7b2fd43bbd04622f81e40811dca791695c3c896))
* **YouTube - SponsorBlock:** `Submit a new segment` button did not work ([395d3ce](https://github.com/anddea/revanced-integrations/commit/395d3ce4b6c5a1b94e122072e8e5748ea5aac0f9))
* **YouTube - SponsorBlock:** Pressing Now while manually editing a segment results in an error ([9521baa](https://github.com/anddea/revanced-integrations/commit/9521baa34d2a91d964687933cd7f3741504f90fc))
* **YouTube - Spoof format stream data:** Some Uris are not hooked ([7e49ab3](https://github.com/anddea/revanced-integrations/commit/7e49ab3eef85cc0932ac66392422fff57b1fa82c))
* **YouTube - Video playback:** Default video quality sometimes does not apply when video is not loaded ([86b8130](https://github.com/anddea/revanced-integrations/commit/86b8130447e9538e8f1cb72ea0409cddb0b38f5a))
* **YouTube Music/Hide ads:** `Hide fullscreen ads` setting also closes non-ad dialogs https://github.com/inotia00/ReVanced_Extended/issues/1971 ([7dd292d](https://github.com/anddea/revanced-integrations/commit/7dd292d5a1e48965d07f720cc6bff15c3b926d1e))
* **YouTube/Description components:** crash occurs when the title of the engagement panel is null https://github.com/inotia00/ReVanced_Extended/issues/2008 ([e4ba2d0](https://github.com/anddea/revanced-integrations/commit/e4ba2d0e4e5526e0a8604ede55af9ee5242e65cd))
* **YouTube/Hide layout components:** add method to hide settings with whitelist https://github.com/inotia00/ReVanced_Extended/issues/1964 ([10e5d1a](https://github.com/anddea/revanced-integrations/commit/10e5d1a09972227d1bbae33fe7b03d8e19a9bee8))
* **YouTube/Return YouTube Dislike:** dislike count sometimes not shown in Shorts https://github.com/inotia00/ReVanced_Extended/issues/1565 ([f869a37](https://github.com/anddea/revanced-integrations/commit/f869a37b2d06ddf45bf1e9bb4507abfc2f818b38))
* **YouTube/Return YouTube Dislike:** wrong video id is used in shorts https://github.com/inotia00/ReVanced_Extended/issues/1987 ([3868c2b](https://github.com/anddea/revanced-integrations/commit/3868c2b9be5e4d80085ad135fe22bf4f38b6b60d))
* **YouTube/Settings:** settings values of excluded patches have changed due to incorrect settings alignment ([d706d1b](https://github.com/anddea/revanced-integrations/commit/d706d1bafd9b96cb240e82e0c56fd442980920db))
* **YouTube/SponsorBlock:** pressing the fine adjustment buttons skips to the end of the video while creating a new SponsorBlock segment https://github.com/inotia00/ReVanced_Extended/issues/1980 ([f683045](https://github.com/anddea/revanced-integrations/commit/f6830452921f8f38a8045a499ad15735c4858d85))
* **YouTube/Spoof format stream data:** check audio tags first ([75e84a2](https://github.com/anddea/revanced-integrations/commit/75e84a28cda70b24b9ee5227e2b4b862063c241a))
* **YouTube/Spoof format stream data:** incorrect url is used ([2f86f6b](https://github.com/anddea/revanced-integrations/commit/2f86f6b9413f8970ea0cec859f222428352796a7))
* **YouTube/Video playback:** default video quality applies even when video is playing https://github.com/inotia00/ReVanced_Extended/issues/1959 ([5cc2c04](https://github.com/anddea/revanced-integrations/commit/5cc2c042b7b47132641dc64da56e08b8c76e4851))
* **YouTube:** App crashes when `SponsorBlock` patch is excluded ([e657bbd](https://github.com/anddea/revanced-integrations/commit/e657bbdf7338f123ffbb24704d7eca435c95dd8f))
* **YT Music - Hide terms container:** Patch doesn't require restart to apply the changes ([e987b6d](https://github.com/anddea/revanced-integrations/commit/e987b6d7465dd2e821238beec1d998b8b6c742a5))


### Features

* Refactor and match ReVanced and inotia ([30ad38a](https://github.com/anddea/revanced-integrations/commit/30ad38a6d6cac331ede9b515576208dc855853a2))
* **YouTube - Fullscreen components:** `Hide related videos in quick actions` setting has been merged into the `Hide related video overlay` setting ([a9b973b](https://github.com/anddea/revanced-integrations/commit/a9b973bb59bcc3daacd8174aee2cd505e20af233))
* **YouTube - Hide layout components:** Add `Hide videos based on duration` ([b6bd93a](https://github.com/anddea/revanced-integrations/commit/b6bd93a1e9bf8473dadc78a867558f82ceac26e7))
* **YouTube - Hide layout components:** Add an option to hide videos with views greater than specified value ([14d4756](https://github.com/anddea/revanced-integrations/commit/14d475637680a913a8b706cc92785b2abb857d4f))
* **YouTube - Hide shorts components:** Add separate option to hide in player ([67cfeb8](https://github.com/anddea/revanced-integrations/commit/67cfeb88c0c125523524cf96a17b2da6a1f031da))
* **YouTube - Hide shorts components:** Hide comments button if disabled or with label "0" ([44f3c2f](https://github.com/anddea/revanced-integrations/commit/44f3c2f0349a8ea294471c9bc8dc88f963159d2c))
* **YouTube - Overlay buttons:** Add `Whitelist` overlay button ([564a044](https://github.com/anddea/revanced-integrations/commit/564a0443b85807cab156c3fafea804c560c20498))
* **YouTube - Player components:** Add setting toggle for `Hide suggested video end screen` ([d020bc8](https://github.com/anddea/revanced-integrations/commit/d020bc8dd987683f75002095bbbc403146371d9e))
* **YouTube - Settings:** Add ability to search in summaries and list entries ([fbe9e53](https://github.com/anddea/revanced-integrations/commit/fbe9e53aff8aeb13c9307e14b0cbce6aaf465282))
* **YouTube - Settings:** Add search bar for settings ([681cea9](https://github.com/anddea/revanced-integrations/commit/681cea9aa1d1fdf0c88c7a240d2bf92a053782e7))
* **YouTube - Toolbar components:** Remove `Hide trending searches` setting ([b14f20c](https://github.com/anddea/revanced-integrations/commit/b14f20cd05b89500b22adba980a3329003c4afb3))
* **YouTube Music - Player components:** Add `Enable black player background` setting ([d27d362](https://github.com/anddea/revanced-integrations/commit/d27d362423f5d3d8312ba33dd1c96804aa670ded))
* **YouTube Music - Player components:** Add `Hide audio video switch toggle` setting ([68238da](https://github.com/anddea/revanced-integrations/commit/68238dad79efacfe286a0e56413217e64d2e7fb1))
* **YouTube/Spoof format stream data:** check endpoint url is non-null ([a25d7d7](https://github.com/anddea/revanced-integrations/commit/a25d7d7dfebb3f0e4cccd253528334ba5f207641))
* **YouTube/Spoof format stream data:** improve hook method, fetch to `ANDROID_TESTSUITE` client ([ef50cf5](https://github.com/anddea/revanced-integrations/commit/ef50cf55416b7052fe27467950cc18921d5de218))
* **YouTube:** Add `Hide videos by duration` and `Hide videos by views count` greater than specified value ([#37](https://github.com/anddea/revanced-integrations/issues/37)) ([d15028c](https://github.com/anddea/revanced-integrations/commit/d15028cdabf11093f8628938f3ffc22fa7ef809e))
* **YouTube:** Add `Spoof client` patch ([010f879](https://github.com/anddea/revanced-integrations/commit/010f87917dec41caefff7d1123c527829e24f243))
* **YouTube:** Add support versions `19.19.39` - `19.20.34` ([3c4ccc3](https://github.com/anddea/revanced-integrations/commit/3c4ccc318163b3405a38f1e464158e40d017d8e7))
* **YouTube:** Hide live chat replay in fullscreen ([e8184a1](https://github.com/anddea/revanced-integrations/commit/e8184a1a1b200d8c759abd588ed664dbf0db9b54))
* **YouTube:** Remove `Spoof test client` patch ([782b25b](https://github.com/anddea/revanced-integrations/commit/782b25b1fbc4e1b08d3150d388cf18afd11d9d1b))


### Reverts

* default landscape mode timeout ([f46db5d](https://github.com/anddea/revanced-integrations/commit/f46db5da6ac9ada68ed92b03e434a686b07d27a3))

# [0.152.0-dev.14](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.13...v0.152.0-dev.14) (2024-05-28)


### Bug Fixes

* **YouTube - SponsorBlock:** `Submit a new segment` button did not work ([395d3ce](https://github.com/anddea/revanced-integrations/commit/395d3ce4b6c5a1b94e122072e8e5748ea5aac0f9))


### Features

* **YouTube - Player components:** Add setting toggle for `Hide suggested video end screen` ([d020bc8](https://github.com/anddea/revanced-integrations/commit/d020bc8dd987683f75002095bbbc403146371d9e))
* **YouTube Music - Player components:** Add `Enable black player background` setting ([d27d362](https://github.com/anddea/revanced-integrations/commit/d27d362423f5d3d8312ba33dd1c96804aa670ded))

# [0.152.0-dev.13](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.12...v0.152.0-dev.13) (2024-05-27)


### Bug Fixes

* App crashes when resource not found ([6abcabc](https://github.com/anddea/revanced-integrations/commit/6abcabcd6142b264e93ff31c1740497410a08d8b))
* **YouTube - Return YouTube Dislike:** Dislike counts showing in views ([83bf7b2](https://github.com/anddea/revanced-integrations/commit/83bf7b25cfed359a520f2d81efb9a196072706c1))
* **YouTube - Return YouTube Dislike:** Subscribe icon is hidden ([95b34d4](https://github.com/anddea/revanced-integrations/commit/95b34d463c820fe35d83e6cff0c1ecb252bf9d83))
* **YouTube - SponsorBlock:** Pressing Now while manually editing a segment results in an error ([9521baa](https://github.com/anddea/revanced-integrations/commit/9521baa34d2a91d964687933cd7f3741504f90fc))
* **YouTube - Video playback:** Default video quality sometimes does not apply when video is not loaded ([86b8130](https://github.com/anddea/revanced-integrations/commit/86b8130447e9538e8f1cb72ea0409cddb0b38f5a))
* **YouTube:** App crashes when `SponsorBlock` patch is excluded ([e657bbd](https://github.com/anddea/revanced-integrations/commit/e657bbdf7338f123ffbb24704d7eca435c95dd8f))


### Features

* **YouTube - Fullscreen components:** `Hide related videos in quick actions` setting has been merged into the `Hide related video overlay` setting ([a9b973b](https://github.com/anddea/revanced-integrations/commit/a9b973bb59bcc3daacd8174aee2cd505e20af233))
* **YouTube - Overlay buttons:** Add `Whitelist` overlay button ([564a044](https://github.com/anddea/revanced-integrations/commit/564a0443b85807cab156c3fafea804c560c20498))
* **YouTube - Toolbar components:** Remove `Hide trending searches` setting ([b14f20c](https://github.com/anddea/revanced-integrations/commit/b14f20cd05b89500b22adba980a3329003c4afb3))
* **YouTube:** Add `Spoof client` patch ([010f879](https://github.com/anddea/revanced-integrations/commit/010f87917dec41caefff7d1123c527829e24f243))
* **YouTube:** Add support versions `19.19.39` - `19.20.34` ([3c4ccc3](https://github.com/anddea/revanced-integrations/commit/3c4ccc318163b3405a38f1e464158e40d017d8e7))
* **YouTube:** Remove `Spoof test client` patch ([782b25b](https://github.com/anddea/revanced-integrations/commit/782b25b1fbc4e1b08d3150d388cf18afd11d9d1b))

# [0.152.0-dev.12](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.11...v0.152.0-dev.12) (2024-05-21)


### Bug Fixes

* **YouTube - Searchbar:** Hide searchbar in RYD and Sponsorblock sections ([#13](https://github.com/anddea/revanced-integrations/issues/13)) ([ed60bea](https://github.com/anddea/revanced-integrations/commit/ed60bead5cde292e7f264a2db9ab7a1b96c52d95))
* **YouTube - Searchbar:** Prevent searchbar appearing briefly when switching between fragments ([#14](https://github.com/anddea/revanced-integrations/issues/14)) ([584e6e3](https://github.com/anddea/revanced-integrations/commit/584e6e38f2344a511fb39b9a6c99d5dba21feca8))
* **YouTube - Searchbar:** Restore original settings page if query is empty ([#12](https://github.com/anddea/revanced-integrations/issues/12)) ([9592bd5](https://github.com/anddea/revanced-integrations/commit/9592bd5449ff5c92f407790f0802da11bb461900))


### Features

* **YouTube - Settings:** Add ability to search in summaries and list entries ([fbe9e53](https://github.com/anddea/revanced-integrations/commit/fbe9e53aff8aeb13c9307e14b0cbce6aaf465282))

# [0.152.0-dev.11](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.10...v0.152.0-dev.11) (2024-05-20)


### Features

* **YouTube - Settings:** Add search bar for settings ([681cea9](https://github.com/anddea/revanced-integrations/commit/681cea9aa1d1fdf0c88c7a240d2bf92a053782e7))


### Reverts

* default landscape mode timeout ([f46db5d](https://github.com/anddea/revanced-integrations/commit/f46db5da6ac9ada68ed92b03e434a686b07d27a3))

# [0.152.0-dev.10](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.9...v0.152.0-dev.10) (2024-05-16)


### Bug Fixes

* **YouTube - Shorts components:** Update pattern for hide disabled comments ([c7b2fd4](https://github.com/anddea/revanced-integrations/commit/c7b2fd43bbd04622f81e40811dca791695c3c896))

# [0.152.0-dev.9](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.8...v0.152.0-dev.9) (2024-05-16)


### Bug Fixes

* **YouTube - Hide action buttons:** Some action buttons are not hidden properly ([c37f238](https://github.com/anddea/revanced-integrations/commit/c37f238d9b6baad5b202f192433d0dc773f14b9c))
* **YouTube - Hide feed components:** `Hide mix playlists` setting hides components in channel profile ([dc151ae](https://github.com/anddea/revanced-integrations/commit/dc151ae6d9d1c886f7a746f9f17a92b786e46d13))
* **YouTube - Spoof format stream data:** Some Uris are not hooked ([7e49ab3](https://github.com/anddea/revanced-integrations/commit/7e49ab3eef85cc0932ac66392422fff57b1fa82c))


### Features

* **YouTube Music - Player components:** Add `Hide audio video switch toggle` setting ([68238da](https://github.com/anddea/revanced-integrations/commit/68238dad79efacfe286a0e56413217e64d2e7fb1))

# [0.152.0-dev.8](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.7...v0.152.0-dev.8) (2024-05-16)


### Bug Fixes

* **YouTube Music/Hide ads:** `Hide fullscreen ads` setting also closes non-ad dialogs https://github.com/inotia00/ReVanced_Extended/issues/1971 ([7dd292d](https://github.com/anddea/revanced-integrations/commit/7dd292d5a1e48965d07f720cc6bff15c3b926d1e))
* **YouTube/Description components:** crash occurs when the title of the engagement panel is null https://github.com/inotia00/ReVanced_Extended/issues/2008 ([e4ba2d0](https://github.com/anddea/revanced-integrations/commit/e4ba2d0e4e5526e0a8604ede55af9ee5242e65cd))
* **YouTube/Hide layout components:** add method to hide settings with whitelist https://github.com/inotia00/ReVanced_Extended/issues/1964 ([10e5d1a](https://github.com/anddea/revanced-integrations/commit/10e5d1a09972227d1bbae33fe7b03d8e19a9bee8))
* **YouTube/Return YouTube Dislike:** dislike count sometimes not shown in Shorts https://github.com/inotia00/ReVanced_Extended/issues/1565 ([f869a37](https://github.com/anddea/revanced-integrations/commit/f869a37b2d06ddf45bf1e9bb4507abfc2f818b38))
* **YouTube/Return YouTube Dislike:** wrong video id is used in shorts https://github.com/inotia00/ReVanced_Extended/issues/1987 ([3868c2b](https://github.com/anddea/revanced-integrations/commit/3868c2b9be5e4d80085ad135fe22bf4f38b6b60d))
* **YouTube/Settings:** settings values of excluded patches have changed due to incorrect settings alignment ([d706d1b](https://github.com/anddea/revanced-integrations/commit/d706d1bafd9b96cb240e82e0c56fd442980920db))
* **YouTube/SponsorBlock:** pressing the fine adjustment buttons skips to the end of the video while creating a new SponsorBlock segment https://github.com/inotia00/ReVanced_Extended/issues/1980 ([f683045](https://github.com/anddea/revanced-integrations/commit/f6830452921f8f38a8045a499ad15735c4858d85))
* **YouTube/Spoof format stream data:** check audio tags first ([75e84a2](https://github.com/anddea/revanced-integrations/commit/75e84a28cda70b24b9ee5227e2b4b862063c241a))
* **YouTube/Spoof format stream data:** incorrect url is used ([2f86f6b](https://github.com/anddea/revanced-integrations/commit/2f86f6b9413f8970ea0cec859f222428352796a7))
* **YouTube/Video playback:** default video quality applies even when video is playing https://github.com/inotia00/ReVanced_Extended/issues/1959 ([5cc2c04](https://github.com/anddea/revanced-integrations/commit/5cc2c042b7b47132641dc64da56e08b8c76e4851))


### Features

* **YouTube/Spoof format stream data:** check endpoint url is non-null ([a25d7d7](https://github.com/anddea/revanced-integrations/commit/a25d7d7dfebb3f0e4cccd253528334ba5f207641))
* **YouTube/Spoof format stream data:** improve hook method, fetch to `ANDROID_TESTSUITE` client ([ef50cf5](https://github.com/anddea/revanced-integrations/commit/ef50cf55416b7052fe27467950cc18921d5de218))
* **YouTube:** Add `Hide videos by duration` and `Hide videos by views count` greater than specified value ([#37](https://github.com/anddea/revanced-integrations/issues/37)) ([d15028c](https://github.com/anddea/revanced-integrations/commit/d15028cdabf11093f8628938f3ffc22fa7ef809e))

# [0.152.0-dev.7](https://github.com/anddea/revanced-integrations/compare/v0.152.0-dev.6...v0.152.0-dev.7) (2024-05-12)


### Features

* Refactor and match ReVanced and inotia ([30ad38a](https://github.com/anddea/revanced-integrations/commit/30ad38a6d6cac331ede9b515576208dc855853a2))
