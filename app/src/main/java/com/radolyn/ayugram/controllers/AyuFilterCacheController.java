package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import android.util.LongSparseArray;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import com.radolyn.ayugram.utils.ThreadSafeLongSparseArray;
import com.radolyn.ayugram.utils.filters.HashablePattern;
import com.radolyn.ayugram.utils.filters.ReversiblePattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;

public class AyuFilterCacheController extends BaseController {
    private static final AyuFilterCacheController[] Instance = new AyuFilterCacheController[16];
    private LongSparseArray exclusionsByDialogId;
    private ThreadSafeLongSparseArray filteredGroups;
    private ThreadSafeLongSparseArray filteredMessages;
    private LongSparseArray patternsByDialogId;
    private ArrayList sharedPatterns;

    public AyuFilterCacheController(int i) {
        super(i);
        this.filteredMessages = new ThreadSafeLongSparseArray();
        this.filteredGroups = new ThreadSafeLongSparseArray();
    }

    public static AyuFilterCacheController getInstance(int i) {
        AyuFilterCacheController ayuFilterCacheController;
        AyuFilterCacheController[] ayuFilterCacheControllerArr = Instance;
        AyuFilterCacheController ayuFilterCacheController2 = ayuFilterCacheControllerArr[i];
        if (ayuFilterCacheController2 != null) {
            return ayuFilterCacheController2;
        }
        synchronized (AyuFilterCacheController.class) {
            try {
                ayuFilterCacheController = ayuFilterCacheControllerArr[i];
                if (ayuFilterCacheController == null) {
                    ayuFilterCacheController = new AyuFilterCacheController(i);
                    ayuFilterCacheControllerArr[i] = ayuFilterCacheController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuFilterCacheController;
    }

    public static void rebuildCache() {
        synchronized (AyuFilterCacheController.class) {
            try {
                List<RegexFilter> all = AyuData.getRegexFilterDao().getAll();
                List<RegexFilterGlobalExclusion> allExclusions = AyuData.getRegexFilterDao().getAllExclusions();
                ArrayList arrayList = new ArrayList();
                LongSparseArray longSparseArray = new LongSparseArray();
                for (RegexFilter regexFilter : all) {
                    if (regexFilter.enabled && !TextUtils.isEmpty(regexFilter.text)) {
                        Pattern patternCompile = Pattern.compile(regexFilter.text, regexFilter.caseInsensitive ? 10 : 8);
                        Long l = regexFilter.dialogId;
                        if (l != null) {
                            ArrayList arrayList2 = (ArrayList) longSparseArray.get(l.longValue());
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                                longSparseArray.put(regexFilter.dialogId.longValue(), arrayList2);
                            }
                            arrayList2.add(new ReversiblePattern(patternCompile, regexFilter.reversed));
                        } else {
                            arrayList.add(new HashablePattern(regexFilter.id, new ReversiblePattern(patternCompile, regexFilter.reversed)));
                        }
                    }
                }
                LongSparseArray longSparseArrayBuildExclusions = buildExclusions(allExclusions, arrayList);
                for (AyuFilterCacheController ayuFilterCacheController : Instance) {
                    if (ayuFilterCacheController != null) {
                        ayuFilterCacheController.sharedPatterns = arrayList;
                        ayuFilterCacheController.patternsByDialogId = longSparseArray;
                        ayuFilterCacheController.exclusionsByDialogId = longSparseArrayBuildExclusions;
                        ayuFilterCacheController.filteredMessages = new ThreadSafeLongSparseArray();
                        ayuFilterCacheController.filteredGroups = new ThreadSafeLongSparseArray();
                    }
                }
                NotificationCenter.getInstance(UserConfig.selectedAccount).postNotificationNameOnUIThread(AyuConstants.FILTERS_UPDATED, new Object[0]);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static LongSparseArray buildExclusions(List list, ArrayList arrayList) {
        HashablePattern hashablePattern;
        LongSparseArray longSparseArray = new LongSparseArray();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            RegexFilterGlobalExclusion regexFilterGlobalExclusion = (RegexFilterGlobalExclusion) it.next();
            HashSet hashSet = (HashSet) longSparseArray.get(regexFilterGlobalExclusion.dialogId);
            if (hashSet == null) {
                hashSet = new HashSet();
                longSparseArray.put(regexFilterGlobalExclusion.dialogId, hashSet);
            }
            int size = arrayList.size();
            int i = 0;
            do {
                if (i >= size) {
                    hashablePattern = null;
                    break;
                }
                Object obj = arrayList.get(i);
                i++;
                hashablePattern = (HashablePattern) obj;
            } while (!hashablePattern.getId().equals(regexFilterGlobalExclusion.filterId));
            if (hashablePattern != null) {
                hashSet.add(hashablePattern);
            }
        }
        return longSparseArray;
    }

    public ArrayList getSharedPatterns() {
        if (this.sharedPatterns == null) {
            rebuildCache();
        }
        return this.sharedPatterns;
    }

    public ArrayList getPatternsByDialogId(long j) {
        if (this.patternsByDialogId == null) {
            rebuildCache();
        }
        return (ArrayList) this.patternsByDialogId.get(j);
    }

    public HashSet getExclusionsByDialogId(long j) {
        if (this.exclusionsByDialogId == null) {
            rebuildCache();
        }
        return (HashSet) this.exclusionsByDialogId.get(j);
    }

    public Boolean isFiltered(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        Boolean boolIsFilteredSingle = isFilteredSingle(messageObject);
        return boolIsFilteredSingle != null ? boolIsFilteredSingle : isFilteredGroup(messageObject, groupedMessages);
    }

    private Boolean isFilteredSingle(MessageObject messageObject) {
        HashMap map = (HashMap) this.filteredMessages.get(messageObject.getDialogId());
        if (map == null || map.isEmpty()) {
            return null;
        }
        return (Boolean) map.get(Integer.valueOf(messageObject.getId()));
    }

    private Boolean isFilteredGroup(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        HashMap map;
        if (groupedMessages == null || (map = (HashMap) this.filteredGroups.get(messageObject.getDialogId())) == null || map.isEmpty()) {
            return null;
        }
        return (Boolean) map.get(Long.valueOf(groupedMessages.groupId));
    }

    public void putFiltered(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, boolean z) {
        HashMap map = (HashMap) this.filteredMessages.get(messageObject.getDialogId());
        if (map == null) {
            map = new HashMap();
            this.filteredMessages.put(messageObject.getDialogId(), map);
        }
        map.put(Integer.valueOf(messageObject.getId()), Boolean.valueOf(z));
        long groupId = groupedMessages == null ? messageObject.getGroupId() : groupedMessages.groupId;
        if (groupId == 0) {
            return;
        }
        HashMap map2 = (HashMap) this.filteredGroups.get(messageObject.getDialogId());
        if (map2 == null) {
            map2 = new HashMap();
            this.filteredGroups.put(messageObject.getDialogId(), map2);
        }
        map2.put(Long.valueOf(groupId), Boolean.valueOf(z));
    }

    public void invalidate(MessageObject messageObject) {
        HashMap map = (HashMap) this.filteredMessages.get(messageObject.getDialogId());
        if (map != null) {
            map.remove(Integer.valueOf(messageObject.getId()));
        }
        HashMap map2 = (HashMap) this.filteredGroups.get(messageObject.getDialogId());
        if (map2 != null) {
            map2.remove(Long.valueOf(messageObject.getGroupId()));
        }
    }
}
