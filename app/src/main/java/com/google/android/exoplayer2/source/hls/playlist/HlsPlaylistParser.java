package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.hls.HlsTrackMetadataEntry;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.internal.url._UrlKt;

public final class HlsPlaylistParser implements ParsingLoadable.Parser {
    private final HlsMultivariantPlaylist multivariantPlaylist;
    private final HlsMediaPlaylist previousMediaPlaylist;
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_VIDEO = Pattern.compile("VIDEO=\"(.+?)\"");
    private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
    private static final Pattern REGEX_SUBTITLES = Pattern.compile("SUBTITLES=\"(.+?)\"");
    private static final Pattern REGEX_CLOSED_CAPTIONS = Pattern.compile("CLOSED-CAPTIONS=\"(.+?)\"");
    private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_CHANNELS = Pattern.compile("CHANNELS=\"(.+?)\"");
    private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
    private static final Pattern REGEX_MIME = Pattern.compile("MIME=\"(.+?)\"");
    private static final Pattern REGEX_CACHED = Pattern.compile("CACHED=\"(.+?)\"");
    private static final Pattern REGEX_DOC_ID = Pattern.compile("DOCID=\"(.+?)\"");
    private static final Pattern REGEX_DOC_FILENAME = Pattern.compile("DOCFILENAME=\"(.+?)\"");
    private static final Pattern REGEX_ACCOUNT = Pattern.compile("ACCOUNT=\"(.+?)\"");
    private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
    private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final Pattern REGEX_ATTR_DURATION = Pattern.compile("DURATION=([\\d\\.]+)\\b");
    private static final Pattern REGEX_PART_TARGET_DURATION = Pattern.compile("PART-TARGET=([\\d\\.]+)\\b");
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_CAN_SKIP_UNTIL = Pattern.compile("CAN-SKIP-UNTIL=([\\d\\.]+)\\b");
    private static final Pattern REGEX_CAN_SKIP_DATE_RANGES = compileBooleanAttrPattern("CAN-SKIP-DATERANGES");
    private static final Pattern REGEX_SKIPPED_SEGMENTS = Pattern.compile("SKIPPED-SEGMENTS=(\\d+)\\b");
    private static final Pattern REGEX_HOLD_BACK = Pattern.compile("[:|,]HOLD-BACK=([\\d\\.]+)\\b");
    private static final Pattern REGEX_PART_HOLD_BACK = Pattern.compile("PART-HOLD-BACK=([\\d\\.]+)\\b");
    private static final Pattern REGEX_CAN_BLOCK_RELOAD = compileBooleanAttrPattern("CAN-BLOCK-RELOAD");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
    private static final Pattern REGEX_LAST_MSN = Pattern.compile("LAST-MSN=(\\d+)\\b");
    private static final Pattern REGEX_LAST_PART = Pattern.compile("LAST-PART=(\\d+)\\b");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_BYTERANGE_START = Pattern.compile("BYTERANGE-START=(\\d+)\\b");
    private static final Pattern REGEX_BYTERANGE_LENGTH = Pattern.compile("BYTERANGE-LENGTH=(\\d+)\\b");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_PRELOAD_HINT_TYPE = Pattern.compile("TYPE=(PART|MAP)");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_CHARACTERISTICS = Pattern.compile("CHARACTERISTICS=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_INDEPENDENT = compileBooleanAttrPattern("INDEPENDENT");
    private static final Pattern REGEX_GAP = compileBooleanAttrPattern("GAP");
    private static final Pattern REGEX_PRECISE = compileBooleanAttrPattern("PRECISE");
    private static final Pattern REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
    private static final Pattern REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
    private static final Pattern REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");

    public static final class DeltaUpdateException extends IOException {
    }

    public HlsPlaylistParser() {
        this(HlsMultivariantPlaylist.EMPTY, null);
    }

    public HlsPlaylistParser(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist) {
        this.multivariantPlaylist = hlsMultivariantPlaylist;
        this.previousMediaPlaylist = hlsMediaPlaylist;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws ParserException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayDeque arrayDeque = new ArrayDeque();
        try {
            if (!checkPlaylistHeader(bufferedReader)) {
                throw ParserException.createForMalformedManifest("Input does not start with the #EXTM3U header.", null);
            }
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String strTrim = line.trim();
                    if (!strTrim.isEmpty()) {
                        if (strTrim.startsWith("#EXT-X-STREAM-INF")) {
                            arrayDeque.add(strTrim);
                            HlsMultivariantPlaylist multivariantPlaylist = parseMultivariantPlaylist(new LineIterator(arrayDeque, bufferedReader), uri.toString());
                            Util.closeQuietly(bufferedReader);
                            return multivariantPlaylist;
                        }
                        if (!strTrim.startsWith("#EXT-X-TARGETDURATION") && !strTrim.startsWith("#EXT-X-MEDIA-SEQUENCE") && !strTrim.startsWith("#EXTINF") && !strTrim.startsWith("#EXT-X-KEY") && !strTrim.startsWith("#EXT-X-BYTERANGE") && !strTrim.equals("#EXT-X-DISCONTINUITY") && !strTrim.equals("#EXT-X-DISCONTINUITY-SEQUENCE") && !strTrim.equals("#EXT-X-ENDLIST")) {
                            arrayDeque.add(strTrim);
                        }
                        arrayDeque.add(strTrim);
                        HlsMediaPlaylist mediaPlaylist = parseMediaPlaylist(this.multivariantPlaylist, this.previousMediaPlaylist, new LineIterator(arrayDeque, bufferedReader), uri.toString());
                        Util.closeQuietly(bufferedReader);
                        return mediaPlaylist;
                    }
                } else {
                    Util.closeQuietly(bufferedReader);
                    throw ParserException.createForMalformedManifest("Failed to parse the playlist, could not identify any tags.", null);
                }
            }
        } catch (Throwable th) {
            Util.closeQuietly(bufferedReader);
            throw th;
        }
    }

    private static boolean checkPlaylistHeader(BufferedReader bufferedReader) throws IOException {
        int i = bufferedReader.read();
        if (i == 239) {
            if (bufferedReader.read() != 187 || bufferedReader.read() != 191) {
                return false;
            }
            i = bufferedReader.read();
        }
        int iSkipIgnorableWhitespace = skipIgnorableWhitespace(bufferedReader, true, i);
        for (int i2 = 0; i2 < 7; i2++) {
            if (iSkipIgnorableWhitespace != "#EXTM3U".charAt(i2)) {
                return false;
            }
            iSkipIgnorableWhitespace = bufferedReader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(bufferedReader, false, iSkipIgnorableWhitespace));
    }

    private static int skipIgnorableWhitespace(BufferedReader bufferedReader, boolean z, int i) throws IOException {
        while (i != -1 && Character.isWhitespace(i) && (z || !Util.isLinebreak(i))) {
            i = bufferedReader.read();
        }
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:83:0x0361  */
    /* JADX WARN: Failed to find 'out' block for switch in B:100:0x038f. Please report as an issue. */
    private static HlsMultivariantPlaylist parseMultivariantPlaylist(LineIterator lineIterator, String str) throws ParserException {
        ArrayList arrayList;
        String mediaMimeType;
        ArrayList arrayList2;
        int i;
        String str2;
        ArrayList arrayList3;
        String mediaMimeType2;
        int i2;
        int i3;
        Uri uriResolveToUri;
        ArrayList arrayList4;
        HashMap map;
        int i4;
        String str3 = str;
        HashMap map2 = new HashMap();
        HashMap map3 = new HashMap();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        ArrayList arrayList10 = new ArrayList();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        boolean z = false;
        boolean z2 = false;
        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList12.add(next);
            }
            boolean zStartsWith = next.startsWith("#EXT-X-I-FRAME-STREAM-INF");
            ArrayList arrayList13 = arrayList9;
            if (next.startsWith("#EXT-X-DEFINE")) {
                map3.put(parseStringAttr(next, REGEX_NAME, map3), parseStringAttr(next, REGEX_VALUE, map3));
            } else {
                if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                    ArrayList arrayList14 = arrayList5;
                    map = map2;
                    arrayList4 = arrayList14;
                    z2 = true;
                } else if (next.startsWith("#EXT-X-MEDIA")) {
                    arrayList10.add(next);
                } else if (next.startsWith("#EXT-X-SESSION-KEY")) {
                    DrmInitData.SchemeData drmSchemeData = parseDrmSchemeData(next, parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", map3), map3);
                    if (drmSchemeData != null) {
                        arrayList11.add(new DrmInitData(parseEncryptionScheme(parseStringAttr(next, REGEX_METHOD, map3)), drmSchemeData));
                    }
                } else if (next.startsWith("#EXT-X-STREAM-INF") || zStartsWith) {
                    boolean zContains = z | next.contains("CLOSED-CAPTIONS=NONE");
                    int i5 = zStartsWith ? 16384 : 0;
                    int intAttr = parseIntAttr(next, REGEX_BANDWIDTH);
                    int optionalIntAttr = parseOptionalIntAttr(next, REGEX_AVERAGE_BANDWIDTH, -1);
                    String optionalStringAttr = parseOptionalStringAttr(next, REGEX_CODECS, map3);
                    String optionalStringAttr2 = parseOptionalStringAttr(next, REGEX_MIME, map3);
                    boolean z3 = z2;
                    boolean zEquals = TextUtils.equals(parseOptionalStringAttr(next, REGEX_CACHED, map3), "true");
                    String optionalStringAttr3 = parseOptionalStringAttr(next, REGEX_DOC_ID, map3);
                    String optionalStringAttr4 = parseOptionalStringAttr(next, REGEX_DOC_FILENAME, map3);
                    String optionalStringAttr5 = parseOptionalStringAttr(next, REGEX_ACCOUNT, map3);
                    String optionalStringAttr6 = parseOptionalStringAttr(next, REGEX_RESOLUTION, map3);
                    if (optionalStringAttr6 != null) {
                        String[] strArrSplit = Util.split(optionalStringAttr6, "x");
                        int i6 = Integer.parseInt(strArrSplit[0]);
                        int i7 = Integer.parseInt(strArrSplit[1]);
                        if (i6 <= 0 || i7 <= 0) {
                            i7 = -1;
                            i4 = -1;
                        } else {
                            i4 = i6;
                        }
                        i3 = i7;
                        i2 = i4;
                    } else {
                        i2 = -1;
                        i3 = -1;
                    }
                    ArrayList arrayList15 = arrayList5;
                    String optionalStringAttr7 = parseOptionalStringAttr(next, REGEX_FRAME_RATE, map3);
                    float f = optionalStringAttr7 != null ? Float.parseFloat(optionalStringAttr7) : -1.0f;
                    String optionalStringAttr8 = parseOptionalStringAttr(next, REGEX_VIDEO, map3);
                    String optionalStringAttr9 = parseOptionalStringAttr(next, REGEX_AUDIO, map3);
                    String optionalStringAttr10 = parseOptionalStringAttr(next, REGEX_SUBTITLES, map3);
                    String optionalStringAttr11 = parseOptionalStringAttr(next, REGEX_CLOSED_CAPTIONS, map3);
                    if (zStartsWith) {
                        uriResolveToUri = UriUtil.resolveToUri(str3, parseStringAttr(next, REGEX_URI, map3));
                    } else {
                        if (!lineIterator.hasNext()) {
                            throw ParserException.createForMalformedManifest("#EXT-X-STREAM-INF must be followed by another line", null);
                        }
                        uriResolveToUri = UriUtil.resolveToUri(str3, replaceVariableReferences(lineIterator.next(), map3));
                    }
                    Uri uri = uriResolveToUri;
                    HlsMultivariantPlaylist.Variant variant = new HlsMultivariantPlaylist.Variant(uri, new Format.Builder().setId(arrayList15.size()).setContainerMimeType("application/x-mpegURL").setCodecs(optionalStringAttr).setSampleMimeType(optionalStringAttr2).setAverageBitrate(optionalIntAttr).setPeakBitrate(intAttr).setWidth(i2).setHeight(i3).setFrameRate(f).setRoleFlags(i5).setCached(zEquals).setDocumentId(optionalStringAttr3).setDocumentFilename(optionalStringAttr4).setCurrentAccount(optionalStringAttr5).build(), optionalStringAttr8, optionalStringAttr9, optionalStringAttr10, optionalStringAttr11);
                    arrayList4 = arrayList15;
                    arrayList4.add(variant);
                    map = map2;
                    ArrayList arrayList16 = (ArrayList) map.get(uri);
                    if (arrayList16 == null) {
                        arrayList16 = new ArrayList();
                        map.put(uri, arrayList16);
                    }
                    arrayList16.add(new HlsTrackMetadataEntry.VariantInfo(optionalIntAttr, intAttr, optionalStringAttr8, optionalStringAttr9, optionalStringAttr10, optionalStringAttr11));
                    z = zContains;
                    z2 = z3;
                }
                HashMap map4 = map;
                arrayList5 = arrayList4;
                map2 = map4;
                arrayList9 = arrayList13;
                arrayList12 = arrayList12;
                arrayList11 = arrayList11;
                arrayList8 = arrayList8;
                arrayList7 = arrayList7;
                arrayList6 = arrayList6;
                arrayList10 = arrayList10;
            }
            ArrayList arrayList17 = arrayList5;
            map = map2;
            arrayList4 = arrayList17;
            HashMap map5 = map;
            arrayList5 = arrayList4;
            map2 = map5;
            arrayList9 = arrayList13;
            arrayList12 = arrayList12;
            arrayList11 = arrayList11;
            arrayList8 = arrayList8;
            arrayList7 = arrayList7;
            arrayList6 = arrayList6;
            arrayList10 = arrayList10;
        }
        ArrayList arrayList18 = arrayList5;
        HashMap map6 = map2;
        ArrayList arrayList19 = arrayList10;
        ArrayList arrayList20 = arrayList6;
        ArrayList arrayList21 = arrayList7;
        ArrayList arrayList22 = arrayList8;
        ArrayList arrayList23 = arrayList9;
        ArrayList arrayList24 = arrayList12;
        boolean z4 = z;
        ArrayList arrayList25 = arrayList11;
        boolean z5 = z2;
        ArrayList arrayList26 = new ArrayList();
        HashSet hashSet = new HashSet();
        for (int i8 = 0; i8 < arrayList18.size(); i8++) {
            HlsMultivariantPlaylist.Variant variant2 = (HlsMultivariantPlaylist.Variant) arrayList18.get(i8);
            if (hashSet.add(variant2.url)) {
                Assertions.checkState(variant2.format.metadata == null);
                arrayList26.add(variant2.copyWithFormat(variant2.format.buildUpon().setMetadata(new Metadata(new HlsTrackMetadataEntry(null, null, (List) Assertions.checkNotNull((ArrayList) map6.get(variant2.url))))).build()));
            }
        }
        ArrayList arrayList27 = null;
        Format formatBuild = null;
        int i9 = 0;
        while (i9 < arrayList19.size()) {
            ArrayList arrayList28 = arrayList19;
            String str4 = (String) arrayList28.get(i9);
            String stringAttr = parseStringAttr(str4, REGEX_GROUP_ID, map3);
            String stringAttr2 = parseStringAttr(str4, REGEX_NAME, map3);
            Format.Builder language = new Format.Builder().setId(stringAttr + ":" + stringAttr2).setLabel(stringAttr2).setContainerMimeType("application/x-mpegURL").setSelectionFlags(parseSelectionFlags(str4)).setRoleFlags(parseRoleFlags(str4, map3)).setLanguage(parseOptionalStringAttr(str4, REGEX_LANGUAGE, map3));
            String optionalStringAttr12 = parseOptionalStringAttr(str4, REGEX_URI, map3);
            Uri uriResolveToUri2 = optionalStringAttr12 == null ? null : UriUtil.resolveToUri(str3, optionalStringAttr12);
            ArrayList arrayList29 = arrayList27;
            Metadata metadata = new Metadata(new HlsTrackMetadataEntry(stringAttr, stringAttr2, Collections.EMPTY_LIST));
            String stringAttr3 = parseStringAttr(str4, REGEX_TYPE, map3);
            stringAttr3.getClass();
            switch (stringAttr3) {
                case "SUBTITLES":
                    arrayList = arrayList21;
                    HlsMultivariantPlaylist.Variant variantWithSubtitleGroup = getVariantWithSubtitleGroup(arrayList18, stringAttr);
                    if (variantWithSubtitleGroup != null) {
                        String codecsOfType = Util.getCodecsOfType(variantWithSubtitleGroup.format.codecs, 3);
                        language.setCodecs(codecsOfType);
                        mediaMimeType = MimeTypes.getMediaMimeType(codecsOfType);
                    } else {
                        mediaMimeType = null;
                    }
                    if (mediaMimeType == null) {
                        mediaMimeType = "text/vtt";
                    }
                    language.setSampleMimeType(mediaMimeType).setMetadata(metadata);
                    if (uriResolveToUri2 != null) {
                        arrayList2 = arrayList22;
                        arrayList2.add(new HlsMultivariantPlaylist.Rendition(uriResolveToUri2, language.build(), stringAttr, stringAttr2));
                    } else {
                        arrayList2 = arrayList22;
                        Log.w("HlsPlaylistParser", "EXT-X-MEDIA tag with missing mandatory URI attribute: skipping");
                    }
                    arrayList3 = arrayList29;
                    break;
                case "CLOSED-CAPTIONS":
                    arrayList = arrayList21;
                    String stringAttr4 = parseStringAttr(str4, REGEX_INSTREAM_ID, map3);
                    if (stringAttr4.startsWith("CC")) {
                        i = Integer.parseInt(stringAttr4.substring(2));
                        str2 = "application/cea-608";
                    } else {
                        i = Integer.parseInt(stringAttr4.substring(7));
                        str2 = "application/cea-708";
                    }
                    arrayList3 = arrayList29 == null ? new ArrayList() : arrayList29;
                    language.setSampleMimeType(str2).setAccessibilityChannel(i);
                    arrayList3.add(language.build());
                    arrayList2 = arrayList22;
                    break;
                case "AUDIO":
                    arrayList20 = arrayList20;
                    HlsMultivariantPlaylist.Variant variantWithAudioGroup = getVariantWithAudioGroup(arrayList18, stringAttr);
                    if (variantWithAudioGroup != null) {
                        String codecsOfType2 = Util.getCodecsOfType(variantWithAudioGroup.format.codecs, 1);
                        language.setCodecs(codecsOfType2);
                        mediaMimeType2 = MimeTypes.getMediaMimeType(codecsOfType2);
                    } else {
                        mediaMimeType2 = null;
                    }
                    String optionalStringAttr13 = parseOptionalStringAttr(str4, REGEX_CHANNELS, map3);
                    if (optionalStringAttr13 != null) {
                        language.setChannelCount(Integer.parseInt(Util.splitAtFirst(optionalStringAttr13, "/")[0]));
                        if ("audio/eac3".equals(mediaMimeType2) && optionalStringAttr13.endsWith("/JOC")) {
                            language.setCodecs("ec+3");
                            mediaMimeType2 = "audio/eac3-joc";
                        }
                    }
                    language.setSampleMimeType(mediaMimeType2);
                    if (uriResolveToUri2 == null) {
                        arrayList = arrayList21;
                        if (variantWithAudioGroup != null) {
                            arrayList3 = arrayList29;
                            formatBuild = language.build();
                            arrayList2 = arrayList22;
                        }
                        break;
                    } else {
                        language.setMetadata(metadata);
                        arrayList = arrayList21;
                        arrayList.add(new HlsMultivariantPlaylist.Rendition(uriResolveToUri2, language.build(), stringAttr, stringAttr2));
                    }
                    arrayList2 = arrayList22;
                    arrayList3 = arrayList29;
                    break;
                case "VIDEO":
                    HlsMultivariantPlaylist.Variant variantWithVideoGroup = getVariantWithVideoGroup(arrayList18, stringAttr);
                    if (variantWithVideoGroup != null) {
                        Format format = variantWithVideoGroup.format;
                        String codecsOfType3 = Util.getCodecsOfType(format.codecs, 2);
                        language.setCodecs(codecsOfType3).setSampleMimeType(MimeTypes.getMediaMimeType(codecsOfType3)).setWidth(format.width).setHeight(format.height).setFrameRate(format.frameRate);
                    }
                    if (uriResolveToUri2 != null) {
                        language.setMetadata(metadata);
                        arrayList20.add(new HlsMultivariantPlaylist.Rendition(uriResolveToUri2, language.build(), stringAttr, stringAttr2));
                        break;
                    }
                default:
                    arrayList2 = arrayList22;
                    arrayList = arrayList21;
                    arrayList3 = arrayList29;
                    break;
            }
            i9++;
            str3 = str;
            arrayList21 = arrayList;
            arrayList19 = arrayList28;
            arrayList22 = arrayList2;
            arrayList27 = arrayList3;
        }
        return new HlsMultivariantPlaylist(str, arrayList24, arrayList26, arrayList20, arrayList21, arrayList22, arrayList23, formatBuild, z4 ? Collections.EMPTY_LIST : arrayList27, z5, map3, arrayList25);
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithAudioGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.audioGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithVideoGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.videoGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithSubtitleGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.subtitleGroupId)) {
                return variant;
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:114:0x0322 A[PHI: r13
  0x0322: PHI (r13v7 java.lang.String) = (r13v5 java.lang.String), (r13v2 java.lang.String) binds: [B:118:0x0331, B:112:0x0315] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:228:0x0624  */
    /* JADX WARN: Code duplicated, block: B:230:0x0631  */
    /* JADX WARN: Code duplicated, block: B:233:0x0647  */
    private static HlsMediaPlaylist parseMediaPlaylist(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist, LineIterator lineIterator, String str) throws DeltaUpdateException, ParserException {
        String str2;
        HlsMediaPlaylist.Segment segment;
        int i;
        long j;
        long j2;
        long j3;
        HlsMediaPlaylist.Segment segment2;
        long j4;
        boolean z;
        DrmInitData drmInitData;
        DrmInitData playlistProtectionSchemes;
        HlsMediaPlaylist.Segment segment3;
        hlsMultivariantPlaylist = hlsMultivariantPlaylist;
        HlsMediaPlaylist hlsMediaPlaylist2 = hlsMediaPlaylist;
        boolean z2 = hlsMultivariantPlaylist.hasIndependentSegments;
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        HlsMediaPlaylist.ServerControl serverControl = new HlsMediaPlaylist.ServerControl(-9223372036854775807L, false, -9223372036854775807L, -9223372036854775807L, false);
        TreeMap treeMap = new TreeMap();
        boolean z3 = z2;
        long j5 = -9223372036854775807L;
        long doubleAttr = -9223372036854775807L;
        long j6 = 0;
        long j7 = 0;
        long jMsToUs = 0;
        long j8 = 0;
        long longAttr = 0;
        long timeSecondsToUs = 0;
        long j9 = 0;
        String optionalStringAttr = _UrlKt.FRAGMENT_ENCODE_SET;
        String str3 = optionalStringAttr;
        boolean optionalBooleanAttribute = false;
        String encryptionScheme = null;
        DrmInitData drmInitData2 = null;
        HlsMediaPlaylist.Part part = null;
        int i2 = 0;
        boolean z4 = false;
        DrmInitData playlistProtectionSchemes2 = null;
        String str4 = null;
        long j10 = -1;
        boolean z5 = false;
        boolean z6 = false;
        int i3 = 0;
        HlsMediaPlaylist.Segment segment4 = null;
        int i4 = 0;
        String stringAttr = null;
        long j11 = -1;
        boolean z7 = false;
        long intAttr = -9223372036854775807L;
        long j12 = 0;
        int intAttr2 = 1;
        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList4.add(next);
            }
            if (next.startsWith("#EXT-X-PLAYLIST-TYPE")) {
                String stringAttr2 = parseStringAttr(next, REGEX_PLAYLIST_TYPE, map);
                if ("VOD".equals(stringAttr2)) {
                    i2 = 1;
                } else if ("EVENT".equals(stringAttr2)) {
                    i2 = 2;
                }
            } else if (next.equals("#EXT-X-I-FRAMES-ONLY")) {
                z7 = true;
            } else {
                if (next.startsWith("#EXT-X-START")) {
                    ArrayList arrayList5 = arrayList4;
                    serverControl = serverControl;
                    long doubleAttr2 = (long) (parseDoubleAttr(next, REGEX_TIME_OFFSET) * 1000000.0d);
                    optionalBooleanAttribute = parseOptionalBooleanAttribute(next, REGEX_PRECISE, false);
                    arrayList4 = arrayList5;
                    j5 = doubleAttr2;
                } else {
                    ArrayList arrayList6 = arrayList4;
                    serverControl = serverControl;
                    if (next.startsWith("#EXT-X-SERVER-CONTROL")) {
                        serverControl = parseServerControl(next);
                        arrayList4 = arrayList6;
                    } else if (next.startsWith("#EXT-X-PART-INF")) {
                        doubleAttr = (long) (parseDoubleAttr(next, REGEX_PART_TARGET_DURATION) * 1000000.0d);
                        arrayList4 = arrayList6;
                    } else if (next.startsWith("#EXT-X-MAP")) {
                        String stringAttr3 = parseStringAttr(next, REGEX_URI, map);
                        boolean z8 = optionalBooleanAttribute;
                        String optionalStringAttr2 = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, map);
                        if (optionalStringAttr2 != null) {
                            String[] strArrSplit = Util.split(optionalStringAttr2, "@");
                            j10 = Long.parseLong(strArrSplit[0]);
                            if (strArrSplit.length > 1) {
                                j6 = Long.parseLong(strArrSplit[1]);
                            }
                        }
                        long j13 = j10;
                        long j14 = j13 == j11 ? 0L : j6;
                        if (stringAttr != null && str4 == null) {
                            throw ParserException.createForMalformedManifest("The encryption IV attribute must be present when an initialization segment is encrypted with METHOD=AES-128.", null);
                        }
                        HlsMediaPlaylist.Segment segment5 = new HlsMediaPlaylist.Segment(stringAttr3, j14, j13, stringAttr, str4);
                        String str5 = str4;
                        if (j13 != j11) {
                            j14 += j13;
                        }
                        str4 = str5;
                        arrayList4 = arrayList6;
                        segment4 = segment5;
                        j6 = j14;
                        j10 = j11;
                        serverControl = serverControl;
                        optionalBooleanAttribute = z8;
                    } else {
                        optionalBooleanAttribute = optionalBooleanAttribute;
                        arrayList4 = arrayList6;
                        str4 = str4;
                        stringAttr = stringAttr;
                        if (next.startsWith("#EXT-X-TARGETDURATION")) {
                            intAttr = 1000000 * ((long) parseIntAttr(next, REGEX_TARGET_DURATION));
                        } else if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                            longAttr = parseLongAttr(next, REGEX_MEDIA_SEQUENCE);
                            j12 = longAttr;
                            serverControl = serverControl;
                            optionalBooleanAttribute = optionalBooleanAttribute;
                            arrayList4 = arrayList4;
                        } else if (next.startsWith("#EXT-X-VERSION")) {
                            intAttr2 = parseIntAttr(next, REGEX_VERSION);
                        } else {
                            if (next.startsWith("#EXT-X-DEFINE")) {
                                String optionalStringAttr3 = parseOptionalStringAttr(next, REGEX_IMPORT, map);
                                if (optionalStringAttr3 != null) {
                                    String str6 = (String) hlsMultivariantPlaylist.variableDefinitions.get(optionalStringAttr3);
                                    if (str6 != null) {
                                        map.put(optionalStringAttr3, str6);
                                    }
                                } else {
                                    map.put(parseStringAttr(next, REGEX_NAME, map), parseStringAttr(next, REGEX_VALUE, map));
                                }
                                treeMap = treeMap;
                                str2 = str3;
                            } else if (next.startsWith("#EXTINF")) {
                                timeSecondsToUs = parseTimeSecondsToUs(next, REGEX_MEDIA_DURATION);
                                optionalStringAttr = parseOptionalStringAttr(next, REGEX_MEDIA_TITLE, str3, map);
                            } else {
                                String str7 = str3;
                                if (next.startsWith("#EXT-X-SKIP")) {
                                    int intAttr3 = parseIntAttr(next, REGEX_SKIPPED_SEGMENTS);
                                    Assertions.checkState(hlsMediaPlaylist2 != null && arrayList.isEmpty());
                                    str2 = str7;
                                    int i5 = (int) (j12 - ((HlsMediaPlaylist) Util.castNonNull(hlsMediaPlaylist2)).mediaSequence);
                                    int i6 = intAttr3 + i5;
                                    if (i5 < 0 || i6 > hlsMediaPlaylist2.segments.size()) {
                                        throw new DeltaUpdateException();
                                    }
                                    stringAttr = stringAttr;
                                    str4 = str4;
                                    long j15 = j7;
                                    while (i5 < i6) {
                                        HlsMediaPlaylist.Segment segmentCopyWith = (HlsMediaPlaylist.Segment) hlsMediaPlaylist2.segments.get(i5);
                                        int i7 = i5;
                                        if (j12 != hlsMediaPlaylist2.mediaSequence) {
                                            segmentCopyWith = segmentCopyWith.copyWith(j15, (hlsMediaPlaylist2.discontinuitySequence - i3) + segmentCopyWith.relativeDiscontinuitySequence);
                                        }
                                        arrayList.add(segmentCopyWith);
                                        j8 = j15 + segmentCopyWith.durationUs;
                                        long j16 = segmentCopyWith.byteRangeLength;
                                        if (j16 != j11) {
                                            j6 = segmentCopyWith.byteRangeOffset + j16;
                                        }
                                        int i8 = segmentCopyWith.relativeDiscontinuitySequence;
                                        HlsMediaPlaylist.Segment segment6 = segmentCopyWith.initializationSegment;
                                        drmInitData2 = segmentCopyWith.drmInitData;
                                        String str8 = segmentCopyWith.fullSegmentEncryptionKeyUri;
                                        String str9 = segmentCopyWith.encryptionIV;
                                        int i9 = i6;
                                        if (str9 == null || !str9.equals(Long.toHexString(longAttr))) {
                                            str4 = segmentCopyWith.encryptionIV;
                                        }
                                        longAttr++;
                                        i4 = i8;
                                        segment4 = segment6;
                                        stringAttr = str8;
                                        j15 = j8;
                                        i5 = i7 + 1;
                                        i6 = i9;
                                    }
                                    j7 = j15;
                                } else {
                                    str2 = str7;
                                    if (next.startsWith("#EXT-X-KEY")) {
                                        String stringAttr4 = parseStringAttr(next, REGEX_METHOD, map);
                                        String optionalStringAttr4 = parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", map);
                                        if ("NONE".equals(stringAttr4)) {
                                            treeMap.clear();
                                            drmInitData2 = null;
                                            str4 = null;
                                            stringAttr = null;
                                        } else {
                                            String optionalStringAttr5 = parseOptionalStringAttr(next, REGEX_IV, map);
                                            if ("identity".equals(optionalStringAttr4)) {
                                                if ("AES-128".equals(stringAttr4)) {
                                                    stringAttr = parseStringAttr(next, REGEX_URI, map);
                                                    str4 = optionalStringAttr5;
                                                } else {
                                                    str4 = optionalStringAttr5;
                                                    stringAttr = null;
                                                }
                                            } else {
                                                if (encryptionScheme == null) {
                                                    encryptionScheme = parseEncryptionScheme(stringAttr4);
                                                }
                                                DrmInitData.SchemeData drmSchemeData = parseDrmSchemeData(next, optionalStringAttr4, map);
                                                if (drmSchemeData != null) {
                                                    treeMap.put(optionalStringAttr4, drmSchemeData);
                                                    str4 = optionalStringAttr5;
                                                    drmInitData2 = null;
                                                    stringAttr = null;
                                                } else {
                                                    str4 = optionalStringAttr5;
                                                    stringAttr = null;
                                                }
                                            }
                                        }
                                    } else {
                                        if (next.startsWith("#EXT-X-BYTERANGE")) {
                                            String[] strArrSplit2 = Util.split(parseStringAttr(next, REGEX_BYTERANGE, map), "@");
                                            j10 = Long.parseLong(strArrSplit2[0]);
                                            if (strArrSplit2.length > 1) {
                                                j6 = Long.parseLong(strArrSplit2[1]);
                                            }
                                        } else if (next.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                                            i3 = Integer.parseInt(next.substring(next.indexOf(58) + 1));
                                            hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                            stringAttr = stringAttr;
                                            str4 = str4;
                                            serverControl = serverControl;
                                            optionalBooleanAttribute = optionalBooleanAttribute;
                                            arrayList4 = arrayList4;
                                            str3 = str2;
                                            z6 = true;
                                        } else if (next.equals("#EXT-X-DISCONTINUITY")) {
                                            i4++;
                                        } else if (next.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                                            if (jMsToUs == 0) {
                                                jMsToUs = Util.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + 1))) - j7;
                                            } else {
                                                treeMap = treeMap;
                                            }
                                        } else if (next.equals("#EXT-X-GAP")) {
                                            hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                            stringAttr = stringAttr;
                                            str4 = str4;
                                            serverControl = serverControl;
                                            optionalBooleanAttribute = optionalBooleanAttribute;
                                            arrayList4 = arrayList4;
                                            str3 = str2;
                                            z5 = true;
                                        } else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                                            hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                            stringAttr = stringAttr;
                                            str4 = str4;
                                            serverControl = serverControl;
                                            optionalBooleanAttribute = optionalBooleanAttribute;
                                            arrayList4 = arrayList4;
                                            str3 = str2;
                                            z3 = true;
                                        } else if (next.equals("#EXT-X-ENDLIST")) {
                                            hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                            stringAttr = stringAttr;
                                            str4 = str4;
                                            serverControl = serverControl;
                                            optionalBooleanAttribute = optionalBooleanAttribute;
                                            arrayList4 = arrayList4;
                                            str3 = str2;
                                            z4 = true;
                                        } else if (next.startsWith("#EXT-X-RENDITION-REPORT")) {
                                            treeMap = treeMap;
                                            arrayList3.add(new HlsMediaPlaylist.RenditionReport(Uri.parse(UriUtil.resolve(str, parseStringAttr(next, REGEX_URI, map))), parseOptionalLongAttr(next, REGEX_LAST_MSN, j11), parseOptionalIntAttr(next, REGEX_LAST_PART, -1)));
                                        } else {
                                            treeMap = treeMap;
                                            if (next.startsWith("#EXT-X-PRELOAD-HINT")) {
                                                if (part == null && "PART".equals(parseStringAttr(next, REGEX_PRELOAD_HINT_TYPE, map))) {
                                                    String stringAttr5 = parseStringAttr(next, REGEX_URI, map);
                                                    long optionalLongAttr = parseOptionalLongAttr(next, REGEX_BYTERANGE_START, -1L);
                                                    long optionalLongAttr2 = parseOptionalLongAttr(next, REGEX_BYTERANGE_LENGTH, -1L);
                                                    long j17 = longAttr;
                                                    String segmentEncryptionIV = getSegmentEncryptionIV(j17, stringAttr, str4);
                                                    if (drmInitData2 == null && !treeMap.isEmpty()) {
                                                        DrmInitData.SchemeData[] schemeDataArr = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                        drmInitData2 = new DrmInitData(encryptionScheme, schemeDataArr);
                                                        if (playlistProtectionSchemes2 == null) {
                                                            playlistProtectionSchemes2 = getPlaylistProtectionSchemes(encryptionScheme, schemeDataArr);
                                                        }
                                                    }
                                                    DrmInitData drmInitData3 = drmInitData2;
                                                    if (optionalLongAttr == -1 || optionalLongAttr2 != -1) {
                                                        part = new HlsMediaPlaylist.Part(stringAttr5, segment4, 0L, i4, j8, drmInitData3, stringAttr, segmentEncryptionIV, optionalLongAttr != -1 ? optionalLongAttr : 0L, optionalLongAttr2, false, false, true);
                                                    }
                                                    hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                    stringAttr = stringAttr;
                                                    str4 = str4;
                                                    longAttr = j17;
                                                    drmInitData2 = drmInitData3;
                                                    serverControl = serverControl;
                                                    optionalBooleanAttribute = optionalBooleanAttribute;
                                                    arrayList4 = arrayList4;
                                                    str3 = str2;
                                                    treeMap = treeMap;
                                                    j11 = -1;
                                                }
                                            } else {
                                                j = longAttr;
                                                if (next.startsWith("#EXT-X-PART")) {
                                                    String segmentEncryptionIV2 = getSegmentEncryptionIV(j, stringAttr, str4);
                                                    String stringAttr6 = parseStringAttr(next, REGEX_URI, map);
                                                    long doubleAttr3 = (long) (parseDoubleAttr(next, REGEX_ATTR_DURATION) * 1000000.0d);
                                                    boolean optionalBooleanAttribute2 = parseOptionalBooleanAttribute(next, REGEX_INDEPENDENT, false) | (z3 && arrayList2.isEmpty());
                                                    boolean optionalBooleanAttribute3 = parseOptionalBooleanAttribute(next, REGEX_GAP, false);
                                                    String optionalStringAttr6 = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, map);
                                                    if (optionalStringAttr6 != null) {
                                                        String[] strArrSplit3 = Util.split(optionalStringAttr6, "@");
                                                        long j18 = Long.parseLong(strArrSplit3[0]);
                                                        if (strArrSplit3.length > 1) {
                                                            j9 = Long.parseLong(strArrSplit3[1]);
                                                        }
                                                        j2 = j18;
                                                    } else {
                                                        j2 = -1;
                                                    }
                                                    long j19 = j2 == -1 ? 0L : j9;
                                                    if (drmInitData2 == null && !treeMap.isEmpty()) {
                                                        DrmInitData.SchemeData[] schemeDataArr2 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                        drmInitData2 = new DrmInitData(encryptionScheme, schemeDataArr2);
                                                        if (playlistProtectionSchemes2 == null) {
                                                            playlistProtectionSchemes2 = getPlaylistProtectionSchemes(encryptionScheme, schemeDataArr2);
                                                        }
                                                    }
                                                    DrmInitData drmInitData4 = drmInitData2;
                                                    HlsMediaPlaylist.Segment segment7 = segment4;
                                                    int i10 = i4;
                                                    arrayList2.add(new HlsMediaPlaylist.Part(stringAttr6, segment4, doubleAttr3, i4, j8, drmInitData4, stringAttr, segmentEncryptionIV2, j19, j2, optionalBooleanAttribute3, optionalBooleanAttribute2, false));
                                                    j8 += doubleAttr3;
                                                    if (j2 != -1) {
                                                        j19 += j2;
                                                    }
                                                    j9 = j19;
                                                    segment4 = segment7;
                                                    stringAttr = stringAttr;
                                                    longAttr = j;
                                                    i4 = i10;
                                                    drmInitData2 = drmInitData4;
                                                    str3 = str2;
                                                } else {
                                                    segment = segment4;
                                                    i = i4;
                                                    if (next.startsWith("#")) {
                                                        j7 = j7;
                                                        hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                                                        segment4 = segment;
                                                        stringAttr = stringAttr;
                                                        str4 = str4;
                                                        longAttr = j;
                                                        optionalStringAttr = optionalStringAttr;
                                                        timeSecondsToUs = timeSecondsToUs;
                                                        j7 = j7;
                                                        j6 = j6;
                                                        serverControl = serverControl;
                                                        optionalBooleanAttribute = optionalBooleanAttribute;
                                                        arrayList4 = arrayList4;
                                                        str3 = str2;
                                                        treeMap = treeMap;
                                                        j11 = -1;
                                                        hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                        i4 = i;
                                                    } else {
                                                        long j20 = j7;
                                                        String segmentEncryptionIV3 = getSegmentEncryptionIV(j, stringAttr, str4);
                                                        longAttr = j + 1;
                                                        String strReplaceVariableReferences = replaceVariableReferences(next, map);
                                                        HlsMediaPlaylist.Segment segment8 = (HlsMediaPlaylist.Segment) map2.get(strReplaceVariableReferences);
                                                        if (j10 == -1) {
                                                            segment2 = segment8;
                                                            j4 = 0;
                                                        } else {
                                                            if (z7 && segment == null && segment8 == null) {
                                                                j3 = j6;
                                                                segment8 = new HlsMediaPlaylist.Segment(strReplaceVariableReferences, 0L, j3, null, null);
                                                                map2.put(strReplaceVariableReferences, segment8);
                                                            } else {
                                                                j3 = j6;
                                                            }
                                                            segment2 = segment8;
                                                            j4 = j3;
                                                        }
                                                        if (drmInitData2 != null || treeMap.isEmpty()) {
                                                            z = false;
                                                        } else {
                                                            z = false;
                                                            DrmInitData.SchemeData[] schemeDataArr3 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                            drmInitData2 = new DrmInitData(encryptionScheme, schemeDataArr3);
                                                            if (playlistProtectionSchemes2 == null) {
                                                                playlistProtectionSchemes = getPlaylistProtectionSchemes(encryptionScheme, schemeDataArr3);
                                                                drmInitData = drmInitData2;
                                                            }
                                                            if (segment != null) {
                                                                segment3 = segment;
                                                            } else {
                                                                segment3 = segment2;
                                                            }
                                                            long j21 = timeSecondsToUs;
                                                            arrayList.add(new HlsMediaPlaylist.Segment(strReplaceVariableReferences, segment3, optionalStringAttr, j21, i, j20, drmInitData, stringAttr, segmentEncryptionIV3, j4, j10, z5, arrayList2));
                                                            j8 = j20 + j21;
                                                            arrayList2 = new ArrayList();
                                                            if (j10 != -1) {
                                                                j4 += j10;
                                                            }
                                                            j6 = j4;
                                                            segment4 = segment;
                                                            stringAttr = stringAttr;
                                                            playlistProtectionSchemes2 = playlistProtectionSchemes;
                                                            z5 = z;
                                                            i4 = i;
                                                            drmInitData2 = drmInitData;
                                                            j7 = j8;
                                                            timeSecondsToUs = 0;
                                                            optionalStringAttr = str2;
                                                            str3 = optionalStringAttr;
                                                            j10 = -1;
                                                        }
                                                        drmInitData = drmInitData2;
                                                        playlistProtectionSchemes = playlistProtectionSchemes2;
                                                        if (segment != null) {
                                                            segment3 = segment;
                                                        } else {
                                                            segment3 = segment2;
                                                        }
                                                        long j22 = timeSecondsToUs;
                                                        arrayList.add(new HlsMediaPlaylist.Segment(strReplaceVariableReferences, segment3, optionalStringAttr, j22, i, j20, drmInitData, stringAttr, segmentEncryptionIV3, j4, j10, z5, arrayList2));
                                                        j8 = j20 + j22;
                                                        arrayList2 = new ArrayList();
                                                        if (j10 != -1) {
                                                            j4 += j10;
                                                        }
                                                        j6 = j4;
                                                        segment4 = segment;
                                                        stringAttr = stringAttr;
                                                        playlistProtectionSchemes2 = playlistProtectionSchemes;
                                                        z5 = z;
                                                        i4 = i;
                                                        drmInitData2 = drmInitData;
                                                        j7 = j8;
                                                        timeSecondsToUs = 0;
                                                        optionalStringAttr = str2;
                                                        str3 = optionalStringAttr;
                                                        j10 = -1;
                                                    }
                                                }
                                                j11 = -1;
                                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                            }
                                        }
                                        stringAttr = stringAttr;
                                        str4 = str4;
                                    }
                                }
                                serverControl = serverControl;
                                optionalBooleanAttribute = optionalBooleanAttribute;
                                arrayList4 = arrayList4;
                                str3 = str2;
                            }
                            segment = segment4;
                            i = i4;
                            j = longAttr;
                            hlsMultivariantPlaylist = hlsMultivariantPlaylist;
                            segment4 = segment;
                            stringAttr = stringAttr;
                            str4 = str4;
                            longAttr = j;
                            optionalStringAttr = optionalStringAttr;
                            timeSecondsToUs = timeSecondsToUs;
                            j7 = j7;
                            j6 = j6;
                            serverControl = serverControl;
                            optionalBooleanAttribute = optionalBooleanAttribute;
                            arrayList4 = arrayList4;
                            str3 = str2;
                            treeMap = treeMap;
                            j11 = -1;
                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                            i4 = i;
                        }
                        serverControl = serverControl;
                        optionalBooleanAttribute = optionalBooleanAttribute;
                        arrayList4 = arrayList4;
                    }
                }
                serverControl = serverControl;
            }
        }
        boolean z9 = optionalBooleanAttribute;
        ArrayList arrayList7 = arrayList4;
        HlsMediaPlaylist.ServerControl serverControl2 = serverControl;
        HashMap map3 = new HashMap();
        for (int i11 = 0; i11 < arrayList3.size(); i11++) {
            HlsMediaPlaylist.RenditionReport renditionReport = (HlsMediaPlaylist.RenditionReport) arrayList3.get(i11);
            long size = renditionReport.lastMediaSequence;
            if (size == -1) {
                size = (j12 + ((long) arrayList.size())) - (arrayList2.isEmpty() ? 1L : 0L);
            }
            int size2 = renditionReport.lastPartIndex;
            if (size2 == -1 && doubleAttr != -9223372036854775807L) {
                size2 = (arrayList2.isEmpty() ? ((HlsMediaPlaylist.Segment) Iterables.getLast(arrayList)).parts : arrayList2).size() - 1;
            }
            Uri uri = renditionReport.playlistUri;
            map3.put(uri, new HlsMediaPlaylist.RenditionReport(uri, size, size2));
        }
        if (part != null) {
            arrayList2.add(part);
        }
        return new HlsMediaPlaylist(i2, str, arrayList7, j5, z9, jMsToUs, z6, i3, j12, intAttr2, intAttr, doubleAttr, z3, z4, jMsToUs != 0, playlistProtectionSchemes2, arrayList, arrayList2, serverControl2, map3);
    }

    private static DrmInitData getPlaylistProtectionSchemes(String str, DrmInitData.SchemeData[] schemeDataArr) {
        DrmInitData.SchemeData[] schemeDataArr2 = new DrmInitData.SchemeData[schemeDataArr.length];
        for (int i = 0; i < schemeDataArr.length; i++) {
            schemeDataArr2[i] = schemeDataArr[i].copyWithData(null);
        }
        return new DrmInitData(str, schemeDataArr2);
    }

    private static String getSegmentEncryptionIV(long j, String str, String str2) {
        if (str == null) {
            return null;
        }
        return str2 != null ? str2 : Long.toHexString(j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6 */
    private static int parseSelectionFlags(String str) {
        boolean optionalBooleanAttribute = parseOptionalBooleanAttribute(str, REGEX_DEFAULT, false);
        ?? r0 = optionalBooleanAttribute;
        if (parseOptionalBooleanAttribute(str, REGEX_FORCED, false)) {
            r0 = (optionalBooleanAttribute ? 1 : 0) | 2;
        }
        return parseOptionalBooleanAttribute(str, REGEX_AUTOSELECT, false) ? r0 | 4 : r0;
    }

    private static int parseRoleFlags(String str, Map map) {
        String optionalStringAttr = parseOptionalStringAttr(str, REGEX_CHARACTERISTICS, map);
        if (TextUtils.isEmpty(optionalStringAttr)) {
            return 0;
        }
        String[] strArrSplit = Util.split(optionalStringAttr, ",");
        int i = Util.contains(strArrSplit, "public.accessibility.describes-video") ? 512 : 0;
        if (Util.contains(strArrSplit, "public.accessibility.transcribes-spoken-dialog")) {
            i |= 4096;
        }
        if (Util.contains(strArrSplit, "public.accessibility.describes-music-and-sound")) {
            i |= 1024;
        }
        return Util.contains(strArrSplit, "public.easy-to-read") ? i | 8192 : i;
    }

    private static DrmInitData.SchemeData parseDrmSchemeData(String str, String str2, Map map) throws ParserException {
        String optionalStringAttr = parseOptionalStringAttr(str, REGEX_KEYFORMATVERSIONS, "1", map);
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(str2)) {
            String stringAttr = parseStringAttr(str, REGEX_URI, map);
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "video/mp4", Base64.decode(stringAttr.substring(stringAttr.indexOf(44)), 0));
        }
        if ("com.widevine".equals(str2)) {
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "hls", Util.getUtf8Bytes(str));
        }
        if (!"com.microsoft.playready".equals(str2) || !"1".equals(optionalStringAttr)) {
            return null;
        }
        String stringAttr2 = parseStringAttr(str, REGEX_URI, map);
        byte[] bArrDecode = Base64.decode(stringAttr2.substring(stringAttr2.indexOf(44)), 0);
        UUID uuid = C.PLAYREADY_UUID;
        return new DrmInitData.SchemeData(uuid, "video/mp4", PsshAtomUtil.buildPsshAtom(uuid, bArrDecode));
    }

    private static HlsMediaPlaylist.ServerControl parseServerControl(String str) {
        double optionalDoubleAttr = parseOptionalDoubleAttr(str, REGEX_CAN_SKIP_UNTIL, -9.223372036854776E18d);
        long j = optionalDoubleAttr == -9.223372036854776E18d ? -9223372036854775807L : (long) (optionalDoubleAttr * 1000000.0d);
        boolean optionalBooleanAttribute = parseOptionalBooleanAttribute(str, REGEX_CAN_SKIP_DATE_RANGES, false);
        double optionalDoubleAttr2 = parseOptionalDoubleAttr(str, REGEX_HOLD_BACK, -9.223372036854776E18d);
        long j2 = optionalDoubleAttr2 == -9.223372036854776E18d ? -9223372036854775807L : (long) (optionalDoubleAttr2 * 1000000.0d);
        double optionalDoubleAttr3 = parseOptionalDoubleAttr(str, REGEX_PART_HOLD_BACK, -9.223372036854776E18d);
        return new HlsMediaPlaylist.ServerControl(j, optionalBooleanAttribute, j2, optionalDoubleAttr3 != -9.223372036854776E18d ? (long) (optionalDoubleAttr3 * 1000000.0d) : -9223372036854775807L, parseOptionalBooleanAttribute(str, REGEX_CAN_BLOCK_RELOAD, false));
    }

    private static String parseEncryptionScheme(String str) {
        if ("SAMPLE-AES-CENC".equals(str) || "SAMPLE-AES-CTR".equals(str)) {
            return "cenc";
        }
        return "cbcs";
    }

    private static int parseIntAttr(String str, Pattern pattern) {
        return Integer.parseInt(parseStringAttr(str, pattern, Collections.EMPTY_MAP));
    }

    private static int parseOptionalIntAttr(String str, Pattern pattern, int i) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt((String) Assertions.checkNotNull(matcher.group(1))) : i;
    }

    private static long parseLongAttr(String str, Pattern pattern) {
        return Long.parseLong(parseStringAttr(str, pattern, Collections.EMPTY_MAP));
    }

    private static long parseOptionalLongAttr(String str, Pattern pattern, long j) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Long.parseLong((String) Assertions.checkNotNull(matcher.group(1))) : j;
    }

    private static long parseTimeSecondsToUs(String str, Pattern pattern) {
        return new BigDecimal(parseStringAttr(str, pattern, Collections.EMPTY_MAP)).multiply(new BigDecimal(1000000L)).longValue();
    }

    private static double parseDoubleAttr(String str, Pattern pattern) {
        return Double.parseDouble(parseStringAttr(str, pattern, Collections.EMPTY_MAP));
    }

    private static String parseStringAttr(String str, Pattern pattern, Map map) throws ParserException {
        String optionalStringAttr = parseOptionalStringAttr(str, pattern, map);
        if (optionalStringAttr != null) {
            return optionalStringAttr;
        }
        throw ParserException.createForMalformedManifest("Couldn't match " + pattern.pattern() + " in " + str, null);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, Map map) {
        return parseOptionalStringAttr(str, pattern, null, map);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, String str2, Map map) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str2 = (String) Assertions.checkNotNull(matcher.group(1));
        }
        return (map.isEmpty() || str2 == null) ? str2 : replaceVariableReferences(str2, map);
    }

    private static double parseOptionalDoubleAttr(String str, Pattern pattern, double d) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Double.parseDouble((String) Assertions.checkNotNull(matcher.group(1))) : d;
    }

    private static String replaceVariableReferences(String str, Map map) {
        Matcher matcher = REGEX_VARIABLE_REFERENCE.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String strGroup = matcher.group(1);
            if (map.containsKey(strGroup)) {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement((String) map.get(strGroup)));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private static boolean parseOptionalBooleanAttribute(String str, Pattern pattern, boolean z) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? "YES".equals(matcher.group(1)) : z;
    }

    private static Pattern compileBooleanAttrPattern(String str) {
        return Pattern.compile(str + "=(NO|YES)");
    }

    private static class LineIterator {
        private final Queue extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue queue, BufferedReader bufferedReader) {
            this.extraLines = queue;
            this.reader = bufferedReader;
        }

        public boolean hasNext() throws IOException {
            String strTrim;
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = (String) Assertions.checkNotNull((String) this.extraLines.poll());
                return true;
            }
            do {
                String line = this.reader.readLine();
                this.next = line;
                if (line == null) {
                    return false;
                }
                strTrim = line.trim();
                this.next = strTrim;
            } while (strTrim.isEmpty());
            return true;
        }

        public String next() {
            if (hasNext()) {
                String str = this.next;
                this.next = null;
                return str;
            }
            throw new NoSuchElementException();
        }
    }
}
