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
