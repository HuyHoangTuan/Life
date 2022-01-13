package com.example.Life.engine.search.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentScore implements Comparable<ContentScore>
{
    long contentId;
    double score;

    public ContentScore(long contentId, double score)
    {
        this.contentId =contentId;
        this.score =score;
    }
    @Override
    public int compareTo(ContentScore o)
    {
            return (int)(this.score-o.score);
    }

}
