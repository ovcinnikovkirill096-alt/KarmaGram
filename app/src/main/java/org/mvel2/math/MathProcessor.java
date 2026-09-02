package org.mvel2.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import org.mvel2.DataConversion;
import org.mvel2.Unit;
import org.mvel2.compiler.BlankLiteral;
import org.mvel2.debug.DebugTools;
import org.mvel2.util.ParseTools;
import org.mvel2.util.Soundex;

public class MathProcessor {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    private static int box(int i) {
        if (i == 7) {
            return 15;
        }
        if (i == 8) {
            return 112;
        }
        if (i == 9) {
            return 113;
        }
        switch (i) {
            case 100:
                return 105;
            case 101:
                return 106;
            case 102:
                return 107;
            case 103:
                return 109;
            case 104:
                return 108;
            default:
                return i;
        }
    }

    private static boolean isIntegerType(int i) {
        return i == 101 || i == 106 || i == 102 || i == 107;
    }

    public static Object doOperations(Object obj, int i, Object obj2) {
        return doOperations(obj == null ? 0 : ParseTools.__resolveType(obj.getClass()), obj, i, obj2 == null ? -1 : ParseTools.__resolveType(obj2.getClass()), obj2);
    }

    public static Object doOperations(Object obj, int i, int i2, Object obj2) {
        return doOperations(obj == null ? 0 : ParseTools.__resolveType(obj.getClass()), obj, i, i2, obj2);
    }

    public static Object doOperations(int i, Object obj, int i2, int i3, Object obj2) {
        if (i < 1) {
            i = obj == null ? 0 : ParseTools.__resolveType(obj.getClass());
        }
        if (i3 < 1) {
            i3 = obj2 == null ? 0 : ParseTools.__resolveType(obj2.getClass());
        }
        if (i != 110) {
            return _doOperations(i, obj, i2, i3, obj2);
        }
        if (i3 == 110) {
            return doBigDecimalArithmetic((BigDecimal) obj, i2, (BigDecimal) obj2, false, -1);
        }
        if (i3 > 99) {
            return doBigDecimalArithmetic((BigDecimal) obj, i2, asBigDecimal(obj2), false, -1);
        }
        return _doOperations(i, obj, i2, i3, obj2);
    }

    private static Object doPrimWrapperArithmetic(Number number, int i, Number number2, int i2) {
        if (i == 0) {
            return toType(Double.valueOf(number.doubleValue() + number2.doubleValue()), i2);
        }
        if (i == 1) {
            return toType(Double.valueOf(number.doubleValue() - number2.doubleValue()), i2);
        }
        if (i == 2) {
            return toType(Double.valueOf(number.doubleValue() * number2.doubleValue()), i2);
        }
        if (i == 3) {
            return toType(Double.valueOf(number.doubleValue() / number2.doubleValue()), i2);
        }
        if (i == 4) {
            return toType(Double.valueOf(number.doubleValue() % number2.doubleValue()), i2);
        }
        if (i == 5) {
            return toType(Double.valueOf(Math.pow(number.doubleValue(), number2.doubleValue())), i2);
        }
        switch (i) {
            case 14:
                return Boolean.valueOf(number.doubleValue() < number2.doubleValue());
            case 15:
                return Boolean.valueOf(number.doubleValue() > number2.doubleValue());
            case 16:
                return Boolean.valueOf(number.doubleValue() <= number2.doubleValue());
            case 17:
                return Boolean.valueOf(number.doubleValue() >= number2.doubleValue());
            case 18:
                return Boolean.valueOf(number.doubleValue() == number2.doubleValue());
            case 19:
                return Boolean.valueOf(number.doubleValue() != number2.doubleValue());
            default:
                return null;
        }
    }

    private static Object toType(Number number, int i) {
        if (i != 1) {
            switch (i) {
                case 100:
                case 105:
                    return Short.valueOf(number.shortValue());
                case 101:
                case 106:
                    return Integer.valueOf(number.intValue());
                case 102:
                case 107:
                    return Long.valueOf(number.longValue());
                case 103:
                case 109:
                    return Double.valueOf(number.doubleValue());
                case 104:
                case 108:
                    return Float.valueOf(number.floatValue());
                case 110:
                    return new BigDecimal(number.doubleValue());
                case 111:
                    return BigInteger.valueOf(number.longValue());
                default:
                    throw new RuntimeException("internal error: " + i);
            }
        }
        return Double.valueOf(number.doubleValue());
    }

