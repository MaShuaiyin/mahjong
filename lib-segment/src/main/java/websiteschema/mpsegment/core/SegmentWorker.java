package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.filter.SegmentResultFilter;

public class SegmentWorker {

    private SegmentResultFilter unKnownFilter;
    private int maxSegStrLength;
    private MPSegment mpSegment;
    private boolean recognizePOS = true;

    public SegmentWorker(MPSegmentConfiguration config) {
        unKnownFilter = null;
        maxSegStrLength = 400000;
        mpSegment = new MPSegment(config);
        unKnownFilter = new SegmentResultFilter(config);
    }

    public void setUseDomainDictionary(boolean flag) {
        mpSegment.setUseDomainDictionary(flag);
    }

    public boolean isUseDomainDictionary() {
        return mpSegment.isUseDomainDictionary();
    }

    public SegmentResult segment(String sentence) {
        SegmentResult result = null;
        if (sentence != null && sentence.length() > 0) {
            if (sentence.length() > maxSegStrLength) {
                sentence = sentence.substring(0, maxSegStrLength);
            }
            result = mpSegment.segmentMP(sentence, recognizePOS);
            if (recognizePOS) {
                unKnownFilter.filter(result);
            }
        } else {
            result = new SegmentResult(0);
        }
        return result;
    }

    public boolean isUseContextFreqSegment() {
        return mpSegment.isUseContextFreqSegment();
    }

    public void setUseContextFreqSegment(boolean useContextFreqSegment) {
        mpSegment.setUseContextFreqSegment(useContextFreqSegment);
    }

    public boolean isRecognizePOS() {
        return recognizePOS;
    }

    public void setRecognizePOS(boolean recognizePOS) {
        this.recognizePOS = recognizePOS;
    }
}
