package com.diting.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.diting.error.AppErrors;
import com.diting.model.options.PageableOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.dropwizard.jackson.AnnotationSensitivePropertyNamingStrategy;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.GuavaExtrasModule;
import io.dropwizard.jackson.LogbackModule;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.solr.common.SolrDocument;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils.
 */
public final class Utils {
    private static final Document.OutputSettings ESCAPE_SETTINGS =
            new Document.OutputSettings().prettyPrint(true).escapeMode(Entities.EscapeMode.xhtml).charset("UTF-8");

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{11}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new GuavaModule());
        OBJECT_MAPPER.registerModule(new LogbackModule());
        OBJECT_MAPPER.registerModule(new GuavaExtrasModule());
        OBJECT_MAPPER.registerModule(new JodaModule());
        OBJECT_MAPPER.registerModule(new AfterburnerModule());
        //OBJECT_MAPPER.registerModule(new FuzzyEnumModule());
        OBJECT_MAPPER.setPropertyNamingStrategy(new AnnotationSensitivePropertyNamingStrategy());
        OBJECT_MAPPER.setSubtypeResolver(new DiscoverableSubtypeResolver());

        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        OBJECT_MAPPER.setDateFormat(new ISO8601DateFormat());
    }

    private Utils() {
        // private ctor
    }

    // ===== File ===== //
    public static String readFileContent(String fileName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(getLineSeparator());
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred during read file [" + fileName + "].", e);
        }

        return result.toString();
    }

    public static void appendFile(File file, String content) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.print(content);
        } catch (Exception e) {
            //ignore silently
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static File createFile(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }


    public static File createDir(String dirName) {
        File dir = new File(dirName);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    public static void writeFile(File file, String content) {
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(bufferedWriter);
            close(outputStreamWriter);
        }
    }

    // ===== JSON =====//
    public static String toJacksonJSON(Object input) {
        if (input == null)
            return null;

        try {
            return OBJECT_MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJacksonJson(String input, Class<T> clazz) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(input, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String toJSON(Object input) {
        if (input == null)
            return null;

        return JSON.toJSONString(input, SerializerFeature.PrettyFormat);
    }

    public static String toJson(Object input, String[] filterProperties) {
        if (input == null)
            return null;

        SerializeWriter writer = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(writer);

        // apply filters
        if (filterProperties != null) {
            final Set<String> filters = new HashSet(Arrays.asList(filterProperties));
            serializer.getPropertyFilters().add(new PropertyFilter() {
                public boolean apply(Object source, String name, Object value) {
                    return !filters.contains(name);
                }
            });
        }

        serializer.write(input);
        return writer.toString();
    }

    public static <T> T fromJson(String input, Class<T> clazz) {
        return JSON.parseObject(input, clazz);
    }

    public static JSONObject str2json(String input){
        if (input == null)
            return null;
        return new JSONObject(input);
    }

    // ===== Date =====//
    public static Date now() {
        return new Date();
    }

    public static Long timeDifference(Date date,Date date1) {
        return date1.getTime()-date.getTime();
    }

    public static Long nowTime() {
        return now().getTime();
    }

    public static Date parseDate(String source, DateFormat format) {
        if (source == null)
            return null;

        try {
            return format.parse(source);
        } catch (ParseException e) {
            // ignore silently
        }

        return null;
    }

    public static String date2iso(Date date) {
        return date == null ? null : ISO8601Utils.format(date);
    }

    public static Date daysBefore(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * days);

        return calendar.getTime();
    }

    public static Date daysBefore(Date time, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());

        calendar.add(Calendar.DATE, -1 * days);

        return calendar.getTime();
    }

    public static Date daysAfter(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);

        return calendar.getTime();
    }

    public static Date daysAfter(Date time, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());

        calendar.add(Calendar.DATE, days);

        return calendar.getTime();
    }

    public static Date getDayStart(Date time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date minutesBefore(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1 * minutes);

        return calendar.getTime();
    }

    public static Date minutesBefore(Date time, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());

        calendar.add(Calendar.MINUTE, -1 * minutes);

        return calendar.getTime();
    }

    public static Date minutesAfter(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now().getTime());
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    public static Date minutesAfter(Date time, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());

        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    public static Date today() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static int differentDays(Date date1,Date date2){
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    public static int daysOfTwo(Date fDate, Date oDate) {
         Calendar aCalendar = Calendar.getInstance();
         aCalendar.setTime(fDate);
         int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
         aCalendar.setTime(oDate);
         int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
         return day2 - day1;
    }
    // ===== Check ===== //
    public static boolean validateMobile(String mobile) {
        Matcher m = MOBILE_PATTERN.matcher(mobile);
        return m.matches();
    }

    public static boolean validateEmail(String email) {
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    public static boolean validateInteger(String value) {
        Matcher m = INTEGER_PATTERN.matcher(value);
        return m.matches();
    }

    public static <T> T checkNull(Object param, String paramName) {
        if (param == null) {
            throw AppErrors.INSTANCE.missingField(paramName).exception();
        }

        return (T) param;
    }

    public static String checkString(String param, String paramName) {
        if (param == null || param.trim().length() == 0) {
            throw AppErrors.INSTANCE.missingField(paramName).exception();
        }

        return param;
    }

    public static boolean equals(String a, String b) {
        if (a == null && b == null)
            return true;

        if ((a == null && b != null) || (a != null && b == null)) {
            return false;
        }

        return a.equalsIgnoreCase(b);
    }

    public static boolean equalsCaseSensitive(String a, String b) {
        if (a == null && b == null)
            return true;

        if ((a == null && b != null) || (a != null && b == null)) {
            return false;
        }

        return a.equals(b);
    }

    public static boolean equals(Integer a, Integer b) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static <T> boolean isCollectionEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isArrayEmpty(Object[] objects) {
        return objects == null || (objects != null && objects.length == 0);
    }

    public static String nullIfEmpty(String value) {
        return isEmpty(value) ? null : value;
    }

    public static <T> List<T> nullIfEmpty(List<T> collections) {
        return isCollectionEmpty(collections) ? null : collections;
    }

    public static String isNull(String input, String defaultValue) {
        return input == null ? defaultValue : input;
    }

    public static Integer isNull(Integer input, Integer defaultValue) {
        return input == null ? defaultValue : input;
    }

    // ===== Convert ===== //
    public static String str(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static Integer str2int(String input) {
        try {
            return isEmpty(input) ? null : Integer.valueOf(input);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long str2long(String input) {
        return isEmpty(input) ? null : Long.valueOf(input);
    }

    public static Double str2double(String input) {
        return isEmpty(input) ? null : Double.valueOf(input);
    }

    public static BigDecimal str2decimal(String input) {
        return isEmpty(input) ? null : new BigDecimal(input);
    }

    public static Date str2Date(String input) {
        try {
            return input == null ? null : Constants.DATE_FORMAT.parse(input);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date str_Date(String input) {
        try {
            return input == null ? null : Constants.TIMESTAMP_FORMAT.parse(input);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date str2Date(String input, DateFormat format) {
        try {
            return input == null ? null : format.parse(input);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    public static Date isoStr2date(String input) {
//        return StringUtils.isEmpty(input) ? null : ISO8601Utils.parse(input);
//    }

    public static Long date2long(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String date2str(Date date) {
        if (date == null)
            return null;

        return Constants.DATE_FORMAT.format(date);
    }

    public static String datetime2str(Date time) {
        if (time == null)
            return null;

        return Constants.DATETIME_FORMAT.format(time);
    }

    public static String timeRange2str(Date time) {
        if (time == null)
            return null;

        return Constants.TIME_RANGE_FORMAT.format(time);
    }

    public static String timeMsec2str(Date time) {
        if (time == null)
            return null;

        return Constants.TIME_MSEC_FORMAT.format(time);
    }

    public static String time2str(Date time) {
        if (time == null)
            return null;

        return Constants.TIMESTAMP_FORMAT.format(time);
    }

    public static String timeAPM2str(Date time) {
        if (time == null)
            return null;

        return Constants.TIME_APM_FORMAT.format(time);
    }

    public static List<Integer> str2int(Collection<String> input) {
        if (input == null)
            return null;

        List<Integer> results = new ArrayList<>();
        for (String entry : input) {
            if (!isEmpty(entry)) {
                results.add(Integer.parseInt(entry));
            }
        }

        return results;
    }

    public static String convertDecimalNCRToString(String ncr) {
        if (ncr == null)
            return null;

        ncr = ncr.replace("&#", "");
        String[] split = ncr.split(";");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            sb.append((char) Integer.parseInt(split[i]));
        }

        return sb.toString();
    }

    // ===== Collection ===== //
    public static <T> T getUnique(List<T> list) {
        if (list == null || list.size() != 1)
            throw new RuntimeException("List is empty or contains more than one element.");

        return list.get(0);
    }

    public static <T> T getFirst(List<T> list) {
        if (list == null || list.isEmpty())
            throw new RuntimeException("List is empty.");

        return list.get(0);
    }

    public static <T> T getLast(List<T> list) {
        if (list == null || list.isEmpty())
            throw new RuntimeException("List is empty.");

        return list.get(list.size() - 1);
    }

    // ===== Misc ===== //
    public static String find(Pattern pattern, String content) {
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String text = matcher.group(1);
            return text == null ? null : text.trim().replaceAll("&nbsp;", "");
        } else {
            return null;
        }
    }

    public static String find(Pattern pattern, String content, Integer index) {
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String text = matcher.group(index);
            return text == null ? null : text.trim().replaceAll("&nbsp;", "");
        } else {
            return null;
        }
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String trim(String input) {
        if (input == null)
            return null;

        return input.trim();
    }

    public static void buildPageableOptions(UriInfo uriInfo, PageableOptions options) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        options.setPageNo(str2int(isNull(params.getFirst("pageNo"), "1")));
        options.setPageSize(str2int(isNull(params.getFirst("pageSize"), "15")));
    }

    public static BigDecimal divide(Integer a, Integer b) {
        return new BigDecimal(a).divide(new BigDecimal(b), 2, RoundingMode.HALF_UP);
    }

    public static String tidyHtml(String html) {
        if (html == null)
            return null;

        return Jsoup.clean(html, "", Whitelist.none(), ESCAPE_SETTINGS);
    }

    public static String hash(String input) {
        if (input == null)
            return null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(input.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);

            String result = bigInt.toString(16);
            if (result.length() > 32)
                result = result.substring(0, 32);

            // forget to padding left 0 to len-32, which does not meet MD5 standard

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String input) {
        try {
            return DigestUtils.md5Hex(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha1(String input) {
        if (input == null)
            return null;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(input.getBytes("UTF-8"));
            return byte2Hex(crypt.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha1(String name, String password) {
        return new SimpleHash("SHA-1", name, password).toString();
    }

    public static String byte2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String encryptBase64(String input) {
        if (input == null)
            return null;
        try {
            byte[] encryptByte = input.getBytes();
            Base64 base64 = new Base64();
            encryptByte = base64.encode(encryptByte);
            return new String(encryptByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeBase64(String input) {
        if (input == null)
            return null;
        try {
            byte[] decodeByte = input.getBytes();
            Base64 base64 = new Base64();
            decodeByte = base64.decode(decodeByte);
            return new String(decodeByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore silently
            }
        }
    }

    public static byte[] serialize(Object obj) {
        if (obj == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);

            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ex) {
                // silently ignore
            }

            try {
                bos.close();
            } catch (IOException ex) {
                // silently ignore
            }
        }
    }

    public static byte[] toBytes(String string) {
        try {
            return string == null ? null : string.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toStr(byte[] bytes) {
        return bytes == null ? null : new String(bytes);
    }

    public static <T> T deserialize(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(bis);

            return (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // silently ignore
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // silently ignore
            }
        }
    }

    public static <T> T coalesce(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }

        return null;
    }

    public static Class<?> guessComponentType(List list) {
        if (list == null)
            return null;

        for (Object element : list) {
            if (element == null)
                continue;

            return element.getClass();
        }

        return null;
    }

    public static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void inject(Object instance, String fieldName, Object fieldValue) {
        checkNull(instance, "instance");
        checkString(fieldName, "fieldName");
        checkNull(fieldValue, "fieldValue");

        try {
            Field field = getField(instance.getClass(), fieldName);
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(instance, fieldValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String safeString(String s) {
        return s == null ? "" : s;
    }

    public static String safeGet(SolrDocument doc, String key) {
        return doc.get(key) != null ? doc.get(key).toString() : null;
    }

    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    public static String generateToken(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
