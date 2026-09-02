package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import com.google.common.base.Ascii;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.internal.url._UrlKt;
import org.webrtc.MediaStreamTrack;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DashManifestParser extends DefaultHandler implements ParsingLoadable.Parser {
    private final XmlPullParserFactory xmlParserFactory;
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
    private static final int[] MPEG_CHANNEL_CONFIGURATION_MAPPING = {-1, 1, 2, 3, 4, 5, 6, 8, 2, 3, 4, 7, 8, 24, 8, 12, 10, 12, 14, 12, 14};

    private static long getFinalAvailabilityTimeOffset(long j, long j2) {
        if (j2 != -9223372036854775807L) {
            j = j2;
        }
        if (j == Long.MAX_VALUE) {
            return -9223372036854775807L;
        }
        return j;
    }

    public DashManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public DashManifest parse(Uri uri, InputStream inputStream) throws ParserException {
        try {
            XmlPullParser xmlPullParserNewPullParser = this.xmlParserFactory.newPullParser();
            xmlPullParserNewPullParser.setInput(inputStream, null);
            if (xmlPullParserNewPullParser.next() != 2 || !"MPD".equals(xmlPullParserNewPullParser.getName())) {
                throw ParserException.createForMalformedManifest("inputStream does not contain a valid media presentation description", null);
            }
            return parseMediaPresentationDescription(xmlPullParserNewPullParser, uri);
        } catch (XmlPullParserException e) {
            throw ParserException.createForMalformedManifest(null, e);
        }
    }

    /* JADX WARN: Code duplicated, block: B:67:0x01a0  */
    /* JADX WARN: Code duplicated, block: B:69:0x01a4  */
    /* JADX WARN: Code duplicated, block: B:70:0x01a7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:71:0x01a9  */
    /* JADX WARN: Code duplicated, block: B:72:0x01ab  */
    /* JADX WARN: Code duplicated, block: B:77:0x01bb  */
    /* JADX WARN: Code duplicated, block: B:79:0x01d8  */
    /* JADX WARN: Code duplicated, block: B:81:0x01e0 A[LOOP:0: B:24:0x00a5->B:81:0x01e0, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:82:0x019c A[SYNTHETIC] */
    protected DashManifest parseMediaPresentationDescription(XmlPullParser xmlPullParser, Uri uri) throws XmlPullParserException, IOException {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        long j;
        ArrayList arrayList5;
        long j2;
        Throwable th;
        long j3;
        DashManifestParser dashManifestParser = this;
        boolean zIsDvbProfileDeclared = dashManifestParser.isDvbProfileDeclared(dashManifestParser.parseProfiles(xmlPullParser, "profiles", new String[0]));
        long dateTime = parseDateTime(xmlPullParser, "availabilityStartTime", -9223372036854775807L);
        long duration = parseDuration(xmlPullParser, "mediaPresentationDuration", -9223372036854775807L);
        long duration2 = parseDuration(xmlPullParser, "minBufferTime", -9223372036854775807L);
        boolean zEquals = "dynamic".equals(xmlPullParser.getAttributeValue(null, "type"));
        long duration3 = zEquals ? parseDuration(xmlPullParser, "minimumUpdatePeriod", -9223372036854775807L) : -9223372036854775807L;
        long duration4 = zEquals ? parseDuration(xmlPullParser, "timeShiftBufferDepth", -9223372036854775807L) : -9223372036854775807L;
        long duration5 = zEquals ? parseDuration(xmlPullParser, "suggestedPresentationDelay", -9223372036854775807L) : -9223372036854775807L;
        long dateTime2 = parseDateTime(xmlPullParser, "publishTime", -9223372036854775807L);
        long j4 = zEquals ? 0L : -9223372036854775807L;
        boolean z = false;
        ArrayList arrayListNewArrayList = Lists.newArrayList(new BaseUrl(uri.toString(), uri.toString(), zIsDvbProfileDeclared ? 1 : Integer.MIN_VALUE, 1));
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        long j5 = zEquals ? -9223372036854775807L : 0L;
        ArrayList arrayList8 = arrayListNewArrayList;
        long availabilityTimeOffsetUs = j4;
        boolean z2 = false;
        ProgramInformation programInformation = null;
        UtcTimingElement utcTiming = null;
        Uri uriResolveToUri = null;
        ServiceDescriptionElement serviceDescription = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z) {
                    availabilityTimeOffsetUs = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser, availabilityTimeOffsetUs);
                    z = true;
                }
                arrayList = arrayList8;
                arrayList2 = arrayList6;
                arrayList7.addAll(dashManifestParser.parseBaseUrl(xmlPullParser, arrayList, zIsDvbProfileDeclared));
            } else {
                arrayList = arrayList8;
                arrayList2 = arrayList6;
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "ProgramInformation")) {
                    programInformation = parseProgramInformation(xmlPullParser);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "UTCTiming")) {
                    utcTiming = parseUtcTiming(xmlPullParser);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Location")) {
                    uriResolveToUri = UriUtil.resolveToUri(uri.toString(), xmlPullParser.nextText());
                } else {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser, "ServiceDescription")) {
                        serviceDescription = parseServiceDescription(xmlPullParser);
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Period") && !z2) {
                        if (arrayList7.isEmpty()) {
                            arrayList3 = arrayList7;
                            arrayList5 = arrayList;
                            arrayList4 = arrayList2;
                            arrayList8 = arrayList5;
                        } else {
                            arrayList5 = arrayList7;
                            arrayList3 = arrayList5;
                            arrayList8 = arrayList;
                            arrayList4 = arrayList2;
                        }
                        Pair period = parsePeriod(xmlPullParser, arrayList5, j5, availabilityTimeOffsetUs, dateTime, duration4, zIsDvbProfileDeclared);
                        Period period2 = (Period) period.first;
                        if (period2.startMs != -9223372036854775807L) {
                            long jLongValue = ((Long) period.second).longValue();
                            j2 = jLongValue == -9223372036854775807L ? -9223372036854775807L : period2.startMs + jLongValue;
                            arrayList4.add(period2);
                        } else {
                            if (!zEquals) {
                                throw ParserException.createForMalformedManifest("Unable to determine start of period " + arrayList4.size(), null);
                            }
                            j2 = j5;
                            z2 = true;
                        }
                        j = availabilityTimeOffsetUs;
                        j5 = j2;
                    } else {
                        arrayList3 = arrayList7;
                        arrayList8 = arrayList;
                        long j6 = j5;
                        long j7 = availabilityTimeOffsetUs;
                        arrayList4 = arrayList2;
                        maybeSkipTag(xmlPullParser);
                        j5 = j6;
                        j = j7;
                    }
                    if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                        if (duration != -9223372036854775807L) {
                            th = null;
                            j3 = duration;
                        } else if (j5 != -9223372036854775807L) {
                            j3 = j5;
                            th = null;
                        } else {
                            if (!zEquals) {
                                throw ParserException.createForMalformedManifest("Unable to determine duration of static manifest.", null);
                            }
                            th = null;
                            j3 = duration;
                        }
                        if (!arrayList4.isEmpty()) {
                            throw ParserException.createForMalformedManifest("No periods found.", th);
                        }
                        return buildMediaPresentationDescription(dateTime, j3, duration2, zEquals, duration3, duration4, duration5, dateTime2, programInformation, utcTiming, serviceDescription, uriResolveToUri, arrayList4);
                    }
                    long j8 = duration4;
                    long j9 = duration2;
                    ArrayList arrayList9 = arrayList4;
                    dashManifestParser = this;
                    zEquals = zEquals;
                    duration3 = duration3;
                    availabilityTimeOffsetUs = j;
                    arrayList6 = arrayList9;
                    arrayList7 = arrayList3;
                    duration2 = j9;
                    duration4 = j8;
                    duration = duration;
                }
            }
            arrayList3 = arrayList7;
            j = availabilityTimeOffsetUs;
            arrayList4 = arrayList2;
            arrayList8 = arrayList;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                if (duration != -9223372036854775807L) {
                    th = null;
                    j3 = duration;
                } else if (j5 != -9223372036854775807L) {
                    j3 = j5;
                    th = null;
                } else {
                    if (!zEquals) {
                        throw ParserException.createForMalformedManifest("Unable to determine duration of static manifest.", null);
                    }
                    th = null;
                    j3 = duration;
                }
                if (!arrayList4.isEmpty()) {
                    throw ParserException.createForMalformedManifest("No periods found.", th);
                }
                return buildMediaPresentationDescription(dateTime, j3, duration2, zEquals, duration3, duration4, duration5, dateTime2, programInformation, utcTiming, serviceDescription, uriResolveToUri, arrayList4);
            }
            long j10 = duration4;
            long j11 = duration2;
            ArrayList arrayList10 = arrayList4;
            dashManifestParser = this;
            zEquals = zEquals;
            duration3 = duration3;
            availabilityTimeOffsetUs = j;
            arrayList6 = arrayList10;
            arrayList7 = arrayList3;
            duration2 = j11;
            duration4 = j10;
            duration = duration;
        }
    }

    protected DashManifest buildMediaPresentationDescription(long j, long j2, long j3, boolean z, long j4, long j5, long j6, long j7, ProgramInformation programInformation, UtcTimingElement utcTimingElement, ServiceDescriptionElement serviceDescriptionElement, Uri uri, List list) {
        return new DashManifest(j, j2, j3, z, j4, j5, j6, j7, programInformation, utcTimingElement, serviceDescriptionElement, uri, list);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser xmlPullParser) {
        return buildUtcTimingElement(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, "value"));
    }

    protected UtcTimingElement buildUtcTimingElement(String str, String str2) {
        return new UtcTimingElement(str, str2);
    }

    protected ServiceDescriptionElement parseServiceDescription(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        long j = -9223372036854775807L;
        long j2 = -9223372036854775807L;
        long j3 = -9223372036854775807L;
        float f = -3.4028235E38f;
        float f2 = -3.4028235E38f;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Latency")) {
                j = parseLong(xmlPullParser, "target", -9223372036854775807L);
                j2 = parseLong(xmlPullParser, "min", -9223372036854775807L);
                j3 = parseLong(xmlPullParser, "max", -9223372036854775807L);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "PlaybackRate")) {
                f = parseFloat(xmlPullParser, "min", -3.4028235E38f);
                f2 = parseFloat(xmlPullParser, "max", -3.4028235E38f);
            }
            long j4 = j;
            long j5 = j2;
            long j6 = j3;
            float f3 = f;
            float f4 = f2;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "ServiceDescription")) {
                return new ServiceDescriptionElement(j4, j5, j6, f3, f4);
            }
            j = j4;
            j2 = j5;
            j3 = j6;
            f = f3;
            f2 = f4;
        }
    }

    protected Pair parsePeriod(XmlPullParser xmlPullParser, List list, long j, long j2, long j3, long j4, boolean z) throws XmlPullParserException, IOException {
        long availabilityTimeOffsetUs;
        ArrayList arrayList;
        ArrayList arrayList2;
        long j5;
        Object obj;
        long j6;
        SegmentBase segmentTemplate;
        long j7;
        long j8;
        SegmentBase segmentBase;
        ArrayList arrayList3;
        boolean z2;
        long j9;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        List list2;
        ArrayList arrayList7;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "id");
        long duration = parseDuration(xmlPullParser2, "start", j);
        long j10 = j3 != -9223372036854775807L ? j3 + duration : -9223372036854775807L;
        long duration2 = parseDuration(xmlPullParser2, "duration", -9223372036854775807L);
        ArrayList arrayList8 = new ArrayList();
        long j11 = -9223372036854775807L;
        ArrayList arrayList9 = new ArrayList();
        boolean z3 = false;
        Descriptor descriptor = null;
        long availabilityTimeOffsetUs2 = j2;
        ArrayList arrayList10 = arrayList8;
        SegmentBase segmentBase2 = null;
        long j12 = -9223372036854775807L;
        ArrayList arrayList11 = new ArrayList();
        while (true) {
            xmlPullParser2.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (!z3) {
                    availabilityTimeOffsetUs2 = parseAvailabilityTimeOffsetUs(xmlPullParser2, availabilityTimeOffsetUs2);
                    z3 = true;
                }
                arrayList11.addAll(parseBaseUrl(xmlPullParser2, list, z));
                arrayList2 = arrayList10;
                j7 = availabilityTimeOffsetUs2;
                arrayList = arrayList9;
                z2 = z3;
                j5 = j11;
                obj = null;
                duration2 = duration2;
                arrayList7 = arrayList11;
                descriptor = descriptor;
                xmlPullParser2 = xmlPullParser2;
                segmentBase = segmentBase2;
                j9 = j12;
                j10 = j10;
            } else {
                ArrayList arrayList12 = arrayList10;
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AdaptationSet")) {
                    if (arrayList11.isEmpty()) {
                        arrayList6 = arrayList11;
                        list2 = list;
                    } else {
                        ArrayList arrayList13 = arrayList11;
                        arrayList6 = arrayList13;
                        list2 = arrayList13;
                    }
                    ArrayList arrayList14 = arrayList9;
                    long j13 = availabilityTimeOffsetUs2;
                    long j14 = j10;
                    long j15 = duration2;
                    AdaptationSet adaptationSet = parseAdaptationSet(xmlPullParser2, list2, segmentBase2, j15, j13, j12, j14, j4, z);
                    j10 = j14;
                    availabilityTimeOffsetUs = j12;
                    arrayList12.add(adaptationSet);
                    duration2 = j15;
                    arrayList = arrayList14;
                    obj = null;
                    j6 = j13;
                    arrayList2 = arrayList12;
                    j5 = -9223372036854775807L;
                    arrayList5 = arrayList6;
                } else {
                    ArrayList arrayList15 = arrayList11;
                    ArrayList arrayList16 = arrayList9;
                    long j16 = availabilityTimeOffsetUs2;
                    availabilityTimeOffsetUs = j12;
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "EventStream")) {
                        arrayList16.add(parseEventStream(xmlPullParser));
                        duration2 = duration2;
                        arrayList = arrayList16;
                        arrayList2 = arrayList12;
                        j5 = -9223372036854775807L;
                        obj = null;
                        j6 = j16;
                        arrayList5 = arrayList15;
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentBase")) {
                            segmentTemplate = parseSegmentBase(xmlPullParser2, null);
                            j7 = j16;
                            obj = null;
                            arrayList = arrayList16;
                            arrayList2 = arrayList12;
                            j5 = -9223372036854775807L;
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentList")) {
                            long j17 = j10;
                            long j18 = duration2;
                            long availabilityTimeOffsetUs3 = parseAvailabilityTimeOffsetUs(xmlPullParser2, -9223372036854775807L);
                            arrayList = arrayList16;
                            arrayList2 = arrayList12;
                            j5 = -9223372036854775807L;
                            SegmentBase.SegmentList segmentList = parseSegmentList(xmlPullParser2, null, j17, j18, j16, availabilityTimeOffsetUs3, j4);
                            j7 = j16;
                            obj = null;
                            j10 = j17;
                            duration2 = j18;
                            descriptor = descriptor;
                            xmlPullParser2 = xmlPullParser2;
                            j8 = availabilityTimeOffsetUs3;
                            segmentBase = segmentList;
                            arrayList3 = arrayList15;
                        } else {
                            segmentBase2 = segmentBase2;
                            arrayList = arrayList16;
                            arrayList2 = arrayList12;
                            j5 = -9223372036854775807L;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                                availabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser2, -9223372036854775807L);
                                obj = null;
                                segmentTemplate = parseSegmentTemplate(xmlPullParser2, null, ImmutableList.of(), j10, duration2, j16, availabilityTimeOffsetUs, j4);
                                j7 = j16;
                            } else {
                                obj = null;
                                duration2 = duration2;
                                xmlPullParser2 = xmlPullParser2;
                                j6 = j16;
                                j10 = j10;
                                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AssetIdentifier")) {
                                    descriptor = parseDescriptor(xmlPullParser2, "AssetIdentifier");
                                    arrayList4 = arrayList15;
                                } else {
                                    maybeSkipTag(xmlPullParser2);
                                    arrayList4 = arrayList15;
                                }
                                j7 = j6;
                                descriptor = descriptor;
                                j8 = availabilityTimeOffsetUs;
                                segmentBase = segmentBase2;
                                arrayList3 = arrayList4;
                            }
                        }
                        j8 = availabilityTimeOffsetUs;
                        segmentBase = segmentTemplate;
                        arrayList3 = arrayList15;
                    }
                    z2 = z3;
                    j9 = j8;
                    arrayList7 = arrayList3;
                }
                j10 = j10;
                arrayList4 = arrayList5;
                j7 = j6;
                descriptor = descriptor;
                j8 = availabilityTimeOffsetUs;
                segmentBase = segmentBase2;
                arrayList3 = arrayList4;
                z2 = z3;
                j9 = j8;
                arrayList7 = arrayList3;
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Period")) {
                return Pair.create(buildPeriod(attributeValue, duration, arrayList2, arrayList, descriptor), Long.valueOf(duration2));
            }
            xmlPullParser2 = xmlPullParser2;
            j10 = j10;
            segmentBase2 = segmentBase;
            j12 = j9;
            arrayList11 = arrayList7;
            arrayList10 = arrayList2;
            descriptor = descriptor;
            z3 = z2;
            duration2 = duration2;
            j11 = j5;
            availabilityTimeOffsetUs2 = j7;
            arrayList9 = arrayList;
        }
    }

    protected Period buildPeriod(String str, long j, List list, List list2, Descriptor descriptor) {
        return new Period(str, j, list, list2, descriptor);
    }

    /* JADX WARN: Code duplicated, block: B:74:0x0369 A[LOOP:0: B:3:0x0090->B:74:0x0369, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:75:0x0323 A[EDGE_INSN: B:75:0x0323->B:68:0x0323 BREAK  A[LOOP:0: B:3:0x0090->B:74:0x0369], SYNTHETIC] */
    protected AdaptationSet parseAdaptationSet(XmlPullParser xmlPullParser, List list, SegmentBase segmentBase, long j, long j2, long j3, long j4, long j5, boolean z) throws XmlPullParserException, IOException {
        String str;
        SegmentBase segmentBase2;
        long j6;
        long j7;
        float f;
        ArrayList arrayList;
        int i;
        ArrayList arrayList2;
        int i2;
        ArrayList arrayList3;
        int i3;
        ArrayList arrayList4;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        int i4 = parseInt(xmlPullParser2, "id", -1);
        int contentType = parseContentType(xmlPullParser);
        String attributeValue = xmlPullParser2.getAttributeValue(null, "mimeType");
        String attributeValue2 = xmlPullParser2.getAttributeValue(null, "codecs");
        int i5 = parseInt(xmlPullParser2, "width", -1);
        int i6 = parseInt(xmlPullParser2, "height", -1);
        float frameRate = parseFrameRate(xmlPullParser2, -1.0f);
        int i7 = parseInt(xmlPullParser2, "audioSamplingRate", -1);
        String str2 = "lang";
        String attributeValue3 = xmlPullParser2.getAttributeValue(null, "lang");
        String attributeValue4 = xmlPullParser2.getAttributeValue(null, "label");
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        ArrayList arrayList10 = arrayList6;
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        String str3 = attributeValue2;
        int iCheckContentTypeConsistency = contentType;
        long j8 = j3;
        ArrayList arrayList14 = arrayList12;
        int i8 = i5;
        int i9 = i6;
        float f2 = frameRate;
        String label = attributeValue4;
        String str4 = attributeValue3;
        boolean z2 = false;
        int audioChannelConfiguration = -1;
        String str5 = null;
        SegmentBase segmentTemplate = segmentBase;
        String str6 = attributeValue;
        long availabilityTimeOffsetUs = j2;
        while (true) {
            xmlPullParser2.next();
            float f3 = f2;
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (!z2) {
                    availabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser2, availabilityTimeOffsetUs);
                    z2 = true;
                }
                arrayList13.addAll(parseBaseUrl(xmlPullParser2, list, z));
                availabilityTimeOffsetUs = availabilityTimeOffsetUs;
            } else {
                long j9 = availabilityTimeOffsetUs;
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentProtection")) {
                    Pair contentProtection = parseContentProtection(xmlPullParser);
                    Object obj = contentProtection.first;
                    if (obj != null) {
                        str5 = (String) obj;
                    }
                    Object obj2 = contentProtection.second;
                    if (obj2 != null) {
                        arrayList5.add((DrmInitData.SchemeData) obj2);
                    }
                    availabilityTimeOffsetUs = j9;
                } else {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentComponent")) {
                        String strCheckLanguageConsistency = checkLanguageConsistency(str4, xmlPullParser2.getAttributeValue(null, str2));
                        iCheckContentTypeConsistency = checkContentTypeConsistency(iCheckContentTypeConsistency, parseContentType(xmlPullParser));
                        str4 = strCheckLanguageConsistency;
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Role")) {
                            arrayList8.add(parseDescriptor(xmlPullParser2, "Role"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AudioChannelConfiguration")) {
                            audioChannelConfiguration = parseAudioChannelConfiguration(xmlPullParser);
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Accessibility")) {
                            arrayList7.add(parseDescriptor(xmlPullParser2, "Accessibility"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "EssentialProperty")) {
                            arrayList9.add(parseDescriptor(xmlPullParser2, "EssentialProperty"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SupplementalProperty")) {
                            arrayList11.add(parseDescriptor(xmlPullParser2, "SupplementalProperty"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Representation")) {
                            arrayList13 = arrayList13;
                            arrayList5 = arrayList5;
                            ArrayList arrayList15 = arrayList9;
                            RepresentationInfo representation = parseRepresentation(xmlPullParser2, !arrayList13.isEmpty() ? arrayList13 : list, str6, str3, i8, i9, f3, audioChannelConfiguration, i7, str4, arrayList8, arrayList7, arrayList15, arrayList11, segmentTemplate, j4, j, j9, j8, j5, z);
                            str6 = str6;
                            i9 = i9;
                            arrayList3 = arrayList8;
                            arrayList = arrayList15;
                            SegmentBase segmentBase3 = segmentTemplate;
                            f = f3;
                            arrayList2 = arrayList7;
                            ArrayList arrayList16 = arrayList11;
                            str3 = str3;
                            i8 = i8;
                            i2 = i7;
                            long j10 = j8;
                            int i10 = audioChannelConfiguration;
                            int iCheckContentTypeConsistency2 = checkContentTypeConsistency(iCheckContentTypeConsistency, MimeTypes.getTrackType(representation.format.sampleMimeType));
                            ArrayList arrayList17 = arrayList14;
                            arrayList17.add(representation);
                            xmlPullParser2 = xmlPullParser2;
                            arrayList14 = arrayList17;
                            arrayList4 = arrayList16;
                            audioChannelConfiguration = i10;
                            arrayList10 = arrayList10;
                            j8 = j10;
                            iCheckContentTypeConsistency = iCheckContentTypeConsistency2;
                            segmentTemplate = segmentBase3;
                            availabilityTimeOffsetUs = j9;
                            str4 = str4;
                        } else {
                            arrayList13 = arrayList13;
                            int i11 = iCheckContentTypeConsistency;
                            str = str4;
                            str2 = str2;
                            arrayList5 = arrayList5;
                            segmentBase2 = segmentTemplate;
                            ArrayList arrayList18 = arrayList10;
                            j6 = j8;
                            j7 = j9;
                            f = f3;
                            XmlPullParser xmlPullParser3 = xmlPullParser2;
                            arrayList = arrayList9;
                            i = audioChannelConfiguration;
                            arrayList2 = arrayList7;
                            ArrayList arrayList19 = arrayList11;
                            ArrayList arrayList20 = arrayList14;
                            i2 = i7;
                            arrayList3 = arrayList8;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SegmentBase")) {
                                SegmentBase segmentBase4 = parseSegmentBase(xmlPullParser3, (SegmentBase.SingleSegmentBase) segmentBase2);
                                xmlPullParser2 = xmlPullParser3;
                                arrayList14 = arrayList20;
                                audioChannelConfiguration = i;
                                arrayList10 = arrayList18;
                                j8 = j6;
                                availabilityTimeOffsetUs = j7;
                                str4 = str;
                                iCheckContentTypeConsistency = i11;
                                segmentTemplate = segmentBase4;
                                arrayList4 = arrayList19;
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SegmentList")) {
                                long availabilityTimeOffsetUs2 = parseAvailabilityTimeOffsetUs(xmlPullParser3, j6);
                                arrayList14 = arrayList20;
                                xmlPullParser2 = xmlPullParser;
                                segmentTemplate = parseSegmentList(xmlPullParser2, (SegmentBase.SegmentList) segmentBase2, j4, j, j7, availabilityTimeOffsetUs2, j5);
                                availabilityTimeOffsetUs = j7;
                                arrayList4 = arrayList19;
                                str4 = str;
                                iCheckContentTypeConsistency = i11;
                                arrayList10 = arrayList18;
                                audioChannelConfiguration = i;
                                j8 = availabilityTimeOffsetUs2;
                            } else {
                                i3 = i11;
                                xmlPullParser2 = xmlPullParser3;
                                arrayList14 = arrayList20;
                                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                                    long availabilityTimeOffsetUs3 = parseAvailabilityTimeOffsetUs(xmlPullParser2, j6);
                                    arrayList4 = arrayList19;
                                    segmentTemplate = parseSegmentTemplate(xmlPullParser2, (SegmentBase.SegmentTemplate) segmentBase2, arrayList4, j4, j, j7, availabilityTimeOffsetUs3, j5);
                                    availabilityTimeOffsetUs = j7;
                                    str4 = str;
                                    audioChannelConfiguration = i;
                                    iCheckContentTypeConsistency = i3;
                                    arrayList10 = arrayList18;
                                    j8 = availabilityTimeOffsetUs3;
                                } else {
                                    arrayList4 = arrayList19;
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "InbandEventStream")) {
                                        arrayList10 = arrayList18;
                                        arrayList10.add(parseDescriptor(xmlPullParser2, "InbandEventStream"));
                                    } else {
                                        arrayList10 = arrayList18;
                                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Label")) {
                                            label = parseLabel(xmlPullParser);
                                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2)) {
                                            parseAdaptationSetChild(xmlPullParser);
                                        }
                                    }
                                    segmentTemplate = segmentBase2;
                                    audioChannelConfiguration = i;
                                    long j11 = j7;
                                    str4 = str;
                                    iCheckContentTypeConsistency = i3;
                                    j8 = j6;
                                    availabilityTimeOffsetUs = j11;
                                }
                            }
                        }
                        i3 = iCheckContentTypeConsistency;
                        j6 = j8;
                        arrayList13 = arrayList13;
                        str = str4;
                        i = audioChannelConfiguration;
                        str2 = str2;
                        arrayList5 = arrayList5;
                        segmentBase2 = segmentTemplate;
                        arrayList14 = arrayList14;
                        arrayList10 = arrayList10;
                        j7 = j9;
                        f = f3;
                        arrayList = arrayList9;
                        arrayList3 = arrayList8;
                        arrayList2 = arrayList7;
                        arrayList4 = arrayList11;
                        i2 = i7;
                        segmentTemplate = segmentBase2;
                        audioChannelConfiguration = i;
                        long j12 = j7;
                        str4 = str;
                        iCheckContentTypeConsistency = i3;
                        j8 = j6;
                        availabilityTimeOffsetUs = j12;
                    }
                    str2 = str2;
                    arrayList5 = arrayList5;
                    segmentTemplate = segmentTemplate;
                    arrayList14 = arrayList14;
                    arrayList10 = arrayList10;
                    availabilityTimeOffsetUs = j9;
                    f = f3;
                    arrayList = arrayList9;
                    arrayList3 = arrayList8;
                    arrayList2 = arrayList7;
                    arrayList4 = arrayList11;
                    i2 = i7;
                }
                if (XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                    break;
                }
                f2 = f;
                i7 = i2;
                arrayList7 = arrayList2;
                arrayList8 = arrayList3;
                arrayList5 = arrayList5;
                str2 = str2;
                arrayList14 = arrayList14;
                segmentTemplate = segmentTemplate;
                arrayList11 = arrayList4;
                arrayList9 = arrayList;
                arrayList10 = arrayList10;
                arrayList13 = arrayList13;
            }
            f = f3;
            arrayList = arrayList9;
            arrayList3 = arrayList8;
            arrayList2 = arrayList7;
            arrayList4 = arrayList11;
            i2 = i7;
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                break;
                break;
            }
            f2 = f;
            i7 = i2;
            arrayList7 = arrayList2;
            arrayList8 = arrayList3;
            arrayList5 = arrayList5;
            str2 = str2;
            arrayList14 = arrayList14;
            segmentTemplate = segmentTemplate;
            arrayList11 = arrayList4;
            arrayList9 = arrayList;
            arrayList10 = arrayList10;
            arrayList13 = arrayList13;
        }
        List arrayList21 = new ArrayList(arrayList14.size());
        int i12 = 0;
        while (i12 < arrayList14.size()) {
            ArrayList arrayList22 = arrayList10;
            arrayList21.add(buildRepresentation((RepresentationInfo) arrayList14.get(i12), label, str5, arrayList5, arrayList22));
            i12++;
            arrayList10 = arrayList22;
        }
        return buildAdaptationSet(i4, iCheckContentTypeConsistency, arrayList21, arrayList2, arrayList, arrayList4);
    }

    protected AdaptationSet buildAdaptationSet(int i, int i2, List list, List list2, List list3, List list4) {
        return new AdaptationSet(i, i2, list, list2, list3, list4);
    }

    protected int parseContentType(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(attributeValue)) {
            return -1;
        }
        if (MediaStreamTrack.AUDIO_TRACK_KIND.equals(attributeValue)) {
            return 1;
        }
        if (MediaStreamTrack.VIDEO_TRACK_KIND.equals(attributeValue)) {
            return 2;
        }
        if ("text".equals(attributeValue)) {
            return 3;
        }
        return "image".equals(attributeValue) ? 4 : -1;
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0092  */
    /* JADX WARN: Code duplicated, block: B:67:0x010d  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v10, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v25 */
    /* JADX WARN: Type inference failed for: r4v26 */
    /* JADX WARN: Type inference failed for: r4v27 */
    /* JADX WARN: Type inference failed for: r4v28 */
    /* JADX WARN: Type inference failed for: r4v29 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v12 */
    /* JADX WARN: Type inference failed for: r5v13 */
    /* JADX WARN: Type inference failed for: r5v14 */
    /* JADX WARN: Type inference failed for: r5v15 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v9 */
    protected Pair parseContentProtection(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String attributeValue;
        UUID uuid;
        UUID uuid2;
        ?? attributeValue2;
        ?? BuildPsshAtom;
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "schemeIdUri");
        if (attributeValue3 != null) {
            String lowerCase = Ascii.toLowerCase(attributeValue3);
            lowerCase.getClass();
            switch (lowerCase) {
                case "urn:uuid:e2719d58-a985-b3c9-781a-b030af78d30e":
                    uuid = C.CLEARKEY_UUID;
                    attributeValue = null;
                    uuid2 = null;
                    attributeValue2 = uuid2;
                    BuildPsshAtom = uuid2;
                    break;
                case "urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95":
                    uuid = C.PLAYREADY_UUID;
                    attributeValue = null;
                    uuid2 = null;
                    attributeValue2 = uuid2;
                    BuildPsshAtom = uuid2;
                    break;
                case "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed":
                    uuid = C.WIDEVINE_UUID;
                    attributeValue = null;
                    uuid2 = null;
                    attributeValue2 = uuid2;
                    BuildPsshAtom = uuid2;
                    break;
                case "urn:mpeg:dash:mp4protection:2011":
                    attributeValue = xmlPullParser.getAttributeValue(null, "value");
                    String attributeValueIgnorePrefix = XmlPullParserUtil.getAttributeValueIgnorePrefix(xmlPullParser, "default_KID");
                    if (!TextUtils.isEmpty(attributeValueIgnorePrefix) && !"00000000-0000-0000-0000-000000000000".equals(attributeValueIgnorePrefix)) {
                        String[] strArrSplit = attributeValueIgnorePrefix.split("\\s+");
                        UUID[] uuidArr = new UUID[strArrSplit.length];
                        for (int i = 0; i < strArrSplit.length; i++) {
                            uuidArr[i] = UUID.fromString(strArrSplit[i]);
                        }
                        uuid = C.COMMON_PSSH_UUID;
                        attributeValue2 = 0;
                        BuildPsshAtom = PsshAtomUtil.buildPsshAtom(uuid, uuidArr, null);
                        break;
                    } else {
                        uuid = null;
                        uuid2 = uuid;
                        attributeValue2 = uuid2;
                        BuildPsshAtom = uuid2;
                        break;
                    }
                    break;
                default:
                    attributeValue = null;
                    uuid = null;
                    uuid2 = uuid;
                    attributeValue2 = uuid2;
                    BuildPsshAtom = uuid2;
                    break;
            }
        } else {
            attributeValue = null;
            uuid = null;
            uuid2 = uuid;
            attributeValue2 = uuid2;
            BuildPsshAtom = uuid2;
        }
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "clearkey:Laurl") && xmlPullParser.next() == 4) {
                BuildPsshAtom = BuildPsshAtom;
                attributeValue2 = xmlPullParser.getText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "ms:laurl")) {
                BuildPsshAtom = BuildPsshAtom;
                attributeValue2 = xmlPullParser.getAttributeValue(null, "licenseUrl");
            } else if (BuildPsshAtom == 0 && XmlPullParserUtil.isStartTagIgnorePrefix(xmlPullParser, "pssh") && xmlPullParser.next() == 4) {
                byte[] bArrDecode = Base64.decode(xmlPullParser.getText(), 0);
                UUID uuid3 = PsshAtomUtil.parseUuid(bArrDecode);
                if (uuid3 == null) {
                    Log.w("MpdParser", "Skipping malformed cenc:pssh data");
                    uuid = uuid3;
                    BuildPsshAtom = 0;
                    attributeValue2 = attributeValue2;
                } else {
                    BuildPsshAtom = bArrDecode;
                    uuid = uuid3;
                    attributeValue2 = attributeValue2;
                }
            } else if (BuildPsshAtom == 0) {
                UUID uuid4 = C.PLAYREADY_UUID;
                if (uuid4.equals(uuid) && XmlPullParserUtil.isStartTag(xmlPullParser, "mspr:pro") && xmlPullParser.next() == 4) {
                    BuildPsshAtom = PsshAtomUtil.buildPsshAtom(uuid4, Base64.decode(xmlPullParser.getText(), 0));
                    attributeValue2 = attributeValue2;
                } else {
                    maybeSkipTag(xmlPullParser);
                    BuildPsshAtom = BuildPsshAtom;
                    attributeValue2 = attributeValue2;
                }
            } else {
                maybeSkipTag(xmlPullParser);
                BuildPsshAtom = BuildPsshAtom;
                attributeValue2 = attributeValue2;
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "ContentProtection"));
        return Pair.create(attributeValue, uuid != null ? new DrmInitData.SchemeData(uuid, attributeValue2, "video/mp4", BuildPsshAtom) : null);
    }

    protected void parseAdaptationSetChild(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        maybeSkipTag(xmlPullParser);
    }

    /* JADX WARN: Code duplicated, block: B:56:0x0214 A[LOOP:0: B:3:0x006c->B:56:0x0214, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:57:0x01c7 A[EDGE_INSN: B:57:0x01c7->B:46:0x01c7 BREAK  A[LOOP:0: B:3:0x006c->B:56:0x0214], SYNTHETIC] */
    protected RepresentationInfo parseRepresentation(XmlPullParser xmlPullParser, List list, String str, String str2, int i, int i2, float f, int i3, int i4, String str3, List list2, List list3, List list4, List list5, SegmentBase segmentBase, long j, long j2, long j3, long j4, long j5, boolean z) throws XmlPullParserException, IOException {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        int audioChannelConfiguration;
        SegmentBase segmentBase2;
        long j6;
        SegmentBase segmentTemplate;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "id");
        int i5 = parseInt(xmlPullParser2, "bandwidth", -1);
        String string = parseString(xmlPullParser2, "mimeType", str);
        String string2 = parseString(xmlPullParser2, "codecs", str2);
        int i6 = parseInt(xmlPullParser2, "width", i);
        int i7 = parseInt(xmlPullParser2, "height", i2);
        float frameRate = parseFrameRate(xmlPullParser2, f);
        int i8 = parseInt(xmlPullParser2, "audioSamplingRate", i4);
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList(list4);
        ArrayList arrayList10 = new ArrayList(list5);
        int i9 = i3;
        long j7 = j4;
        String str4 = null;
        boolean z2 = false;
        SegmentBase segmentBase3 = segmentBase;
        long availabilityTimeOffsetUs = j3;
        ArrayList arrayList11 = new ArrayList();
        while (true) {
            xmlPullParser2.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (!z2) {
                    availabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser2, availabilityTimeOffsetUs);
                    z2 = true;
                }
                arrayList11.addAll(parseBaseUrl(xmlPullParser2, list, z));
                xmlPullParser2 = xmlPullParser2;
                arrayList6 = arrayList11;
                j7 = j7;
                i5 = i5;
                audioChannelConfiguration = i9;
                str4 = str4;
                z2 = z2;
                arrayList = arrayList8;
                segmentBase2 = segmentBase3;
                j6 = availabilityTimeOffsetUs;
            } else {
                arrayList = arrayList8;
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AudioChannelConfiguration")) {
                    segmentBase2 = segmentBase3;
                    audioChannelConfiguration = parseAudioChannelConfiguration(xmlPullParser);
                } else {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentBase")) {
                        audioChannelConfiguration = i9;
                        segmentBase2 = parseSegmentBase(xmlPullParser2, (SegmentBase.SingleSegmentBase) segmentBase3);
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentList")) {
                            long availabilityTimeOffsetUs2 = parseAvailabilityTimeOffsetUs(xmlPullParser2, j7);
                            arrayList4 = arrayList11;
                            arrayList2 = arrayList10;
                            i5 = i5;
                            arrayList = arrayList;
                            segmentTemplate = parseSegmentList(xmlPullParser2, (SegmentBase.SegmentList) segmentBase3, j, j2, availabilityTimeOffsetUs, availabilityTimeOffsetUs2, j5);
                            arrayList3 = arrayList9;
                            j7 = availabilityTimeOffsetUs2;
                        } else {
                            ArrayList arrayList12 = arrayList11;
                            arrayList2 = arrayList10;
                            i5 = i5;
                            arrayList = arrayList;
                            ArrayList arrayList13 = arrayList9;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                                long availabilityTimeOffsetUs3 = parseAvailabilityTimeOffsetUs(xmlPullParser2, j7);
                                long j8 = availabilityTimeOffsetUs;
                                segmentTemplate = parseSegmentTemplate(xmlPullParser2, (SegmentBase.SegmentTemplate) segmentBase3, list5, j, j2, j8, availabilityTimeOffsetUs3, j5);
                                availabilityTimeOffsetUs = j8;
                                arrayList3 = arrayList13;
                                j7 = availabilityTimeOffsetUs3;
                                arrayList4 = arrayList12;
                            } else {
                                xmlPullParser2 = xmlPullParser2;
                                arrayList7 = arrayList7;
                                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentProtection")) {
                                    Pair contentProtection = parseContentProtection(xmlPullParser);
                                    Object obj = contentProtection.first;
                                    if (obj != null) {
                                        str4 = (String) obj;
                                    }
                                    Object obj2 = contentProtection.second;
                                    if (obj2 != null) {
                                        arrayList7.add((DrmInitData.SchemeData) obj2);
                                    }
                                    arrayList3 = arrayList13;
                                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "InbandEventStream")) {
                                    arrayList.add(parseDescriptor(xmlPullParser2, "InbandEventStream"));
                                    arrayList3 = arrayList13;
                                } else {
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "EssentialProperty")) {
                                        arrayList3 = arrayList13;
                                        arrayList3.add(parseDescriptor(xmlPullParser2, "EssentialProperty"));
                                    } else {
                                        arrayList3 = arrayList13;
                                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SupplementalProperty")) {
                                            arrayList10 = arrayList2;
                                            arrayList10.add(parseDescriptor(xmlPullParser2, "SupplementalProperty"));
                                        } else {
                                            arrayList10 = arrayList2;
                                            maybeSkipTag(xmlPullParser2);
                                        }
                                    }
                                    j7 = j7;
                                    audioChannelConfiguration = i9;
                                    str4 = str4;
                                    z2 = z2;
                                    segmentBase2 = segmentBase3;
                                    j6 = availabilityTimeOffsetUs;
                                    arrayList5 = arrayList12;
                                }
                                arrayList10 = arrayList2;
                                j7 = j7;
                                audioChannelConfiguration = i9;
                                str4 = str4;
                                z2 = z2;
                                segmentBase2 = segmentBase3;
                                j6 = availabilityTimeOffsetUs;
                                arrayList5 = arrayList12;
                            }
                        }
                        audioChannelConfiguration = i9;
                        str4 = str4;
                        z2 = z2;
                        segmentBase2 = segmentTemplate;
                        j6 = availabilityTimeOffsetUs;
                        arrayList10 = arrayList2;
                        arrayList5 = arrayList4;
                    }
                    if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Representation")) {
                        break;
                    }
                    ArrayList arrayList14 = arrayList10;
                    arrayList9 = arrayList3;
                    arrayList7 = arrayList7;
                    arrayList8 = arrayList;
                    segmentBase3 = segmentBase2;
                    availabilityTimeOffsetUs = j6;
                    i5 = i5;
                    j7 = j7;
                    z2 = z2;
                    str4 = str4;
                    arrayList10 = arrayList14;
                    i9 = audioChannelConfiguration;
                    arrayList11 = arrayList5;
                    xmlPullParser2 = xmlPullParser;
                }
                j6 = availabilityTimeOffsetUs;
                arrayList6 = arrayList11;
            }
            arrayList7 = arrayList7;
            arrayList3 = arrayList9;
            arrayList5 = arrayList6;
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Representation")) {
                break;
                break;
            }
            ArrayList arrayList15 = arrayList10;
            arrayList9 = arrayList3;
            arrayList7 = arrayList7;
            arrayList8 = arrayList;
            segmentBase3 = segmentBase2;
            availabilityTimeOffsetUs = j6;
            i5 = i5;
            j7 = j7;
            z2 = z2;
            str4 = str4;
            arrayList10 = arrayList15;
            i9 = audioChannelConfiguration;
            arrayList11 = arrayList5;
            xmlPullParser2 = xmlPullParser;
        }
        ArrayList arrayList16 = arrayList7;
        List list6 = arrayList10;
        Format formatBuildFormat = buildFormat(attributeValue, string, i6, i7, frameRate, audioChannelConfiguration, i8, i5, str3, list2, list3, string2, arrayList3, list6);
        if (segmentBase2 == null) {
            segmentBase2 = new SegmentBase.SingleSegmentBase();
        }
        boolean zIsEmpty = arrayList5.isEmpty();
        List list7 = arrayList5;
        if (zIsEmpty) {
            list7 = list;
        }
        return new RepresentationInfo(formatBuildFormat, list7, segmentBase2, str4, arrayList16, arrayList, arrayList3, list6, -1L);
    }

    protected Format buildFormat(String str, String str2, int i, int i2, float f, int i3, int i4, int i5, String str3, List list, List list2, String str4, List list3, List list4) {
        String str5 = str4;
        String sampleMimeType = getSampleMimeType(str2, str5);
        if ("audio/eac3".equals(sampleMimeType)) {
            sampleMimeType = parseEac3SupplementalProperties(list4);
            if ("audio/eac3-joc".equals(sampleMimeType)) {
                str5 = "ec+3";
            }
        }
        int selectionFlagsFromRoleDescriptors = parseSelectionFlagsFromRoleDescriptors(list);
        int roleFlagsFromRoleDescriptors = parseRoleFlagsFromRoleDescriptors(list) | parseRoleFlagsFromAccessibilityDescriptors(list2) | parseRoleFlagsFromProperties(list3) | parseRoleFlagsFromProperties(list4);
        Pair tileCountFromProperties = parseTileCountFromProperties(list3);
        Format.Builder language = new Format.Builder().setId(str).setContainerMimeType(str2).setSampleMimeType(sampleMimeType).setCodecs(str5).setPeakBitrate(i5).setSelectionFlags(selectionFlagsFromRoleDescriptors).setRoleFlags(roleFlagsFromRoleDescriptors).setLanguage(str3);
        int cea708AccessibilityChannel = -1;
        Format.Builder tileCountVertical = language.setTileCountHorizontal(tileCountFromProperties != null ? ((Integer) tileCountFromProperties.first).intValue() : -1).setTileCountVertical(tileCountFromProperties != null ? ((Integer) tileCountFromProperties.second).intValue() : -1);
        if (MimeTypes.isVideo(sampleMimeType)) {
            tileCountVertical.setWidth(i).setHeight(i2).setFrameRate(f);
        } else if (MimeTypes.isAudio(sampleMimeType)) {
            tileCountVertical.setChannelCount(i3).setSampleRate(i4);
        } else if (MimeTypes.isText(sampleMimeType)) {
            if ("application/cea-608".equals(sampleMimeType)) {
                cea708AccessibilityChannel = parseCea608AccessibilityChannel(list2);
            } else if ("application/cea-708".equals(sampleMimeType)) {
                cea708AccessibilityChannel = parseCea708AccessibilityChannel(list2);
            }
            tileCountVertical.setAccessibilityChannel(cea708AccessibilityChannel);
        } else if (MimeTypes.isImage(sampleMimeType)) {
            tileCountVertical.setWidth(i).setHeight(i2);
        }
        return tileCountVertical.build();
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String str, String str2, ArrayList arrayList, ArrayList arrayList2) {
        Format.Builder builderBuildUpon = representationInfo.format.buildUpon();
        if (str != null) {
            builderBuildUpon.setLabel(str);
        }
        String str3 = representationInfo.drmSchemeType;
        if (str3 != null) {
            str2 = str3;
        }
        ArrayList arrayList3 = representationInfo.drmSchemeDatas;
        arrayList3.addAll(arrayList);
        if (!arrayList3.isEmpty()) {
            fillInClearKeyInformation(arrayList3);
            filterRedundantIncompleteSchemeDatas(arrayList3);
            builderBuildUpon.setDrmInitData(new DrmInitData(str2, arrayList3));
        }
        ArrayList arrayList4 = representationInfo.inbandEventStreams;
        arrayList4.addAll(arrayList2);
        return Representation.newInstance(representationInfo.revisionId, builderBuildUpon.build(), representationInfo.baseUrls, representationInfo.segmentBase, arrayList4, representationInfo.essentialProperties, representationInfo.supplementalProperties, null);
    }

    protected SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser xmlPullParser, SegmentBase.SingleSegmentBase singleSegmentBase) throws XmlPullParserException, IOException {
        long j = parseLong(xmlPullParser, "timescale", singleSegmentBase != null ? singleSegmentBase.timescale : 1L);
        long j2 = parseLong(xmlPullParser, "presentationTimeOffset", singleSegmentBase != null ? singleSegmentBase.presentationTimeOffset : 0L);
        long j3 = singleSegmentBase != null ? singleSegmentBase.indexStart : 0L;
        long j4 = singleSegmentBase != null ? singleSegmentBase.indexLength : 0L;
        String attributeValue = xmlPullParser.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] strArrSplit = attributeValue.split("-");
            j3 = Long.parseLong(strArrSplit[0]);
            j4 = (Long.parseLong(strArrSplit[1]) - j3) + 1;
        }
        long j5 = j4;
        RangedUri initialization = singleSegmentBase != null ? singleSegmentBase.initialization : null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization = parseInitialization(xmlPullParser);
            } else {
                maybeSkipTag(xmlPullParser);
            }
            RangedUri rangedUri = initialization;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentBase")) {
                return buildSingleSegmentBase(rangedUri, j, j2, j3, j5);
            }
            initialization = rangedUri;
        }
    }

    protected SegmentBase.SingleSegmentBase buildSingleSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4) {
        return new SegmentBase.SingleSegmentBase(rangedUri, j, j2, j3, j4);
    }

    protected SegmentBase.SegmentList parseSegmentList(XmlPullParser xmlPullParser, SegmentBase.SegmentList segmentList, long j, long j2, long j3, long j4, long j5) throws XmlPullParserException, IOException {
        long j6;
        long j7 = parseLong(xmlPullParser, "timescale", segmentList != null ? segmentList.timescale : 1L);
        long j8 = parseLong(xmlPullParser, "presentationTimeOffset", segmentList != null ? segmentList.presentationTimeOffset : 0L);
        long j9 = parseLong(xmlPullParser, "duration", segmentList != null ? segmentList.duration : -9223372036854775807L);
        long j10 = parseLong(xmlPullParser, "startNumber", segmentList != null ? segmentList.startNumber : 1L);
        long finalAvailabilityTimeOffset = getFinalAvailabilityTimeOffset(j3, j4);
        List arrayList = null;
        RangedUri initialization = null;
        List segmentTimeline = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization = parseInitialization(xmlPullParser);
                j6 = j7;
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                j6 = j7;
                segmentTimeline = parseSegmentTimeline(xmlPullParser, j6, j2);
            } else {
                j6 = j7;
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentURL")) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(parseSegmentUrl(xmlPullParser));
                } else {
                    maybeSkipTag(xmlPullParser);
                }
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentList")) {
                break;
            }
            j7 = j6;
        }
        if (segmentList != null) {
            if (initialization == null) {
                initialization = segmentList.initialization;
            }
            if (segmentTimeline == null) {
                segmentTimeline = segmentList.segmentTimeline;
            }
            if (arrayList == null) {
                arrayList = segmentList.mediaSegments;
            }
        }
        return buildSegmentList(initialization, j6, j8, j10, j9, segmentTimeline, finalAvailabilityTimeOffset, arrayList, j5, j);
    }

    protected SegmentBase.SegmentList buildSegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List list, long j5, List list2, long j6, long j7) {
        return new SegmentBase.SegmentList(rangedUri, j, j2, j3, j4, list, j5, list2, Util.msToUs(j6), Util.msToUs(j7));
    }

    protected SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser xmlPullParser, SegmentBase.SegmentTemplate segmentTemplate, List list, long j, long j2, long j3, long j4, long j5) throws XmlPullParserException, IOException {
        long j6;
        DashManifestParser dashManifestParser = this;
        long j7 = parseLong(xmlPullParser, "timescale", segmentTemplate != null ? segmentTemplate.timescale : 1L);
        long j8 = parseLong(xmlPullParser, "presentationTimeOffset", segmentTemplate != null ? segmentTemplate.presentationTimeOffset : 0L);
        long j9 = parseLong(xmlPullParser, "duration", segmentTemplate != null ? segmentTemplate.duration : -9223372036854775807L);
        long j10 = parseLong(xmlPullParser, "startNumber", segmentTemplate != null ? segmentTemplate.startNumber : 1L);
        long lastSegmentNumberSupplementalProperty = parseLastSegmentNumberSupplementalProperty(list);
        long finalAvailabilityTimeOffset = getFinalAvailabilityTimeOffset(j3, j4);
        UrlTemplate urlTemplate = dashManifestParser.parseUrlTemplate(xmlPullParser, "media", segmentTemplate != null ? segmentTemplate.mediaTemplate : null);
        UrlTemplate urlTemplate2 = dashManifestParser.parseUrlTemplate(xmlPullParser, "initialization", segmentTemplate != null ? segmentTemplate.initializationTemplate : null);
        RangedUri initialization = null;
        List segmentTimeline = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization = parseInitialization(xmlPullParser);
                j6 = j7;
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                j6 = j7;
                segmentTimeline = dashManifestParser.parseSegmentTimeline(xmlPullParser, j6, j2);
            } else {
                j6 = j7;
                maybeSkipTag(xmlPullParser);
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTemplate")) {
                break;
            }
            dashManifestParser = this;
            finalAvailabilityTimeOffset = finalAvailabilityTimeOffset;
            lastSegmentNumberSupplementalProperty = lastSegmentNumberSupplementalProperty;
            j10 = j10;
            j8 = j8;
            j7 = j6;
        }
        if (segmentTemplate != null) {
            if (initialization == null) {
                initialization = segmentTemplate.initialization;
            }
            if (segmentTimeline == null) {
                segmentTimeline = segmentTemplate.segmentTimeline;
            }
        }
        return buildSegmentTemplate(initialization, j6, j8, j10, lastSegmentNumberSupplementalProperty, j9, segmentTimeline, finalAvailabilityTimeOffset, urlTemplate2, urlTemplate, j5, j);
    }

    protected SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, long j5, List list, long j6, UrlTemplate urlTemplate, UrlTemplate urlTemplate2, long j7, long j8) {
        return new SegmentBase.SegmentTemplate(rangedUri, j, j2, j3, j4, j5, list, j6, urlTemplate, urlTemplate2, Util.msToUs(j7), Util.msToUs(j8));
    }

    protected EventStream parseEventStream(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        long j;
        String str;
        String str2;
        XmlPullParser xmlPullParser2;
        String string = parseString(xmlPullParser, "schemeIdUri", _UrlKt.FRAGMENT_ENCODE_SET);
        String string2 = parseString(xmlPullParser, "value", _UrlKt.FRAGMENT_ENCODE_SET);
        long j2 = parseLong(xmlPullParser, "timescale", 1L);
        long j3 = parseLong(xmlPullParser, "presentationTimeOffset", 0L);
        ArrayList arrayList = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                j = j2;
                str = string2;
                str2 = string;
                xmlPullParser2 = xmlPullParser;
                arrayList.add(parseEvent(xmlPullParser2, str2, str, j, j3, byteArrayOutputStream));
            } else {
                j = j2;
                str = string2;
                str2 = string;
                xmlPullParser2 = xmlPullParser;
                maybeSkipTag(xmlPullParser2);
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "EventStream")) {
                break;
            }
            xmlPullParser = xmlPullParser2;
            string = str2;
            string2 = str;
            j2 = j;
        }
        long[] jArr = new long[arrayList.size()];
        EventMessage[] eventMessageArr = new EventMessage[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            Pair pair = (Pair) arrayList.get(i);
            jArr[i] = ((Long) pair.first).longValue();
            eventMessageArr[i] = (EventMessage) pair.second;
        }
        return buildEventStream(str2, str, j, jArr, eventMessageArr);
    }

    protected EventStream buildEventStream(String str, String str2, long j, long[] jArr, EventMessage[] eventMessageArr) {
        return new EventStream(str, str2, j, jArr, eventMessageArr);
    }

    protected Pair parseEvent(XmlPullParser xmlPullParser, String str, String str2, long j, long j2, ByteArrayOutputStream byteArrayOutputStream) throws XmlPullParserException, IOException {
        long j3 = parseLong(xmlPullParser, "id", 0L);
        long j4 = parseLong(xmlPullParser, "duration", -9223372036854775807L);
        long j5 = parseLong(xmlPullParser, "presentationTime", 0L);
        long jScaleLargeTimestamp = Util.scaleLargeTimestamp(j4, 1000L, j);
        long jScaleLargeTimestamp2 = Util.scaleLargeTimestamp(j5 - j2, 1000000L, j);
        String string = parseString(xmlPullParser, "messageData", null);
        byte[] eventObject = parseEventObject(xmlPullParser, byteArrayOutputStream);
        Long lValueOf = Long.valueOf(jScaleLargeTimestamp2);
        if (string != null) {
            eventObject = Util.getUtf8Bytes(string);
        }
        return Pair.create(lValueOf, buildEvent(str, str2, j3, jScaleLargeTimestamp, eventObject));
    }

    protected byte[] parseEventObject(XmlPullParser xmlPullParser, ByteArrayOutputStream byteArrayOutputStream) throws XmlPullParserException, IOException {
        byteArrayOutputStream.reset();
        XmlSerializer xmlSerializerNewSerializer = Xml.newSerializer();
        xmlSerializerNewSerializer.setOutput(byteArrayOutputStream, Charsets.UTF_8.name());
        xmlPullParser.nextToken();
        while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Event")) {
            switch (xmlPullParser.getEventType()) {
                case 0:
                    xmlSerializerNewSerializer.startDocument(null, Boolean.FALSE);
                    break;
                case 1:
                    xmlSerializerNewSerializer.endDocument();
                    break;
                case 2:
                    xmlSerializerNewSerializer.startTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                        xmlSerializerNewSerializer.attribute(xmlPullParser.getAttributeNamespace(i), xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
                    }
                    break;
                case 3:
                    xmlSerializerNewSerializer.endTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    break;
                case 4:
                    xmlSerializerNewSerializer.text(xmlPullParser.getText());
                    break;
                case 5:
                    xmlSerializerNewSerializer.cdsect(xmlPullParser.getText());
                    break;
                case 6:
                    xmlSerializerNewSerializer.entityRef(xmlPullParser.getText());
                    break;
                case 7:
                    xmlSerializerNewSerializer.ignorableWhitespace(xmlPullParser.getText());
                    break;
                case 8:
                    xmlSerializerNewSerializer.processingInstruction(xmlPullParser.getText());
                    break;
                case 9:
                    xmlSerializerNewSerializer.comment(xmlPullParser.getText());
                    break;
                case 10:
                    xmlSerializerNewSerializer.docdecl(xmlPullParser.getText());
                    break;
            }
            xmlPullParser.nextToken();
        }
        xmlSerializerNewSerializer.flush();
        return byteArrayOutputStream.toByteArray();
    }

    protected EventMessage buildEvent(String str, String str2, long j, long j2, byte[] bArr) {
        return new EventMessage(str, str2, j2, j, bArr);
    }

    protected List parseSegmentTimeline(XmlPullParser xmlPullParser, long j, long j2) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        long jAddSegmentTimelineElementsToList = 0;
        long j3 = -9223372036854775807L;
        boolean z = false;
        int i = 0;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "S")) {
                long j4 = parseLong(xmlPullParser, "t", -9223372036854775807L);
                if (z) {
                    ArrayList arrayList2 = arrayList;
                    jAddSegmentTimelineElementsToList = addSegmentTimelineElementsToList(arrayList2, jAddSegmentTimelineElementsToList, j3, i, j4);
                    arrayList = arrayList2;
                }
                if (j4 != -9223372036854775807L) {
                    jAddSegmentTimelineElementsToList = j4;
                }
                j3 = parseLong(xmlPullParser, "d", -9223372036854775807L);
                i = parseInt(xmlPullParser, "r", 0);
                z = true;
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTimeline"));
        if (z) {
            addSegmentTimelineElementsToList(arrayList, jAddSegmentTimelineElementsToList, j3, i, Util.scaleLargeTimestamp(j2, j, 1000L));
        }
        return arrayList;
    }

    private long addSegmentTimelineElementsToList(List list, long j, long j2, int i, long j3) {
        int iCeilDivide = i >= 0 ? i + 1 : (int) Util.ceilDivide(j3 - j, j2);
        for (int i2 = 0; i2 < iCeilDivide; i2++) {
            list.add(buildSegmentTimelineElement(j, j2));
            j += j2;
        }
        return j;
    }

    protected SegmentBase.SegmentTimelineElement buildSegmentTimelineElement(long j, long j2) {
        return new SegmentBase.SegmentTimelineElement(j, j2);
    }

    protected UrlTemplate parseUrlTemplate(XmlPullParser xmlPullParser, String str, UrlTemplate urlTemplate) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue != null ? UrlTemplate.compile(attributeValue) : urlTemplate;
    }

    protected RangedUri parseInitialization(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "sourceURL", "range");
    }

    protected RangedUri parseSegmentUrl(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "media", "mediaRange");
    }

    protected RangedUri parseRangedUrl(XmlPullParser xmlPullParser, String str, String str2) {
        long j;
        long j2;
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        String attributeValue2 = xmlPullParser.getAttributeValue(null, str2);
        if (attributeValue2 != null) {
            String[] strArrSplit = attributeValue2.split("-");
            j = Long.parseLong(strArrSplit[0]);
            if (strArrSplit.length == 2) {
                j2 = (Long.parseLong(strArrSplit[1]) - j) + 1;
            }
            return buildRangedUri(attributeValue, j, j2);
        }
        j = 0;
        j2 = -1;
        return buildRangedUri(attributeValue, j, j2);
    }

    protected RangedUri buildRangedUri(String str, long j, long j2) {
        return new RangedUri(str, j, j2);
    }

    protected ProgramInformation parseProgramInformation(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String strNextText = null;
        String string = parseString(xmlPullParser, "moreInformationURL", null);
        String string2 = parseString(xmlPullParser, "lang", null);
        String strNextText2 = null;
        String strNextText3 = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Title")) {
                strNextText = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Source")) {
                strNextText2 = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Copyright")) {
                strNextText3 = xmlPullParser.nextText();
            } else {
                maybeSkipTag(xmlPullParser);
            }
            String str = strNextText2;
            String str2 = strNextText;
            String str3 = strNextText3;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "ProgramInformation")) {
                return new ProgramInformation(str2, str, str3, string, string2);
            }
            strNextText = str2;
            strNextText2 = str;
            strNextText3 = str3;
        }
    }

    protected String parseLabel(XmlPullParser xmlPullParser) {
        return parseText(xmlPullParser, "Label");
    }

    protected List parseBaseUrl(XmlPullParser xmlPullParser, List list, boolean z) throws XmlPullParserException, IOException {
        int i;
        String attributeValue = xmlPullParser.getAttributeValue(null, "dvb:priority");
        if (attributeValue != null) {
            i = Integer.parseInt(attributeValue);
        } else {
            i = z ? 1 : Integer.MIN_VALUE;
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "dvb:weight");
        int i2 = attributeValue2 != null ? Integer.parseInt(attributeValue2) : 1;
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "serviceLocation");
        String text = parseText(xmlPullParser, "BaseURL");
        if (UriUtil.isAbsolute(text)) {
            if (attributeValue3 == null) {
                attributeValue3 = text;
            }
            return Lists.newArrayList(new BaseUrl(text, attributeValue3, i, i2));
        }
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < list.size(); i3++) {
            BaseUrl baseUrl = (BaseUrl) list.get(i3);
            String strResolve = UriUtil.resolve(baseUrl.url, text);
            String str = attributeValue3 == null ? strResolve : attributeValue3;
            if (z) {
                i = baseUrl.priority;
                i2 = baseUrl.weight;
                str = baseUrl.serviceLocation;
            }
            arrayList.add(new BaseUrl(strResolve, str, i, i2));
        }
        return arrayList;
    }

    protected long parseAvailabilityTimeOffsetUs(XmlPullParser xmlPullParser, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "availabilityTimeOffset");
        if (attributeValue == null) {
            return j;
        }
        if ("INF".equals(attributeValue)) {
            return Long.MAX_VALUE;
        }
        return (long) (Float.parseFloat(attributeValue) * 1000000.0f);
    }

    protected int parseAudioChannelConfiguration(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String string = parseString(xmlPullParser, "schemeIdUri", null);
        string.getClass();
        int dtsChannelConfiguration = -1;
        switch (string) {
            case "urn:dts:dash:audio_channel_configuration:2012":
            case "tag:dts.com,2014:dash:audio_channel_configuration:2012":
                dtsChannelConfiguration = parseDtsChannelConfiguration(xmlPullParser);
                break;
            case "urn:mpeg:dash:23003:3:audio_channel_configuration:2011":
                dtsChannelConfiguration = parseInt(xmlPullParser, "value", -1);
                break;
            case "tag:dolby.com,2014:dash:audio_channel_configuration:2011":
            case "urn:dolby:dash:audio_channel_configuration:2011":
                dtsChannelConfiguration = parseDolbyChannelConfiguration(xmlPullParser);
                break;
            case "urn:mpeg:mpegB:cicp:ChannelConfiguration":
                dtsChannelConfiguration = parseMpegChannelConfiguration(xmlPullParser);
                break;
            case "tag:dts.com,2018:uhd:audio_channel_configuration":
                dtsChannelConfiguration = parseDtsxChannelConfiguration(xmlPullParser);
                break;
        }
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "AudioChannelConfiguration"));
        return dtsChannelConfiguration;
    }

    protected int parseSelectionFlagsFromRoleDescriptors(List list) {
        int selectionFlagsFromDashRoleScheme = 0;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                selectionFlagsFromDashRoleScheme |= parseSelectionFlagsFromDashRoleScheme(descriptor.value);
            }
        }
        return selectionFlagsFromDashRoleScheme;
    }

    protected int parseSelectionFlagsFromDashRoleScheme(String str) {
        if (str == null) {
            return 0;
        }
        return (str.equals("forced_subtitle") || str.equals("forced-subtitle")) ? 2 : 0;
    }

    protected int parseRoleFlagsFromRoleDescriptors(List list) {
        int roleFlagsFromDashRoleScheme = 0;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                roleFlagsFromDashRoleScheme |= parseRoleFlagsFromDashRoleScheme(descriptor.value);
            }
        }
        return roleFlagsFromDashRoleScheme;
    }

    protected int parseRoleFlagsFromAccessibilityDescriptors(List list) {
        int tvaAudioPurposeCsValue;
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = (Descriptor) list.get(i2);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                tvaAudioPurposeCsValue = parseRoleFlagsFromDashRoleScheme(descriptor.value);
            } else {
                if (Ascii.equalsIgnoreCase("urn:tva:metadata:cs:AudioPurposeCS:2007", descriptor.schemeIdUri)) {
                    tvaAudioPurposeCsValue = parseTvaAudioPurposeCsValue(descriptor.value);
                }
            }
            i |= tvaAudioPurposeCsValue;
        }
        return i;
    }

    protected int parseRoleFlagsFromProperties(List list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/trickmode", ((Descriptor) list.get(i2)).schemeIdUri)) {
                i = 16384;
            }
        }
        return i;
    }

    protected int parseRoleFlagsFromDashRoleScheme(String str) {
        if (str == null) {
            return 0;
        }
        switch (str) {
            case "subtitle":
            case "forced_subtitle":
            case "forced-subtitle":
                return 128;
            case "description":
                return 512;
            case "enhanced-audio-intelligibility":
                return 2048;
            case "alternate":
                return 2;
            case "dub":
                return 16;
            case "main":
                return 1;
            case "sign":
                return 256;
            case "caption":
                return 64;
            case "commentary":
                return 8;
            case "emergency":
                return 32;
            case "supplementary":
                return 4;
            default:
                return 0;
        }
    }

    protected int parseTvaAudioPurposeCsValue(String str) {
        if (str == null) {
            return 0;
        }
        byte b = -1;
        switch (str.hashCode()) {
            case 49:
                if (str.equals("1")) {
                    b = 0;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    b = 1;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    b = 2;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    b = 3;
                }
                break;
            case 54:
                if (str.equals("6")) {
                    b = 4;
                }
                break;
        }
        switch (b) {
            case 0:
                return 512;
            case 1:
                return 2048;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    protected String[] parseProfiles(XmlPullParser xmlPullParser, String str, String[] strArr) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? strArr : attributeValue.split(",");
    }

    protected Pair parseTileCountFromProperties(List list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ((Ascii.equalsIgnoreCase("http://dashif.org/thumbnail_tile", descriptor.schemeIdUri) || Ascii.equalsIgnoreCase("http://dashif.org/guidelines/thumbnail_tile", descriptor.schemeIdUri)) && (str = descriptor.value) != null) {
                String[] strArrSplit = Util.split(str, "x");
                if (strArrSplit.length != 2) {
                    continue;
                } else {
                    try {
                        return Pair.create(Integer.valueOf(Integer.parseInt(strArrSplit[0])), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
                    } catch (NumberFormatException unused) {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public static void maybeSkipTag(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
            int i = 1;
            while (i != 0) {
                xmlPullParser.next();
                if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                    i++;
                } else if (XmlPullParserUtil.isEndTag(xmlPullParser)) {
                    i--;
                }
            }
        }
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            DrmInitData.SchemeData schemeData = (DrmInitData.SchemeData) arrayList.get(size);
            if (!schemeData.hasData()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (((DrmInitData.SchemeData) arrayList.get(i)).canReplace(schemeData)) {
                        arrayList.remove(size);
                        break;
                    }
                }
            }
        }
    }

    private static void fillInClearKeyInformation(ArrayList arrayList) {
        String str;
        int i = 0;
        while (true) {
            if (i >= arrayList.size()) {
                str = null;
                break;
            }
            DrmInitData.SchemeData schemeData = (DrmInitData.SchemeData) arrayList.get(i);
            if (C.CLEARKEY_UUID.equals(schemeData.uuid) && (str = schemeData.licenseServerUrl) != null) {
                arrayList.remove(i);
                break;
            }
            i++;
        }
        if (str == null) {
            return;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            DrmInitData.SchemeData schemeData2 = (DrmInitData.SchemeData) arrayList.get(i2);
            if (C.COMMON_PSSH_UUID.equals(schemeData2.uuid) && schemeData2.licenseServerUrl == null) {
                arrayList.set(i2, new DrmInitData.SchemeData(C.CLEARKEY_UUID, str, schemeData2.mimeType, schemeData2.data));
            }
        }
    }

    private static String getSampleMimeType(String str, String str2) {
        if (MimeTypes.isAudio(str)) {
            return MimeTypes.getAudioMediaMimeType(str2);
        }
        if (MimeTypes.isVideo(str)) {
            return MimeTypes.getVideoMediaMimeType(str2);
        }
        if (MimeTypes.isText(str) || MimeTypes.isImage(str)) {
            return str;
        }
        if (!"application/mp4".equals(str)) {
            return null;
        }
        String mediaMimeType = MimeTypes.getMediaMimeType(str2);
        return "text/vtt".equals(mediaMimeType) ? "application/x-mp4-vtt" : mediaMimeType;
    }

    private static String checkLanguageConsistency(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        Assertions.checkState(str.equals(str2));
        return str;
    }

    private static int checkContentTypeConsistency(int i, int i2) {
        if (i == -1) {
            return i2;
        }
        if (i2 == -1) {
            return i;
        }
        Assertions.checkState(i == i2);
        return i;
    }

    protected static Descriptor parseDescriptor(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String string = parseString(xmlPullParser, "schemeIdUri", _UrlKt.FRAGMENT_ENCODE_SET);
        String string2 = parseString(xmlPullParser, "value", null);
        String string3 = parseString(xmlPullParser, "id", null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return new Descriptor(string, string2, string3);
    }

    protected static int parseCea608AccessibilityChannel(List list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri) && (str = descriptor.value) != null) {
                Matcher matcher = CEA_608_ACCESSIBILITY_PATTERN.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                Log.w("MpdParser", "Unable to parse CEA-608 channel number from: " + descriptor.value);
            }
        }
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-708:2015".equals(descriptor.schemeIdUri) && (str = descriptor.value) != null) {
                Matcher matcher = CEA_708_ACCESSIBILITY_PATTERN.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                Log.w("MpdParser", "Unable to parse CEA-708 service block number from: " + descriptor.value);
            }
        }
        return -1;
    }

    protected static String parseEac3SupplementalProperties(List list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            String str = descriptor.schemeIdUri;
            if (!"tag:dolby.com,2018:dash:EC3_ExtensionType:2018".equals(str) || !"JOC".equals(descriptor.value)) {
                if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(str) && "ec+3".equals(descriptor.value)) {
                    return "audio/eac3-joc";
                }
            } else {
                return "audio/eac3-joc";
            }
        }
        return "audio/eac3";
    }

    protected static float parseFrameRate(XmlPullParser xmlPullParser, float f) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "frameRate");
        if (attributeValue != null) {
            Matcher matcher = FRAME_RATE_PATTERN.matcher(attributeValue);
            if (matcher.matches()) {
                int i = Integer.parseInt(matcher.group(1));
                String strGroup = matcher.group(2);
                return !TextUtils.isEmpty(strGroup) ? i / Integer.parseInt(strGroup) : i;
            }
        }
        return f;
    }

    protected static long parseDuration(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Util.parseXsDuration(attributeValue);
    }

    protected static long parseDateTime(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Util.parseXsDateTime(attributeValue);
    }

    protected static String parseText(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String text = _UrlKt.FRAGMENT_ENCODE_SET;
        do {
            xmlPullParser.next();
            if (xmlPullParser.getEventType() == 4) {
                text = xmlPullParser.getText();
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return text;
    }

    protected static int parseInt(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? i : Integer.parseInt(attributeValue);
    }

    protected static long parseLong(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Long.parseLong(attributeValue);
    }

    protected static float parseFloat(XmlPullParser xmlPullParser, String str, float f) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? f : Float.parseFloat(attributeValue);
    }

    protected static String parseString(XmlPullParser xmlPullParser, String str, String str2) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    protected static int parseMpegChannelConfiguration(XmlPullParser xmlPullParser) {
        int i = parseInt(xmlPullParser, "value", -1);
        if (i >= 0) {
            int[] iArr = MPEG_CHANNEL_CONFIGURATION_MAPPING;
            if (i < iArr.length) {
                return iArr[i];
            }
        }
        return -1;
    }

    protected static int parseDtsChannelConfiguration(XmlPullParser xmlPullParser) {
        int i = parseInt(xmlPullParser, "value", -1);
        if (i <= 0 || i >= 33) {
            return -1;
        }
        return i;
    }

    protected static int parseDtsxChannelConfiguration(XmlPullParser xmlPullParser) {
        int iBitCount;
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null || (iBitCount = Integer.bitCount(Integer.parseInt(attributeValue, 16))) == 0) {
            return -1;
        }
        return iBitCount;
    }

    protected static int parseDolbyChannelConfiguration(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null) {
            return -1;
        }
        String lowerCase = Ascii.toLowerCase(attributeValue);
        lowerCase.getClass();
        switch (lowerCase) {
            case "4000":
                return 1;
            case "a000":
                return 2;
            case "f801":
                return 6;
            case "fa01":
                return 8;
            default:
                return -1;
        }
    }

    protected static long parseLastSegmentNumberSupplementalProperty(List list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/last-segment-number", descriptor.schemeIdUri)) {
                return Long.parseLong(descriptor.value);
            }
        }
        return -1L;
    }

    private boolean isDvbProfileDeclared(String[] strArr) {
        for (String str : strArr) {
            if (str.startsWith("urn:dvb:dash:profile:dvb-dash:")) {
                return true;
            }
        }
        return false;
    }

    protected static final class RepresentationInfo {
        public final ImmutableList baseUrls;
        public final ArrayList drmSchemeDatas;
        public final String drmSchemeType;
        public final List essentialProperties;
        public final Format format;
        public final ArrayList inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;
        public final List supplementalProperties;

        public RepresentationInfo(Format format, List list, SegmentBase segmentBase, String str, ArrayList arrayList, ArrayList arrayList2, List list2, List list3, long j) {
            this.format = format;
            this.baseUrls = ImmutableList.copyOf((Collection) list);
            this.segmentBase = segmentBase;
            this.drmSchemeType = str;
            this.drmSchemeDatas = arrayList;
            this.inbandEventStreams = arrayList2;
            this.essentialProperties = list2;
            this.supplementalProperties = list3;
            this.revisionId = j;
        }
    }
}