    private static Object doBigDecimalArithmetic(BigDecimal bigDecimal, int i, BigDecimal bigDecimal2, boolean z, int i2) {
        if (i == 0) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.add(bigDecimal2, MATH_CONTEXT), i2);
            }
            return bigDecimal.add(bigDecimal2, MATH_CONTEXT);
        }
        boolean z2 = true;
        if (i == 1) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.subtract(bigDecimal2, MATH_CONTEXT), i2);
            }
            return bigDecimal.subtract(bigDecimal2, MATH_CONTEXT);
        }
        if (i == 2) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.multiply(bigDecimal2, MATH_CONTEXT), i2);
            }
            return bigDecimal.multiply(bigDecimal2, MATH_CONTEXT);
        }
        if (i == 3) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.divide(bigDecimal2, MATH_CONTEXT), i2);
            }
            return bigDecimal.divide(bigDecimal2, MATH_CONTEXT);
        }
        if (i == 4) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.remainder(bigDecimal2), i2);
            }
            return bigDecimal.remainder(bigDecimal2);
        }
        if (i == 5) {
            if (z) {
                return ParseTools.narrowType(bigDecimal.pow(bigDecimal2.intValue(), MATH_CONTEXT), i2);
            }
            return bigDecimal.pow(bigDecimal2.intValue(), MATH_CONTEXT);
        }
        switch (i) {
            case 14:
                return Boolean.valueOf((bigDecimal == null || bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) >= 0) ? false : true);
            case 15:
                return Boolean.valueOf((bigDecimal == null || bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) <= 0) ? false : true);
            case 16:
                return Boolean.valueOf((bigDecimal == null || bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) > 0) ? false : true);
            case 17:
                return Boolean.valueOf((bigDecimal == null || bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) < 0) ? false : true);
            case 18:
                if (bigDecimal != null ? bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) != 0 : bigDecimal2 != null) {
                    z2 = false;
                }
                return Boolean.valueOf(z2);
            case 19:
                if (bigDecimal != null ? !(bigDecimal2 == null || bigDecimal.compareTo(bigDecimal2) != 0) : bigDecimal2 == null) {
                    z2 = false;
                }
                return Boolean.valueOf(z2);
            default:
                return null;
        }
    }

    private static Object _doOperations(int i, Object obj, int i2, int i3, Object obj2) {
        if (i2 < 20) {
            if (((i > 49 || i2 == 18 || i2 == 19) && i == i3) || (isIntegerType(i) && isIntegerType(i3) && i2 >= 6 && i2 <= 13)) {
                return doOperationsSameType(i, obj, i2, obj2);
            }
            if (obj2 != null && isNumericOperation(i, obj, i2, i3, obj2)) {
                return doPrimWrapperArithmetic(getNumber(obj, i), i2, getNumber(obj2, i3), Math.max(box(i3), box(i)));
            }
            if (i2 != 0 && ((i == 15 || i3 == 15) && i != i3 && i != 200 && i3 != 200)) {
                return doOperationNonNumeric(i, DataConversion.convert(obj, Boolean.class), i2, DataConversion.convert(obj2, Boolean.class));
            }
            if ((i == 1 || i3 == 1) && (i == 8 || i == 112 || i3 == 8 || i3 == 112)) {
                if (i == 1) {
                    return doOperationNonNumeric(i, obj, i2, String.valueOf(obj2));
                }
                return doOperationNonNumeric(i, String.valueOf(obj), i2, obj2);
            }
        }
        return doOperationNonNumeric(i, obj, i2, obj2);
    }

    private static boolean isNumericOperation(int i, Object obj, int i2, int i3, Object obj2) {
        if (i > 99 && i3 > 99) {
            return true;
        }
        if (i2 != 0) {
            return (i > 99 || i3 > 99 || i2 < 14 || i2 > 17) && ParseTools.isNumber(obj) && ParseTools.isNumber(obj2);
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:38:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:53:0x00f1  */
    /* JADX WARN: Code duplicated, block: B:68:0x012a  */
    /* JADX WARN: Code duplicated, block: B:83:0x0164  */
    private static Object doOperationNonNumeric(int i, Object obj, int i2, Object obj2) {
        if (i2 == 0) {
            if (i == 50) {
                ArrayList arrayList = new ArrayList((Collection) obj);
                arrayList.add(obj2);
                return arrayList;
            }
            return String.valueOf(obj) + String.valueOf(obj2);
        }
        boolean z = true;
        if (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4) {
            return null;
        }
        if (i2 != 27) {
            switch (i2) {
                case 14:
                    if (!(obj instanceof Comparable)) {
                        return Boolean.FALSE;
                    }
                    if (obj2 != null) {
                        try {
                            if (((Comparable) obj).compareTo(obj2) > -1) {
                                z = false;
                            }
                        } catch (ClassCastException e) {
                            throw new RuntimeException("uncomparable values <<" + obj + ">> and <<" + obj2 + ">>", e);
                        }
                    } else {
                        z = false;
                    }
                    return Boolean.valueOf(z);
                case 15:
                    if (!(obj instanceof Comparable)) {
                        return Boolean.FALSE;
                    }
                    if (obj2 != null) {
                        try {
                            if (((Comparable) obj).compareTo(obj2) < 1) {
                                z = false;
                            }
                        } catch (ClassCastException e2) {
                            throw new RuntimeException("uncomparable values <<" + obj + ">> and <<" + obj2 + ">>", e2);
                        }
                    } else {
                        z = false;
                    }
                    return Boolean.valueOf(z);
                case 16:
                    if (!(obj instanceof Comparable)) {
                        return Boolean.FALSE;
                    }
                    if (obj2 != null) {
                        try {
                            if (((Comparable) obj).compareTo(obj2) > 0) {
                                z = false;
                            }
                        } catch (ClassCastException e3) {
                            throw new RuntimeException("uncomparable values <<" + obj + ">> and <<" + obj2 + ">>", e3);
                        }
                    } else {
                        z = false;
                    }
                    return Boolean.valueOf(z);
                case 17:
                    if (!(obj instanceof Comparable)) {
                        return Boolean.FALSE;
                    }
                    if (obj2 != null) {
                        try {
                            if (((Comparable) obj).compareTo(obj2) < 0) {
                                z = false;
                            }
                        } catch (ClassCastException e4) {
                            throw new RuntimeException("uncomparable values <<" + obj + ">> and <<" + obj2 + ">>", e4);
                        }
                    } else {
                        z = false;
                    }
                    return Boolean.valueOf(z);
                case 18:
                    return safeEquals(obj2, obj);
                case 19:
                    return safeNotEquals(obj2, obj);
                case 20:
                    return String.valueOf(obj) + String.valueOf(obj2);
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("could not perform numeric operation on non-numeric types: left-type=");
                    sb.append(obj != null ? obj.getClass().getName() : "null");
                    sb.append("; right-type=");
                    sb.append(obj2 != null ? obj2.getClass().getName() : "null");
                    sb.append(" [vals (");
                    sb.append(String.valueOf(obj));
                    sb.append(", ");
                    sb.append(String.valueOf(obj2));
                    sb.append(") operation=");
                    sb.append(DebugTools.getOperatorName(i2));
                    sb.append(" (opcode:");
                    sb.append(i2);
                    sb.append(") ]");
                    throw new RuntimeException(sb.toString());
            }
        }
        return Boolean.valueOf(Soundex.soundex(String.valueOf(obj)).equals(Soundex.soundex(String.valueOf(obj2))));
    }

    private static Boolean safeEquals(Object obj, Object obj2) {
        if (obj != null) {
            return Boolean.valueOf(obj.equals(obj2));
        }
        return Boolean.valueOf(obj2 == null);
    }

    private static Boolean safeNotEquals(Object obj, Object obj2) {
        if (obj != null) {
            return Boolean.valueOf(!obj.equals(obj2));
        }
        return Boolean.valueOf(obj2 != null);
    }

    /* JADX WARN: Code duplicated, block: B:117:0x0209  */
    /* JADX WARN: Code duplicated, block: B:126:0x0223  */
    /* JADX WARN: Code duplicated, block: B:135:0x023d  */
    /* JADX WARN: Code duplicated, block: B:140:0x024e  */
    /* JADX WARN: Code duplicated, block: B:143:0x0254  */
    /* JADX WARN: Code duplicated, block: B:148:0x0265  */
    /* JADX WARN: Code duplicated, block: B:151:0x026b  */
    /* JADX WARN: Code duplicated, block: B:156:0x027c  */
    /* JADX WARN: Code duplicated, block: B:159:0x0282  */
    /* JADX WARN: Code duplicated, block: B:164:0x0293  */
    /* JADX WARN: Code duplicated, block: B:167:0x0299  */
    /* JADX WARN: Code duplicated, block: B:169:0x02a1  */
    /* JADX WARN: Code duplicated, block: B:171:0x02a5  */
    /* JADX WARN: Code duplicated, block: B:173:0x02b7  */
    /* JADX WARN: Code duplicated, block: B:175:0x02ca  */
    /* JADX WARN: Code duplicated, block: B:177:0x02ce  */
    /* JADX WARN: Code duplicated, block: B:179:0x02e0  */
    /* JADX WARN: Code duplicated, block: B:181:0x02f3  */
    /* JADX WARN: Code duplicated, block: B:183:0x02f7  */
    /* JADX WARN: Code duplicated, block: B:185:0x0309  */
    /* JADX WARN: Code duplicated, block: B:187:0x031c  */
    /* JADX WARN: Code duplicated, block: B:189:0x0320  */
    /* JADX WARN: Code duplicated, block: B:191:0x0333  */
    /* JADX WARN: Code duplicated, block: B:193:0x0345  */
    /* JADX WARN: Code duplicated, block: B:195:0x0349  */
    /* JADX WARN: Code duplicated, block: B:197:0x035c  */
    /* JADX WARN: Code duplicated, block: B:199:0x036e  */
    /* JADX WARN: Code duplicated, block: B:201:0x0372  */
    /* JADX WARN: Code duplicated, block: B:203:0x0385  */
    /* JADX WARN: Code duplicated, block: B:205:0x0397  */
    /* JADX WARN: Code duplicated, block: B:207:0x03ab  */
    /* JADX WARN: Code duplicated, block: B:209:0x03b0  */
    /* JADX WARN: Code duplicated, block: B:211:0x03b6  */
    /* JADX WARN: Code duplicated, block: B:213:0x03c4  */
    /* JADX WARN: Code duplicated, block: B:215:0x03d2  */
    /* JADX WARN: Code duplicated, block: B:217:0x03e0  */
    /* JADX WARN: Code duplicated, block: B:219:0x03ee  */
    /* JADX WARN: Code duplicated, block: B:299:0x0555  */
    /* JADX WARN: Code duplicated, block: B:301:0x0566 A[PHI: r7 r9
  0x0566: PHI (r7v22 java.lang.Object) = (r7v35 java.lang.Object), (r7v0 java.lang.Object) binds: [B:300:0x0563, B:8:0x0014] A[DONT_GENERATE, DONT_INLINE]
  0x0566: PHI (r9v17 java.lang.Object) = (r9v19 java.lang.Object), (r9v0 java.lang.Object) binds: [B:300:0x0563, B:8:0x0014] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:303:0x056b  */
    /* JADX WARN: Code duplicated, block: B:304:0x056d A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:309:0x057e A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:312:0x0585  */
    /* JADX WARN: Code duplicated, block: B:313:0x0587 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:318:0x0598 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:321:0x059f  */
    /* JADX WARN: Code duplicated, block: B:322:0x05a1 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:326:0x05b0  */
    /* JADX WARN: Code duplicated, block: B:329:0x05b6  */
    /* JADX WARN: Code duplicated, block: B:330:0x05b8 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:334:0x05c7  */
    /* JADX WARN: Code duplicated, block: B:337:0x05cd  */
    /* JADX WARN: Code duplicated, block: B:338:0x05cf A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:342:0x05de  */
    /* JADX WARN: Code duplicated, block: B:345:0x05e4  */
    /* JADX WARN: Code duplicated, block: B:346:0x05e6 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:350:0x05f5  */
    /* JADX WARN: Code duplicated, block: B:353:0x05fb  */
    /* JADX WARN: Code duplicated, block: B:355:0x0601  */
    /* JADX WARN: Code duplicated, block: B:357:0x0617  */
    /* JADX WARN: Code duplicated, block: B:359:0x0625  */
    /* JADX WARN: Code duplicated, block: B:361:0x0633  */
    /* JADX WARN: Code duplicated, block: B:363:0x0641  */
    /* JADX WARN: Code duplicated, block: B:365:0x064f  */
    /* JADX WARN: Code duplicated, block: B:367:0x065d  */
    /* JADX WARN: Code duplicated, block: B:368:0x065f A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:373:0x0670 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:376:0x0677  */
    /* JADX WARN: Code duplicated, block: B:377:0x0679 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:382:0x068a A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:385:0x0691  */
    /* JADX WARN: Code duplicated, block: B:386:0x0693 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:390:0x06a2  */
    /* JADX WARN: Code duplicated, block: B:393:0x06a8  */
    /* JADX WARN: Code duplicated, block: B:394:0x06aa A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:398:0x06b9  */
    /* JADX WARN: Code duplicated, block: B:401:0x06bf  */
    /* JADX WARN: Code duplicated, block: B:402:0x06c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:406:0x06d0  */
    /* JADX WARN: Code duplicated, block: B:409:0x06d6  */
    /* JADX WARN: Code duplicated, block: B:410:0x06d8 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:414:0x06e7  */
    /* JADX WARN: Code duplicated, block: B:417:0x06ed  */
    /* JADX WARN: Code duplicated, block: B:419:0x06f3  */
    /* JADX WARN: Code duplicated, block: B:421:0x0704  */
    /* JADX WARN: Code duplicated, block: B:423:0x0712  */
    /* JADX WARN: Code duplicated, block: B:425:0x0720  */
    /* JADX WARN: Code duplicated, block: B:427:0x072e  */
    /* JADX WARN: Code duplicated, block: B:429:0x073c  */
    /* JADX WARN: Code duplicated, block: B:431:0x074a A[PHI: r7 r9
  0x074a: PHI (r7v3 java.lang.Object) = (r7v0 java.lang.Object), (r7v22 java.lang.Object) binds: [B:5:0x000c, B:301:0x0566] A[DONT_GENERATE, DONT_INLINE]
  0x074a: PHI (r9v2 java.lang.Object) = (r9v0 java.lang.Object), (r9v17 java.lang.Object) binds: [B:5:0x000c, B:301:0x0566] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:443:0x0777  */
    /* JADX WARN: Code duplicated, block: B:444:0x0779 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:449:0x0788 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:452:0x078f  */
    /* JADX WARN: Code duplicated, block: B:453:0x0791 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:458:0x07a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:461:0x07a7  */
    /* JADX WARN: Code duplicated, block: B:464:0x07b2  */
    /* JADX WARN: Code duplicated, block: B:467:0x07b8  */
    /* JADX WARN: Code duplicated, block: B:470:0x07c3  */
    /* JADX WARN: Code duplicated, block: B:473:0x07c9  */
    /* JADX WARN: Code duplicated, block: B:476:0x07d4  */
    /* JADX WARN: Code duplicated, block: B:479:0x07da  */
    /* JADX WARN: Code duplicated, block: B:482:0x07e5  */
    /* JADX WARN: Code duplicated, block: B:485:0x07eb  */
    /* JADX WARN: Code duplicated, block: B:487:0x07f3  */
    /* JADX WARN: Code duplicated, block: B:489:0x0800  */
    /* JADX WARN: Code duplicated, block: B:491:0x0809  */
    /* JADX WARN: Code duplicated, block: B:493:0x0812  */
    /* JADX WARN: Code duplicated, block: B:495:0x081b  */
    /* JADX WARN: Code duplicated, block: B:497:0x0824  */
    /* JADX WARN: Switch 'out' block B:299:0x0555 for B:114:0x0201 already processed. Defaulting to fallback option. */
    /* JADX WARN: Switch 'out' block B:431:0x074a for B:300:0x0563 already processed. Defaulting to fallback option. */
    private static Object doOperationsSameType(int i, Object obj, int i2, Object obj2) {
        double dPow;
        boolean z = true;
        if (i == 50) {
            if (i2 == 0) {
                ArrayList arrayList = new ArrayList((Collection) obj);
                arrayList.addAll((Collection) obj2);
                return arrayList;
            }
            if (i2 == 18) {
                return Boolean.valueOf(obj.equals(obj2));
            }
            if (i2 == 19) {
                return Boolean.valueOf(!obj.equals(obj2));
            }
            throw new UnsupportedOperationException("illegal operation on Collection type");
        }
        if (i == 111) {
            switch (i2) {
                case 0:
                    return ((BigInteger) obj).add((BigInteger) obj2);
                case 1:
                    return ((BigInteger) obj).subtract((BigInteger) obj2);
                case 2:
                    return ((BigInteger) obj).multiply((BigInteger) obj2);
                case 3:
                    return ((BigInteger) obj).divide((BigInteger) obj2);
                case 4:
                    return ((BigInteger) obj).remainder((BigInteger) obj2);
                case 5:
                    return ((BigInteger) obj).pow(((BigInteger) obj2).intValue());
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    throw new RuntimeException("bitwise operation on a number greater than 32-bits not possible");
                case 14:
                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) < 0);
                case 15:
                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) > 0);
                case 16:
                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) <= 0);
                case 17:
                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) >= 0);
                case 18:
                    if (obj != null || obj2 == null ? obj != obj2 : ((BigInteger) obj).compareTo((BigInteger) obj2) != 0) {
                    }
                    return Boolean.valueOf(z);
                case 19:
                    if (obj != null || obj2 == null ? obj == obj2 : ((BigInteger) obj).compareTo((BigInteger) obj2) == 0) {
                    }
                    return Boolean.valueOf(z);
            }
        }
        if (i != 300) {
            switch (i) {
                case 101:
                case 106:
                    switch (i2) {
                        case 0:
                            return Integer.valueOf(toInteger(obj) + toInteger(obj2));
                        case 1:
                            return Integer.valueOf(toInteger(obj) - toInteger(obj2));
                        case 2:
                            return Integer.valueOf(toInteger(obj) * toInteger(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / ((double) toInteger(obj2)));
                        case 4:
                            return Integer.valueOf(toInteger(obj) % toInteger(obj2));
                        case 5:
                            double dPow2 = Math.pow(toInteger(obj), toInteger(obj2));
                            if (dPow2 > 2.147483647E9d) {
                                return Double.valueOf(dPow2);
                            }
                            return Integer.valueOf((int) dPow2);
                        case 6:
                            return obj2 instanceof Long ? Long.valueOf(((long) ((Integer) obj).intValue()) & ((Long) obj2).longValue()) : Integer.valueOf(((Integer) obj).intValue() & ((Integer) obj2).intValue());
                        case 7:
                            return obj2 instanceof Long ? Long.valueOf(((long) ((Integer) obj).intValue()) | ((Long) obj2).longValue()) : Integer.valueOf(((Integer) obj).intValue() | ((Integer) obj2).intValue());
                        case 8:
                            return obj2 instanceof Long ? Long.valueOf(((long) ((Integer) obj).intValue()) ^ ((Long) obj2).longValue()) : Integer.valueOf(((Integer) obj).intValue() ^ ((Integer) obj2).intValue());
                        case 9:
                            return obj2 instanceof Long ? Integer.valueOf(((Integer) obj).intValue() >> ((int) ((Long) obj2).longValue())) : Integer.valueOf(((Integer) obj).intValue() >> ((Integer) obj2).intValue());
                        case 10:
                            return obj2 instanceof Long ? Integer.valueOf(((Integer) obj).intValue() << ((int) ((Long) obj2).longValue())) : Integer.valueOf(((Integer) obj).intValue() << ((Integer) obj2).intValue());
                        case 11:
                            return obj2 instanceof Long ? Integer.valueOf(((Integer) obj).intValue() >>> ((int) ((Long) obj2).longValue())) : Integer.valueOf(((Integer) obj).intValue() >>> ((Integer) obj2).intValue());
                        case 14:
                            return Boolean.valueOf((obj == null || obj2 == null || toInteger(obj) >= toInteger(obj2)) ? false : true);
                        case 15:
                            return Boolean.valueOf((obj == null || obj2 == null || toInteger(obj) <= toInteger(obj2)) ? false : true);
                        case 16:
                            return Boolean.valueOf((obj == null || obj2 == null || toInteger(obj) > toInteger(obj2)) ? false : true);
                        case 17:
                            return Boolean.valueOf((obj == null || obj2 == null || toInteger(obj) < toInteger(obj2)) ? false : true);
                        case 18:
                            if (obj == null || obj2 == null ? obj != obj2 : toInteger(obj) != toInteger(obj2)) {
                                z = false;
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj == null || obj2 == null ? obj == obj2 : toInteger(obj) == toInteger(obj2)) {
                                z = false;
                            }
                            return Boolean.valueOf(z);
                    }
                    switch (i2) {
                        case 0:
                            return Long.valueOf(toLong(obj) + toLong(obj2));
                        case 1:
                            return Long.valueOf(toLong(obj) - toLong(obj2));
                        case 2:
                            return Long.valueOf(toLong(obj) * toLong(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Long.valueOf(toLong(obj) % toLong(obj2));
                        case 5:
                            dPow = Math.pow(toLong(obj), toLong(obj2));
                            if (dPow > 9.223372036854776E18d) {
                                return Double.valueOf(dPow);
                            }
                            return Long.valueOf((long) dPow);
                        case 6:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() & ((long) ((Integer) obj2).intValue())) : Long.valueOf(((Long) obj).longValue() & ((Long) obj2).longValue());
                        case 7:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() | ((long) ((Integer) obj2).intValue())) : Long.valueOf(((Long) obj).longValue() | ((Long) obj2).longValue());
                        case 8:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() ^ ((long) ((Integer) obj2).intValue())) : Long.valueOf(((Long) obj).longValue() ^ ((Long) obj2).longValue());
                        case 9:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() >> ((Integer) obj2).intValue()) : Long.valueOf(((Long) obj).longValue() >> ((int) ((Long) obj2).longValue()));
                        case 10:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() << ((Integer) obj2).intValue()) : Long.valueOf(((Long) obj).longValue() << ((int) ((Long) obj2).longValue()));
                        case 11:
                            return obj2 instanceof Integer ? Long.valueOf(((Long) obj).longValue() >>> ((Integer) obj2).intValue()) : Long.valueOf(((Long) obj).longValue() >>> ((int) ((Long) obj2).longValue()));
                        case 12:
                            throw new UnsupportedOperationException("unsigned left-shift not supported");
                        case 13:
                        default:
                            Unit unit = (Unit) obj;
                            obj2 = unit.convertFrom(obj2);
                            obj = Double.valueOf(unit.getValue());
                            break;
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) < toLong(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) > toLong(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) <= toLong(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) >= toLong(obj2));
                        case 18:
                            if (obj != null || obj2 == null ? obj != obj2 : toLong(obj) != toLong(obj2)) {
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj != null || obj2 == null ? obj == obj2 : toLong(obj) == toLong(obj2)) {
                            }
                            return Boolean.valueOf(z);
                    }
                    switch (i2) {
                        case 0:
                            return Double.valueOf(toDouble(obj) + toDouble(obj2));
                        case 1:
                            return Double.valueOf(toDouble(obj) - toDouble(obj2));
                        case 2:
                            return Double.valueOf(toDouble(obj) * toDouble(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Double.valueOf(toDouble(obj) % toDouble(obj2));
                        case 5:
                            return Double.valueOf(Math.pow(toDouble(obj), toDouble(obj2)));
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                            throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                        case 12:
                        case 13:
                        default:
                            switch (i2) {
                                case 0:
                                    return Float.valueOf(toFloat(obj) + toFloat(obj2));
                                case 1:
                                    return Float.valueOf(toFloat(obj) - toFloat(obj2));
                                case 2:
                                    return Float.valueOf(toFloat(obj) * toFloat(obj2));
                                case 3:
                                    return Double.valueOf(toDouble(obj) / toDouble(obj2));
                                case 4:
                                    return Float.valueOf(toFloat(obj) % toFloat(obj2));
                                case 5:
                                    return ParseTools.narrowType(asBigDecimal(obj).pow(((Number) obj2).intValue(), MATH_CONTEXT), -1);
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                    throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                                case 12:
                                case 13:
                                default:
                                    switch (i2) {
                                        case 0:
                                            return ((BigInteger) obj).add((BigInteger) obj2);
                                        case 1:
                                            return ((BigInteger) obj).subtract((BigInteger) obj2);
                                        case 2:
                                            return ((BigInteger) obj).multiply((BigInteger) obj2);
                                        case 3:
                                            return ((BigInteger) obj).divide((BigInteger) obj2);
                                        case 4:
                                            return ((BigInteger) obj).remainder((BigInteger) obj2);
                                        case 5:
                                            return ((BigInteger) obj).pow(((BigInteger) obj2).intValue());
                                        case 6:
                                        case 7:
                                        case 8:
                                        case 9:
                                        case 10:
                                        case 11:
                                            throw new RuntimeException("bitwise operation on a number greater than 32-bits not possible");
                                        case 14:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) < 0);
                                        case 15:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) > 0);
                                        case 16:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) <= 0);
                                        case 17:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) >= 0);
                                        case 18:
                                            z = obj != null ? false : false;
                                            return Boolean.valueOf(z);
                                        case 19:
                                            z = obj != null ? false : false;
                                            return Boolean.valueOf(z);
                                    }
                                case 14:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) < toFloat(obj2));
                                case 15:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) > toFloat(obj2));
                                case 16:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) <= toFloat(obj2));
                                case 17:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) >= toFloat(obj2));
                                case 18:
                                    if (obj != null || obj2 == null ? obj != obj2 : toFloat(obj) != toFloat(obj2)) {
                                    }
                                    return Boolean.valueOf(z);
                                case 19:
                                    if (obj != null || obj2 == null ? obj == obj2 : toFloat(obj) == toFloat(obj2)) {
                                    }
                                    return Boolean.valueOf(z);
                            }
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) < toDouble(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) > toDouble(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) <= toDouble(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) >= toDouble(obj2));
                        case 18:
                            if (obj != null || obj2 == null ? obj != obj2 : toDouble(obj) != toDouble(obj2)) {
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj != null || obj2 == null ? obj == obj2 : toDouble(obj) == toDouble(obj2)) {
                            }
                            return Boolean.valueOf(z);
                    }
                case 100:
                case 105:
                    switch (i2) {
                        case 0:
                            return Integer.valueOf(toShort(obj) + toShort(obj2));
                        case 1:
                            return Integer.valueOf(toShort(obj) - toShort(obj2));
                        case 2:
                            return Integer.valueOf(toShort(obj) * toShort(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Integer.valueOf(toShort(obj) % toShort(obj2));
                        case 5:
                            double dPow3 = Math.pow(toShort(obj), toShort(obj2));
                            if (dPow3 > 32767.0d) {
                                return Double.valueOf(dPow3);
                            }
                            return Short.valueOf((short) dPow3);
                        case 6:
                            return Integer.valueOf(((Short) obj).shortValue() & ((Short) obj2).shortValue());
                        case 7:
                            return Integer.valueOf(((Short) obj).shortValue() | ((Short) obj2).shortValue());
                        case 8:
                            return Integer.valueOf(((Short) obj).shortValue() ^ ((Short) obj2).shortValue());
                        case 9:
                            return Integer.valueOf(((Short) obj).shortValue() >> ((Short) obj2).shortValue());
                        case 10:
                            return Integer.valueOf(((Short) obj).shortValue() << ((Short) obj2).shortValue());
                        case 11:
                            return Integer.valueOf(((Short) obj).shortValue() >>> ((Short) obj2).shortValue());
                        case 14:
                            return Boolean.valueOf((obj == null || obj2 == null || toShort(obj) >= toShort(obj2)) ? false : true);
                        case 15:
                            return Boolean.valueOf((obj == null || obj2 == null || toShort(obj) <= toShort(obj2)) ? false : true);
                        case 16:
                            return Boolean.valueOf((obj == null || obj2 == null || toShort(obj) > toShort(obj2)) ? false : true);
                        case 17:
                            return Boolean.valueOf((obj == null || obj2 == null || toShort(obj) < toShort(obj2)) ? false : true);
                        case 18:
                            if (obj == null || obj2 == null ? obj != obj2 : toShort(obj) != toShort(obj2)) {
                                z = false;
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj == null || obj2 == null ? obj == obj2 : toShort(obj) == toShort(obj2)) {
                                z = false;
                            }
                            return Boolean.valueOf(z);
                    }
                case 102:
                case 107:
                    switch (i2) {
                        case 0:
                            return Long.valueOf(toLong(obj) + toLong(obj2));
                        case 1:
                            return Long.valueOf(toLong(obj) - toLong(obj2));
                        case 2:
                            return Long.valueOf(toLong(obj) * toLong(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Long.valueOf(toLong(obj) % toLong(obj2));
                        case 5:
                            dPow = Math.pow(toLong(obj), toLong(obj2));
                            if (dPow > 9.223372036854776E18d) {
                                return Double.valueOf(dPow);
                            }
                            return Long.valueOf((long) dPow);
                        case 6:
                            if (obj2 instanceof Integer) {
                            }
                        case 7:
                            if (obj2 instanceof Integer) {
                            }
                        case 8:
                            if (obj2 instanceof Integer) {
                            }
                        case 9:
                            if (obj2 instanceof Integer) {
                            }
                        case 10:
                            if (obj2 instanceof Integer) {
                            }
                        case 11:
                            if (obj2 instanceof Integer) {
                            }
                        case 12:
                            throw new UnsupportedOperationException("unsigned left-shift not supported");
                        case 13:
                        default:
                            Unit unit2 = (Unit) obj;
                            obj2 = unit2.convertFrom(obj2);
                            obj = Double.valueOf(unit2.getValue());
                            break;
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) < toLong(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) > toLong(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) <= toLong(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toLong(obj) >= toLong(obj2));
                        case 18:
                            z = obj != null ? false : false;
                            return Boolean.valueOf(z);
                        case 19:
                            z = obj != null ? false : false;
                            return Boolean.valueOf(z);
                    }
                    switch (i2) {
                        case 0:
                            return Double.valueOf(toDouble(obj) + toDouble(obj2));
                        case 1:
                            return Double.valueOf(toDouble(obj) - toDouble(obj2));
                        case 2:
                            return Double.valueOf(toDouble(obj) * toDouble(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Double.valueOf(toDouble(obj) % toDouble(obj2));
                        case 5:
                            return Double.valueOf(Math.pow(toDouble(obj), toDouble(obj2)));
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                            throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                        case 12:
                        case 13:
                        default:
                            switch (i2) {
                                case 0:
                                    return Float.valueOf(toFloat(obj) + toFloat(obj2));
                                case 1:
                                    return Float.valueOf(toFloat(obj) - toFloat(obj2));
                                case 2:
                                    return Float.valueOf(toFloat(obj) * toFloat(obj2));
                                case 3:
                                    return Double.valueOf(toDouble(obj) / toDouble(obj2));
                                case 4:
                                    return Float.valueOf(toFloat(obj) % toFloat(obj2));
                                case 5:
                                    return ParseTools.narrowType(asBigDecimal(obj).pow(((Number) obj2).intValue(), MATH_CONTEXT), -1);
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                    throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                                case 12:
                                case 13:
                                default:
                                    switch (i2) {
                                        case 0:
                                            return ((BigInteger) obj).add((BigInteger) obj2);
                                        case 1:
                                            return ((BigInteger) obj).subtract((BigInteger) obj2);
                                        case 2:
                                            return ((BigInteger) obj).multiply((BigInteger) obj2);
                                        case 3:
                                            return ((BigInteger) obj).divide((BigInteger) obj2);
                                        case 4:
                                            return ((BigInteger) obj).remainder((BigInteger) obj2);
                                        case 5:
                                            return ((BigInteger) obj).pow(((BigInteger) obj2).intValue());
                                        case 6:
                                        case 7:
                                        case 8:
                                        case 9:
                                        case 10:
                                        case 11:
                                            throw new RuntimeException("bitwise operation on a number greater than 32-bits not possible");
                                        case 14:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) < 0);
                                        case 15:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) > 0);
                                        case 16:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) <= 0);
                                        case 17:
                                            return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) >= 0);
                                        case 18:
                                            if (obj != null) {
                                            }
                                            return Boolean.valueOf(z);
                                        case 19:
                                            if (obj != null) {
                                            }
                                            return Boolean.valueOf(z);
                                    }
                                case 14:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) < toFloat(obj2));
                                case 15:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) > toFloat(obj2));
                                case 16:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) <= toFloat(obj2));
                                case 17:
                                    return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) >= toFloat(obj2));
                                case 18:
                                    z = obj != null ? false : false;
                                    return Boolean.valueOf(z);
                                case 19:
                                    z = obj != null ? false : false;
                                    return Boolean.valueOf(z);
                            }
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) < toDouble(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) > toDouble(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) <= toDouble(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) >= toDouble(obj2));
                        case 18:
                            z = obj != null ? false : false;
                            return Boolean.valueOf(z);
                        case 19:
                            z = obj != null ? false : false;
                            return Boolean.valueOf(z);
                    }
                case 103:
                case 109:
                    switch (i2) {
                        case 0:
                            return Double.valueOf(toDouble(obj) + toDouble(obj2));
                        case 1:
                            return Double.valueOf(toDouble(obj) - toDouble(obj2));
                        case 2:
                            return Double.valueOf(toDouble(obj) * toDouble(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Double.valueOf(toDouble(obj) % toDouble(obj2));
                        case 5:
                            return Double.valueOf(Math.pow(toDouble(obj), toDouble(obj2)));
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                            throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) < toDouble(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) > toDouble(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) <= toDouble(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) >= toDouble(obj2));
                        case 18:
                            if (obj != null) {
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj != null) {
                            }
                            return Boolean.valueOf(z);
                    }
                case 104:
                case 108:
                    switch (i2) {
                        case 0:
                            return Float.valueOf(toFloat(obj) + toFloat(obj2));
                        case 1:
                            return Float.valueOf(toFloat(obj) - toFloat(obj2));
                        case 2:
                            return Float.valueOf(toFloat(obj) * toFloat(obj2));
                        case 3:
                            return Double.valueOf(toDouble(obj) / toDouble(obj2));
                        case 4:
                            return Float.valueOf(toFloat(obj) % toFloat(obj2));
                        case 5:
                            return ParseTools.narrowType(asBigDecimal(obj).pow(((Number) obj2).intValue(), MATH_CONTEXT), -1);
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                            throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                        case 12:
                        case 13:
                        default:
                            switch (i2) {
                                case 0:
                                    return ((BigInteger) obj).add((BigInteger) obj2);
                                case 1:
                                    return ((BigInteger) obj).subtract((BigInteger) obj2);
                                case 2:
                                    return ((BigInteger) obj).multiply((BigInteger) obj2);
                                case 3:
                                    return ((BigInteger) obj).divide((BigInteger) obj2);
                                case 4:
                                    return ((BigInteger) obj).remainder((BigInteger) obj2);
                                case 5:
                                    return ((BigInteger) obj).pow(((BigInteger) obj2).intValue());
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                    throw new RuntimeException("bitwise operation on a number greater than 32-bits not possible");
                                case 14:
                                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) < 0);
                                case 15:
                                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) > 0);
                                case 16:
                                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) <= 0);
                                case 17:
                                    return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) >= 0);
                                case 18:
                                    if (obj != null) {
                                    }
                                    return Boolean.valueOf(z);
                                case 19:
                                    if (obj != null) {
                                    }
                                    return Boolean.valueOf(z);
                            }
                        case 14:
                            return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) < toFloat(obj2));
                        case 15:
                            return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) > toFloat(obj2));
                        case 16:
                            return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) <= toFloat(obj2));
                        case 17:
                            return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) >= toFloat(obj2));
                        case 18:
                            if (obj != null) {
                            }
                            return Boolean.valueOf(z);
                        case 19:
                            if (obj != null) {
                            }
                            return Boolean.valueOf(z);
                    }
            }
        }
        Unit unit3 = (Unit) obj;
        obj2 = unit3.convertFrom(obj2);
        obj = Double.valueOf(unit3.getValue());
        switch (i2) {
            case 0:
                return Double.valueOf(toDouble(obj) + toDouble(obj2));
            case 1:
                return Double.valueOf(toDouble(obj) - toDouble(obj2));
            case 2:
                return Double.valueOf(toDouble(obj) * toDouble(obj2));
            case 3:
                return Double.valueOf(toDouble(obj) / toDouble(obj2));
            case 4:
                return Double.valueOf(toDouble(obj) % toDouble(obj2));
            case 5:
                return Double.valueOf(Math.pow(toDouble(obj), toDouble(obj2)));
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                throw new RuntimeException("bitwise operation on a non-fixed-point number.");
            case 12:
            case 13:
            default:
                switch (i2) {
                    case 0:
                        return Float.valueOf(toFloat(obj) + toFloat(obj2));
                    case 1:
                        return Float.valueOf(toFloat(obj) - toFloat(obj2));
                    case 2:
                        return Float.valueOf(toFloat(obj) * toFloat(obj2));
                    case 3:
                        return Double.valueOf(toDouble(obj) / toDouble(obj2));
                    case 4:
                        return Float.valueOf(toFloat(obj) % toFloat(obj2));
                    case 5:
                        return ParseTools.narrowType(asBigDecimal(obj).pow(((Number) obj2).intValue(), MATH_CONTEXT), -1);
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        throw new RuntimeException("bitwise operation on a non-fixed-point number.");
                    case 12:
                    case 13:
                    default:
                        switch (i2) {
                            case 0:
                                return ((BigInteger) obj).add((BigInteger) obj2);
                            case 1:
                                return ((BigInteger) obj).subtract((BigInteger) obj2);
                            case 2:
                                return ((BigInteger) obj).multiply((BigInteger) obj2);
                            case 3:
                                return ((BigInteger) obj).divide((BigInteger) obj2);
                            case 4:
                                return ((BigInteger) obj).remainder((BigInteger) obj2);
                            case 5:
                                return ((BigInteger) obj).pow(((BigInteger) obj2).intValue());
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                                throw new RuntimeException("bitwise operation on a number greater than 32-bits not possible");
                            case 14:
                                return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) < 0);
                            case 15:
                                return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) > 0);
                            case 16:
                                return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) <= 0);
                            case 17:
                                return Boolean.valueOf(((BigInteger) obj).compareTo((BigInteger) obj2) >= 0);
                            case 18:
                                if (obj != null) {
                                }
                                return Boolean.valueOf(z);
                            case 19:
                                if (obj != null) {
                                }
                                return Boolean.valueOf(z);
                        }
                    case 14:
                        return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) < toFloat(obj2));
                    case 15:
                        return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) > toFloat(obj2));
                    case 16:
                        return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) <= toFloat(obj2));
                    case 17:
                        return Boolean.valueOf(obj == null && obj2 != null && toFloat(obj) >= toFloat(obj2));
                    case 18:
                        if (obj != null) {
                        }
                        return Boolean.valueOf(z);
                    case 19:
                        if (obj != null) {
                        }
                        return Boolean.valueOf(z);
                }
            case 14:
                return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) < toDouble(obj2));
            case 15:
                return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) > toDouble(obj2));
            case 16:
                return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) <= toDouble(obj2));
            case 17:
                return Boolean.valueOf(obj == null && obj2 != null && toDouble(obj) >= toDouble(obj2));
            case 18:
                if (obj != null) {
                }
                return Boolean.valueOf(z);
            case 19:
                if (obj != null) {
                }
                return Boolean.valueOf(z);
        }
        if (i2 == 0) {
            return String.valueOf(obj) + String.valueOf(obj2);
        }
        if (i2 == 18) {
            return safeEquals(obj2, obj);
        }
        if (i2 != 19) {
            return null;
        }
        return safeNotEquals(obj2, obj);
    }

    private static short toShort(Object obj) {
        return obj instanceof Short ? ((Short) obj).shortValue() : ((Number) obj).shortValue();
    }

    private static int toInteger(Object obj) {
        return obj instanceof Integer ? ((Integer) obj).intValue() : ((Number) obj).intValue();
    }

    private static long toLong(Object obj) {
        return obj instanceof Long ? ((Long) obj).longValue() : ((Number) obj).longValue();
    }

    private static double toDouble(Object obj) {
        return obj instanceof Double ? ((Double) obj).doubleValue() : ((Number) obj).doubleValue();
    }

    private static float toFloat(Object obj) {
        return obj instanceof Float ? ((Float) obj).floatValue() : ((Number) obj).floatValue();
    }

    private static Double getNumber(Object obj, int i) {
        if (obj == null || obj == BlankLiteral.INSTANCE) {
            return Double.valueOf(0.0d);
        }
        if (i == 0) {
            return Double.valueOf(obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj));
        }
        if (i == 1) {
            return Double.valueOf(Double.parseDouble((String) obj));
        }
        if (i != 7) {
            if (i != 8) {
                if (i != 9) {
                    if (i != 15) {
                        switch (i) {
                            case 100:
                            case 101:
                            case 102:
                            case 103:
                            case 104:
                            case 105:
                            case 106:
                            case 107:
                            case 108:
                            case 109:
                            case 110:
                            case 111:
                                return Double.valueOf(((Number) obj).doubleValue());
                            case 112:
                                break;
                            case 113:
                                break;
                            default:
                                throw new RuntimeException("cannot convert <" + obj + "> to a numeric type: " + obj.getClass() + " [" + i + "]");
                        }
                    }
                }
                return Double.valueOf(((Byte) obj).doubleValue());
            }
            return Double.valueOf(Double.parseDouble(String.valueOf(obj)));
        }
        return Double.valueOf(((Boolean) obj).booleanValue() ? 1.0d : 0.0d);
    }

    private static BigDecimal asBigDecimal(Object obj) {
        if (obj == null || obj == BlankLiteral.INSTANCE) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof String) {
            return new BigDecimal((String) obj);
        }
        if (obj instanceof Number) {
            return new BigDecimal(((Number) obj).doubleValue());
        }
        throw new RuntimeException("cannot convert <" + obj + "> to a numeric type: " + obj.getClass());
    }
}
