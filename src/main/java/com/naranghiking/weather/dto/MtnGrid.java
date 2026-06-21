package com.naranghiking.weather.dto;

public enum MtnGrid {

    GARISAN("가리산", 76, 130),
    GARIWANGSAN("가리왕산", 87, 125),
    GAYASAN("가야산", 80, 89),
    GAJISAN("가지산", 97, 85),
    GAMAKSAN("감악산", 60, 136),
    GAEBANGSAN("개방산", 77, 137),
    GEONBONGSAN("건봉산", 83, 143),
    GYERYONGSAN("계룡산", 64, 101),
    GYEBANGSAN("계방산", 85, 128),
    GONGJAKSAN("공작산", 78, 131),
    GWANAKSAN("관악산", 60, 124),
    GWANGGYOSAN("광교산", 61, 121),
    GUBYEONGSAN("구병산", 75, 104),
    GEUMSUSAN("금수산", 82, 113),
    GEUMOSAN("금오산", 84, 96),
    GEUMJEONGSAN("금정산", 97, 78),
    GIBAEKSAN("기백산", 75, 87),
    NAMDEOGYUSAN("남덕유산", 73, 88),
    NAMSAN("남산", 60, 126),
    NAEYEONSAN("내연산", 101, 99),
    NAEJANGSAN("내장산", 59, 82),
    DAEDUNSAN("대둔산", 66, 96),
    DAEYASAN("대야산", 79, 96),
    DEOKSUNGSAN("덕숭산", 54, 107),
    DEOGYUSAN("덕유산", 74, 90),
    DEOKHANGSAN("덕항산", 97, 119),
    DORAKSAN("도락산", 84, 114),
    DOBONGSAN("도봉산", 60, 129),
    DURYUNSAN("두륜산", 54, 60),
    DUTASAN("두타산", 96, 121),
    MANISAN("마니산", 50, 124),
    MAISAN("마이산", 73, 87),
    MYEONGSEONGSAN("명성산", 68, 138),
    MYEONGJISAN("명지산", 67, 134),
    MOAKSAN("모악산", 62, 87),
    MUDEUNGSAN("무등산", 61, 74),
    MUHAKSAN("무학산", 88, 79),
    MIRYEUKSAN("미륵산", 86, 68),
    MINJUJISAN("민주지산", 76, 93),
    BANGTAESAN("방태산", 83, 138),
    BAEKDEOKSAN("백덕산", 82, 122),
    BAEGUNSANGWANGYANG("백운산(광양)", 73, 73),
    BAEGUNSANPOCHEON("백운산(포천)", 67, 136),
    BAEGUNSANJEONGSEON("백운산(정선)", 90, 122),
    BYEONSAN("변산", 53, 85),
    BUKHANSAN("북한산", 60, 129),
    BISEULSAN("비슬산", 87, 87),
    SAMAKSAN("삼악산", 72, 133),
    SEODAESAN("서대산", 71, 99),
    SEONUNSAN("선운산", 53, 86),
    SEORAKSAN("설악산", 85, 139),
    SEONGINBONG("성인봉", 127, 128),
    SOBAEKSAN("소백산", 86, 114),
    SOYOSAN("소요산", 62, 134),
    SONGNISAN("속리산", 76, 105),
    SINBULSAN("신불산", 99, 84),
    ODAESAN("오대산", 87, 132),
    YONGMUNSAN("용문산", 70, 126),
    UNMUNSAN("운문산", 95, 85),
    UNAKSAN("운악산", 65, 133),
    UNJANGSAN("운장산", 67, 90),
    WORAKSAN("월악산", 79, 112),
    WOLCHULSAN("월출산", 56, 66),
    YUMYEONGSAN("유명산", 70, 128),
    EUNGBONGSAN("응봉산", 96, 124),
    IRWOLSAN("일월산", 97, 115),
    JIRISAN("지리산", 74, 78),
    CHEONGWANSAN("천관산", 59, 64),
    CHEONMASAN("천마산", 67, 127),
    CHEONSEONGSAN("천성산", 98, 82),
    CHEONGNYANGSAN("청량산", 94, 108),
    CHEONGOKSAN("청옥산", 96, 119),
    CHUWOLSAN("추월산", 59, 76),
    CHUNGNYEONGSAN("축령산", 66, 128),
    CHIAKSAN("치악산", 79, 123),
    CHILGAPSAN("칠갑산", 58, 100),
    TAEBAEKSAN("태백산", 93, 117),
    PALGONGSAN("팔공산", 90, 93),
    PALBONGSAN("팔봉산", 77, 131),
    HALLASAN("한라산", 53, 35),
    HWANGMAESAN("황매산", 80, 82),
    HWANGSEOKSAN("황석산", 75, 86),
    HWANGAKSAN("황악산", 78, 96),
    HWANGJANGSAN("황장산", 80, 111),
    Test("test", 80, 80);

    private final String name;
    private final int nx;
    private final int ny;

    MtnGrid(String name, int nx, int ny) {
        this.name = name;
        this.nx = nx;
        this.ny = ny;
    }

    public static MtnGrid fromName(String name) {
        for (MtnGrid m : values()) {
            if (m.name.equals(name)) return m;
        }
        throw new IllegalArgumentException("존재하지 않는 산: " + name);
    }

    public String getName() { return name; }
    public int getNx() { return nx; }
    public int getNy() { return ny; }
}