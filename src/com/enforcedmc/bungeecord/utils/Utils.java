package com.enforcedmc.bungeecord.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class Utils {
	private String value;
	private static final Map<String, String> translate;
	private static final DecimalFormat FORMAT;

	static {
		FORMAT = new DecimalFormat("0.#");
		translate = new HashMap<String, String>();
		createMap();
	}

	public static final String toColor(final String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static final TextComponent buildComponent(final String text, final String display, final String command) {
		final TextComponent component = new TextComponent(toColor(display));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor(text)).create()));
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
		return component;
	}

	public static final String getMessage(final int start, final String[] args) {
		final StringBuilder sb = new StringBuilder();
		for(int i = start; i < args.length; ++i)
			sb.append(args[i]).append(" ");
		return sb.toString();
	}

	public void FontFormat(final String value) {
		this.value = "§" + value;
	}

	@Override
	public String toString() {
		return value;
	}

	private static void createMap() {
		Utils.translate.put("&0", "§0");
		Utils.translate.put("&1", "§1");
		Utils.translate.put("&2", "§2");
		Utils.translate.put("&3", "§3");
		Utils.translate.put("&4", "§4");
		Utils.translate.put("&5", "§5");
		Utils.translate.put("&6", "§6");
		Utils.translate.put("&7", "§7");
		Utils.translate.put("&8", "§8");
		Utils.translate.put("&9", "§9");
		Utils.translate.put("&a", "§a");
		Utils.translate.put("&b", "§b");
		Utils.translate.put("&c", "§c");
		Utils.translate.put("&d", "§d");
		Utils.translate.put("&e", "§e");
		Utils.translate.put("&f", "§f");
		Utils.translate.put("&k", "§k");
		Utils.translate.put("&l", "§l");
		Utils.translate.put("&m", "§m");
		Utils.translate.put("&n", "§n");
		Utils.translate.put("&o", "§o");
		Utils.translate.put("&r", "§r");
	}

	public static String translateString(String value) {
		for(final String code : Utils.translate.keySet())
			value = value.replace(code, Utils.translate.get(code));
		return value;
	}

	public static final String formatSeconds(long time, final boolean minus) {
		time = minus ? time - System.currentTimeMillis() : time;
		return String.valueOf(String.valueOf(Utils.FORMAT.format(time / 1000.0))) + "s";
	}

	public static final String formatTime(final long l) {
		final long time = l - System.currentTimeMillis();
		final int days = (int) TimeUnit.MILLISECONDS.toDays(time);
		final int hours = (int) TimeUnit.MILLISECONDS.toHours(time) % 24;
		final int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(time) % 60;
		final int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(time) % 60;
		return String.valueOf(String.valueOf(days > 1 ? new StringBuilder(String.valueOf(days)).append("d ").toString() : ""))
				+ (hours > 1 ? String.valueOf(String.valueOf(hours)) + "h " : "") + (minutes > 1 ? String.valueOf(String.valueOf(minutes)) + "m " : "")
				+ (seconds > 1 ? String.valueOf(String.valueOf(seconds)) + "s" : "");
	}

	public static String getTime(final Long time) {
		final long timeleft = time;
		final int days = (int) TimeUnit.MILLISECONDS.toDays(timeleft);
		final int hours = (int) (TimeUnit.MILLISECONDS.toHours(timeleft) - TimeUnit.DAYS.toHours(days));
		final int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(timeleft) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours)));
		final int second = (int) (TimeUnit.MILLISECONDS.toSeconds(timeleft)
				- (TimeUnit.DAYS.toSeconds(days) + TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minute)));
		final String remaining = String.valueOf(String.valueOf(days)) + " §6Days " + hours + " §6Hours " + minute + " §6Minutes " + second + " §6Seconds";
		return remaining;
	}

	public static boolean checkForIp(final String str) {
		return Pattern.compile("[0-9]{1,3}[^0-9a-zA-Z ][0-9]{1,3}[^0-9a-zA-Z ][0-9]{1,3}[^0-9a-zA-Z ][0-9]{1,3}:[1-9]{0,5}").matcher(str).find();
	}

	public static boolean checkForDomain(final String str) {
		final String domainPattern = "([a-z-0-9]{1,50})\\.(" + getValidLTDs() + ")(?![a-z0-9])";
		return Pattern.compile(domainPattern).matcher(str).find();
	}

	public static String replacePlaceholder(final String msg, final String placeHolder, final String value) {
		return msg.replace(placeHolder, value);
	}

	@SuppressWarnings("deprecation")
	public static void broadcast(final String message, final String permission, boolean component) {
		for(final ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
			if(permission != null && !online.hasPermission(permission))
				continue;
			if(component)
				online.sendMessage(new TextComponent(message));
			else
				online.sendMessage(message);
		}
	}

	public static void broadcast(final String message, final String permission) {
		broadcast(message, permission, true);
	}

	public static void broadcast(final String message) {
		broadcast(message, null);
	}

	public static String getAdvertisement(final String input) {
		String result = null;
		final String[] parts = input.split(" ");
		String[] array;
		for(int length = (array = parts).length, i = 0; i < length; ++i) {
			final String s = array[i];
			if(checkForDomain(s))
				result = s;
		}
		if(result == null) {
			String[] array2;
			for(int length2 = (array2 = parts).length, j = 0; j < length2; ++j) {
				final String s2 = array2[j];
				if(checkForIp(s2)) {
					result = s2;
					break;
				}
			}
		}
		return result;
	}
	
	private static final String getValidLTDs() {
		return "AC|ACADEMY|ACCOUNTANTS|ACTOR|AD|AE|AERO|AF|AG|AGENCY|AI|AIRFORCE|AL|AM|AN|AO|AQ|AR|ARCHI|ARPA|AS|ASIA|ASSOCIATES|AT|AU|AW|AX|AXA|AZ|BA|BAR|BARGAINS|BAYERN|BB|BD|BE|BERLIN|BEST|BF|BG|BH|BI|BID|BIKE|BIZ|BJ|BLACK|BLACKFRIDAY|BLUE|BM|BN|BO|BOUTIQUE|BR|BS|BT|BUILD|BUILDERS|BUZZ|BV|BW|BY|BZ|CA|CAB|CAMERA|CAMP|CAPITAL|CARDS|CARE|CAREER|CAREERS|CASH|CAT|CATERING|CC|CD|CENTER|CEO|CF|CG|CH|CHEAP|CHRISTMAS|CI|CITIC|CK|CL|CLAIMS|CLEANING|CLINIC|CLOTHING|CLUB|CM|CN|CO|CODES|COFFEE|COLLEGE|COLOGNE|COM|COMMUNITY|COMPANY|COMPUTER|CONDOS|CONSTRUCTION|CONSULTING|CONTRACTORS|COOKING|COOL|COOP|COUNTRY|CR|CREDIT|CREDITCARD|CRUISES|CU|CV|CW|CX|CY|CZ|DANCE|DATING|DE|DEMOCRAT|DENTAL|DESI|DIAMONDS|DIGITAL|DIRECTORY|DISCOUNT|DJ|DK|DM|DNP|DO|DOMAINS|DZ|EC|EDU|EDUCATION|EE|EG|EMAIL|ENGINEERING|ENTERPRISES|EQUIPMENT|ER|ES|ESTATE|ET|EU|EUS|EVENTS|EXCHANGE|EXPERT|EXPOSED|FAIL|FARM|FEEDBACK|FI|FINANCE|FINANCIAL|FISH|FISHING|FITNESS|FJ|FK|FLIGHTS|FLORIST|FM|FO|FOO|FOUNDATION|FR|FROGANS|FUND|FURNITURE|FUTBOL|GA|GAL|GALLERY|GB|GD|GE|GF|GG|GH|GI|GIFT|GL|GLASS|GLOBO|GM|GMO|GN|GOP|GOV|GP|GQ|GR|GRAPHICS|GRATIS|GRIPE|GS|GT|GU|GUITARS|GURU|GW|GY|HAUS|HK|HM|HN|HOLDINGS|HOLIDAY|HORSE|HOUSE|HR|HT|HU|ID|IE|IL|IM|IMMOBILIEN|IN|INDUSTRIES|INFO|INK|INSTITUTE|INSURE|INT|INTERNATIONAL|INVESTMENTS|IO|IQ|IR|IS|IT|JE|JETZT|JM|JO|JOBS|JP|KAUFEN|KE|KG|KH|KI|KIM|KITCHEN|KIWI|KM|KN|KOELN|KP|KR|KRED|KW|KY|KZ|LA|LAND|LB|LC|LEASE|LI|LIGHTING|LIMITED|LIMO|LINK|LK|LONDON|LR|LS|LT|LU|LUXURY|LV|LY|MA|MAISON|MANAGEMENT|MANGO|MARKETING|MC|MD|ME|MEDIA|MEET|MENU|MG|MH|MIAMI|MIL|MK|ML|MM|MN|MO|MOBI|MODA|MOE|MONASH|MOSCOW|MP|MQ|MR|MS|MT|MU|MUSEUM|MV|MW|MX|MY|MZ|NA|NAGOYA|NAME|NC|NE|NET|NEUSTAR|NF|NG|NI|NINJA|NL|NO|NP|NR|NU|NYC|NZ|OKINAWA|OM|ONL|ORG|PA|PARIS|PARTNERS|PARTS|PE|PF|PG|PH|PHOTO|PHOTOGRAPHY|PHOTOS|PICS|PICTURES|PINK|PK|PL|PLUMBING|PM|PN|POST|PR|PRO|PRODUCTIONS|PROPERTIES|PS|PT|PUB|PW|PY|QA|QPON|QUEBEC|RE|RECIPES|RED|REISEN|REN|RENTALS|REPAIR|REPORT|REST|REVIEWS|RICH|RO|ROCKS|RODEO|RS|RU|RUHR|RW|RYUKYU|SA|SAARLAND|SB|SC|SCHULE|SD|SE|SERVICES|SEXY|SG|SH|SHIKSHA|SHOES|SI|SINGLES|SJ|SK|SL|SM|SN|SO|SOCIAL|SOHU|SOLAR|SOLUTIONS|SOY|SR|ST|SU|SUPPLIES|SUPPLY|SUPPORT|SURGERY|SV|SX|SY|SYSTEMS|SZ|TATTOO|TAX|TC|TD|TECHNOLOGY|TEL|TF|TG|TH|TIENDA|TIPS|TJ|TK|TL|TM|TN|TO|TODAY|TOKYO|TOOLS|TOWN|TOYS|TP|TR|TRADE|TRAINING|TRAVEL|TT|TV|TW|TZ|UA|UG|UK|UNIVERSITY|UNO|US|UY|UZ|VA|VACATIONS|VC|VE|VEGAS|VENTURES|VG|VI|VIAJES|VILLAS|VISION|VN|VODKA|VOTE|VOTING|VOTO|VOYAGE|VU|WANG|WATCH|WEBCAM|WED|WF|WIEN|WIKI|WORKS|WS|WTC|WTF|XN--3BST00M|XN--3DS443G|XN--3E0B707E|XN--45BRJ9C|XN--55QW42G|XN--55QX5D|XN--6FRZ82G|XN--6QQ986B3XL|XN--80ADXHKS|XN--80AO21A|XN--80ASEHDB|XN--80ASWG|XN--90A3AC|XN--C1AVG|XN--CG4BKI|XN--CLCHC0EA0B2G2A9GCD|XN--CZRU2D|XN--D1ACJ3B|XN--FIQ228C5HS|XN--FIQ64B|XN--FIQS8S|XN--FIQZ9S|XN--FPCRJ9C3D|XN--FZC2C9E2C|XN--GECRJ9C|XN--H2BRJ9C|XN--I1B6B1A6A2E|XN--IO0A7I|XN--J1AMH|XN--J6W193G|XN--KPRW13D|XN--KPRY57D|XN--L1ACC|XN--LGBBAT1AD8J|XN--MGB9AWBF|XN--MGBA3A4F16A|XN--MGBAAM7A8H|XN--MGBAB2BD|XN--MGBAYH7GPA|XN--MGBBH1A71E|XN--MGBC0A9AZCG|XN--MGBERP4A5D4AR|XN--MGBX4CD0AB|XN--NGBC5AZD|XN--NQV7F|XN--NQV7FS00EMA|XN--O3CW4H|XN--OGBPF8FL|XN--P1AI|XN--PGBS0DH|XN--Q9JYB4C|XN--RHQV96G|XN--S9BRJ9C|XN--SES554G|XN--UNUP4Y|XN--WGBH1C|XN--WGBL6A|XN--XKC2AL3HYE2A|XN--XKC2DL3A5EE0H|XN--YFRO4I67O|XN--YGBI2AMMX|XN--ZFR164B|XXX|XYZ|YE|YOKOHAMA|YT|ZA|ZM|ZONE|ZW";
	}
}