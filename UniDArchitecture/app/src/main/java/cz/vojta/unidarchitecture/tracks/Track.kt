package cz.vojta.unidarchitecture.tracks

data class Track(val reposts_count: Int,
                 val comment_count: Int,
                 val favoritings_count: Int,
                 val download_count: Int,
                 val playback_count: Int,
                 val stream_url: String?,
                 val waveform_url: String?,
                 val artwork_url: String?,
                 val permalink_url: String?,
                 val attachments_uri: String?,
                 val user: User,
                 val uri: String?,
                 val license: String?,
                 val purchase_url: String?,
                 val downloadable: Boolean,
                 val embeddable_by: String?,
                 val streamable: Boolean,
                 val permalink: String?,
                 val tag_list: String?,
                 val sharing: String?,
                 val last_modified: String?,
                 val kind: String?,
                 val id: Long,
                 val created_at: String?,
                 val user_id: Long,
                 val duration: Long,
                 val commentable: Boolean,
                 val state: String?,
                 val original_content_size: Long?,
                 val label_id: String?,
                 val purchase_title: String?,
                 val genre:String?,
                 val title: String?,
                 val description:String?,
                 val label_name: String?,
                 val release: String?,
                 val track_type: String?,
                 val key_signature: String?,
                 val isrc: String?,
                 val video_url: String?,
                 val bpm: String?,
                 val release_year: String?,
                 val release_month: String?,
                 val release_day: String?,
                 val original_format: String?)

