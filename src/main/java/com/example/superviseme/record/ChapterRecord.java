package com.example.superviseme.record;

import java.util.UUID;

public record ChapterRecord(
		UUID id,
		String name,
		int stage,
		UUID nextChapterId
		) { }
