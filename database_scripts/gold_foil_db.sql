--
-- PostgreSQL database cluster dump
--

-- Started on 2025-06-26 09:36:08

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS;

--
-- User Configurations
--








--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.0

-- Started on 2025-06-26 09:36:08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Completed on 2025-06-26 09:36:09

--
-- PostgreSQL database dump complete
--

--
-- Database "gold_foil_db" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.0

-- Started on 2025-06-26 09:36:09

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5041 (class 1262 OID 16387)
-- Name: gold_foil_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE gold_foil_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Chinese (Simplified)_China.936';


ALTER DATABASE gold_foil_db OWNER TO postgres;

\connect gold_foil_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 871 (class 1247 OID 16389)
-- Name: special_char; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.special_char AS ENUM (
    'V',
    'X',
    'NA',
    '▷'
);


ALTER TYPE public.special_char OWNER TO postgres;

--
-- TOC entry 874 (class 1247 OID 16398)
-- Name: special_char_new; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.special_char_new AS ENUM (
    'V',
    'X',
    'NA_Enum',
    '▷'
);


ALTER TYPE public.special_char_new OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16407)
-- Name: cloth_paper; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cloth_paper (
    id integer NOT NULL,
    project_id integer NOT NULL,
    jht_8001 public.special_char_new,
    jht_8004 public.special_char_new,
    jht_8013 public.special_char_new,
    jht_8002 public.special_char_new,
    jht_8010 public.special_char_new,
    jht_8014 public.special_char_new,
    jht_8015 public.special_char_new,
    lts_8003 public.special_char_new,
    polyester_esm public.special_char_new,
    jht_8003 public.special_char_new,
    jht_8017 public.special_char_new,
    jht_8006 public.special_char_new,
    jht_8008 public.special_char_new,
    jht_8011 public.special_char_new,
    jht_8016 public.special_char_new,
    jht_8018 public.special_char_new,
    jht_8019 public.special_char_new,
    jht_910 public.special_char_new,
    jht_8007 public.special_char_new,
    jht_8009 public.special_char_new,
    jht_8012 public.special_char_new,
    jht_0001_0103 public.special_char_new,
    jht_0104_0195 public.special_char_new,
    jht_0199_0209 public.special_char_new,
    jht_0211 public.special_char_new,
    jht_0213 public.special_char_new,
    jht_0196_0198 public.special_char_new,
    jht_0265_0311 public.special_char_new,
    jht_0216 public.special_char_new,
    jht_0218 public.special_char_new,
    jht_0312_0330 public.special_char_new,
    jht_0219_0264 public.special_char_new,
    jht_0334_0351 public.special_char_new,
    jht_0352_0386 public.special_char_new,
    jht_0387_0407 public.special_char_new,
    jht_0419_0429 public.special_char_new
);


ALTER TABLE public.cloth_paper OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16410)
-- Name: cloth_paper_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cloth_paper_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cloth_paper_id_seq OWNER TO postgres;

--
-- TOC entry 5042 (class 0 OID 0)
-- Dependencies: 218
-- Name: cloth_paper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cloth_paper_id_seq OWNED BY public.cloth_paper.id;


--
-- TOC entry 219 (class 1259 OID 16411)
-- Name: film_butter_paper; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.film_butter_paper (
    id integer NOT NULL,
    project_id integer NOT NULL,
    vet_vvc_avet public.special_char_new,
    scratch_averse_avet_no_foil public.special_char_new,
    butter_paper_gtf public.special_char_new,
    butter_paper_ztf public.special_char_new
);


ALTER TABLE public.film_butter_paper OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16414)
-- Name: film_butter_paper_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.film_butter_paper_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.film_butter_paper_id_seq OWNER TO postgres;

--
-- TOC entry 5043 (class 0 OID 0)
-- Dependencies: 220
-- Name: film_butter_paper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.film_butter_paper_id_seq OWNED BY public.film_butter_paper.id;


--
-- TOC entry 221 (class 1259 OID 16415)
-- Name: gold_foil_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gold_foil_type (
    id integer NOT NULL,
    project_id integer NOT NULL,
    flat_hot_stamping public.special_char_new,
    embossed_gold_stamping public.special_char_new,
    refractive_holographic_patterned_textured_hot_stamping public.special_char_new,
    post_hot_stamping_embossing_debossing public.special_char_new
);


ALTER TABLE public.gold_foil_type OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16418)
-- Name: gold_foil_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gold_foil_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.gold_foil_type_id_seq OWNER TO postgres;

--
-- TOC entry 5044 (class 0 OID 0)
-- Dependencies: 222
-- Name: gold_foil_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gold_foil_type_id_seq OWNED BY public.gold_foil_type.id;


--
-- TOC entry 223 (class 1259 OID 16419)
-- Name: leo_gp_numbers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.leo_gp_numbers (
    id integer NOT NULL,
    project_id integer NOT NULL,
    company_number character varying(255),
    gp_no character varying(255)
);


ALTER TABLE public.leo_gp_numbers OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16424)
-- Name: leo_gp_numbers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.leo_gp_numbers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.leo_gp_numbers_id_seq OWNER TO postgres;

--
-- TOC entry 5045 (class 0 OID 0)
-- Dependencies: 224
-- Name: leo_gp_numbers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.leo_gp_numbers_id_seq OWNED BY public.leo_gp_numbers.id;


--
-- TOC entry 225 (class 1259 OID 16425)
-- Name: matte_lamination; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matte_lamination (
    id integer NOT NULL,
    project_id integer NOT NULL,
    standard_furonghui_22d public.special_char_new,
    pre_coated_hy1206_65 public.special_char_new,
    high_tack_pre_coated_hy40 public.special_char_new,
    pre_coated_economical_high_wear_resistant_yt008a public.special_char_new,
    pre_coated_high_wear_resistant_tn008 public.special_char_new,
    pre_coated_standard_wear_resistant_kvmb_f public.special_char_new,
    soft_touch_matte_laminate_6015a public.special_char_new
);


ALTER TABLE public.matte_lamination OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16428)
-- Name: matte_lamination_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.matte_lamination_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.matte_lamination_id_seq OWNER TO postgres;

--
-- TOC entry 5046 (class 0 OID 0)
-- Dependencies: 226
-- Name: matte_lamination_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.matte_lamination_id_seq OWNED BY public.matte_lamination.id;


--
-- TOC entry 227 (class 1259 OID 16429)
-- Name: oil_and_uv_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oil_and_uv_types (
    id integer NOT NULL,
    project_id integer NOT NULL,
    oil_6812_glossy_3301_3440_dumb public.special_char_new,
    gv_led_uv_gloss_matte_oil public.special_char_new,
    oil_based_gloss_matte_on_powder_paper public.special_char_new,
    oil_based_gloss_matte_non_powder_paper public.special_char_new
);


ALTER TABLE public.oil_and_uv_types OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16432)
-- Name: oil_and_uv_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oil_and_uv_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oil_and_uv_types_id_seq OWNER TO postgres;

--
-- TOC entry 5047 (class 0 OID 0)
-- Dependencies: 228
-- Name: oil_and_uv_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oil_and_uv_types_id_seq OWNED BY public.oil_and_uv_types.id;


--
-- TOC entry 229 (class 1259 OID 16433)
-- Name: pattern; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pattern (
    id integer NOT NULL,
    project_id integer NOT NULL,
    gradient_halftone public.special_char_new,
    thin_lines_letters public.special_char_new,
    medium_small_thin_lines_letters public.special_char_new,
    medium_large_area_fine_lines_letters public.special_char_new,
    extra_large_area public.special_char_new
);


ALTER TABLE public.pattern OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 16436)
-- Name: pattern_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pattern_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pattern_id_seq OWNER TO postgres;

--
-- TOC entry 5048 (class 0 OID 0)
-- Dependencies: 230
-- Name: pattern_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pattern_id_seq OWNED BY public.pattern.id;


--
-- TOC entry 231 (class 1259 OID 16437)
-- Name: pricing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pricing (
    id integer NOT NULL,
    project_id integer NOT NULL,
    price numeric
);


ALTER TABLE public.pricing OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16442)
-- Name: pricing_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pricing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pricing_id_seq OWNER TO postgres;

--
-- TOC entry 5049 (class 0 OID 0)
-- Dependencies: 232
-- Name: pricing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pricing_id_seq OWNED BY public.pricing.id;


--
-- TOC entry 233 (class 1259 OID 16443)
-- Name: processing_after_ironing; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.processing_after_ironing (
    id integer NOT NULL,
    project_id integer NOT NULL,
    lamination public.special_char_new,
    uv_printing public.special_char_new,
    silk_screen_led_uv_sparkle_powder public.special_char_new,
    printing public.special_char_new
);


ALTER TABLE public.processing_after_ironing OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 16446)
-- Name: processing_after_ironing_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.processing_after_ironing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.processing_after_ironing_id_seq OWNER TO postgres;

--
-- TOC entry 5050 (class 0 OID 0)
-- Dependencies: 234
-- Name: processing_after_ironing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.processing_after_ironing_id_seq OWNED BY public.processing_after_ironing.id;


--
-- TOC entry 235 (class 1259 OID 16447)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    model_number character varying(255) NOT NULL,
    material_number character varying(255) NOT NULL,
    hot_stamping_paper_type character varying(255)
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 16452)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_id_seq OWNER TO postgres;

--
-- TOC entry 5051 (class 0 OID 0)
-- Dependencies: 236
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 237 (class 1259 OID 16453)
-- Name: specifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.specifications (
    id integer NOT NULL,
    project_id integer NOT NULL,
    color character varying(255),
    size character varying(255),
    tightness character varying(255),
    temperature_range character varying(255),
    performance text
);


ALTER TABLE public.specifications OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 16458)
-- Name: specifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.specifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.specifications_id_seq OWNER TO postgres;

--
-- TOC entry 5052 (class 0 OID 0)
-- Dependencies: 238
-- Name: specifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.specifications_id_seq OWNED BY public.specifications.id;


--
-- TOC entry 242 (class 1259 OID 16556)
-- Name: user_match_preferences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_match_preferences (
    id integer NOT NULL,
    user_id integer NOT NULL,
    field_name character varying(50) NOT NULL,
    field_value character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_match_preferences_field_name_check CHECK (((field_name)::text = ANY ((ARRAY['color'::character varying, 'size'::character varying, 'tightness'::character varying, 'temperature'::character varying, 'performance'::character varying])::text[])))
);


ALTER TABLE public.user_match_preferences OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 16555)
-- Name: user_match_preferences_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_match_preferences_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_match_preferences_id_seq OWNER TO postgres;

--
-- TOC entry 5053 (class 0 OID 0)
-- Dependencies: 241
-- Name: user_match_preferences_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_match_preferences_id_seq OWNED BY public.user_match_preferences.id;


--
-- TOC entry 239 (class 1259 OID 16459)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    role character varying(50) DEFAULT 'USER'::character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 16467)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 5054 (class 0 OID 0)
-- Dependencies: 240
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 4808 (class 2604 OID 16468)
-- Name: cloth_paper id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cloth_paper ALTER COLUMN id SET DEFAULT nextval('public.cloth_paper_id_seq'::regclass);


--
-- TOC entry 4809 (class 2604 OID 16469)
-- Name: film_butter_paper id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_butter_paper ALTER COLUMN id SET DEFAULT nextval('public.film_butter_paper_id_seq'::regclass);


--
-- TOC entry 4810 (class 2604 OID 16470)
-- Name: gold_foil_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gold_foil_type ALTER COLUMN id SET DEFAULT nextval('public.gold_foil_type_id_seq'::regclass);


--
-- TOC entry 4811 (class 2604 OID 16471)
-- Name: leo_gp_numbers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leo_gp_numbers ALTER COLUMN id SET DEFAULT nextval('public.leo_gp_numbers_id_seq'::regclass);


--
-- TOC entry 4812 (class 2604 OID 16472)
-- Name: matte_lamination id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matte_lamination ALTER COLUMN id SET DEFAULT nextval('public.matte_lamination_id_seq'::regclass);


--
-- TOC entry 4813 (class 2604 OID 16473)
-- Name: oil_and_uv_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oil_and_uv_types ALTER COLUMN id SET DEFAULT nextval('public.oil_and_uv_types_id_seq'::regclass);


--
-- TOC entry 4814 (class 2604 OID 16474)
-- Name: pattern id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pattern ALTER COLUMN id SET DEFAULT nextval('public.pattern_id_seq'::regclass);


--
-- TOC entry 4815 (class 2604 OID 16475)
-- Name: pricing id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pricing ALTER COLUMN id SET DEFAULT nextval('public.pricing_id_seq'::regclass);


--
-- TOC entry 4816 (class 2604 OID 16476)
-- Name: processing_after_ironing id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.processing_after_ironing ALTER COLUMN id SET DEFAULT nextval('public.processing_after_ironing_id_seq'::regclass);


--
-- TOC entry 4817 (class 2604 OID 16477)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 4818 (class 2604 OID 16478)
-- Name: specifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.specifications ALTER COLUMN id SET DEFAULT nextval('public.specifications_id_seq'::regclass);


--
-- TOC entry 4823 (class 2604 OID 16559)
-- Name: user_match_preferences id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_match_preferences ALTER COLUMN id SET DEFAULT nextval('public.user_match_preferences_id_seq'::regclass);


--
-- TOC entry 4819 (class 2604 OID 16479)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 5010 (class 0 OID 16407)
-- Dependencies: 217
-- Data for Name: cloth_paper; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cloth_paper (id, project_id, jht_8001, jht_8004, jht_8013, jht_8002, jht_8010, jht_8014, jht_8015, lts_8003, polyester_esm, jht_8003, jht_8017, jht_8006, jht_8008, jht_8011, jht_8016, jht_8018, jht_8019, jht_910, jht_8007, jht_8009, jht_8012, jht_0001_0103, jht_0104_0195, jht_0199_0209, jht_0211, jht_0213, jht_0196_0198, jht_0265_0311, jht_0216, jht_0218, jht_0312_0330, jht_0219_0264, jht_0334_0351, jht_0352_0386, jht_0387_0407, jht_0419_0429) FROM stdin;
1	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
2	2	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
3	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
4	4	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
5	5	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
6	6	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
7	7	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
8	8	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
9	9	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
10	10	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
11	11	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
12	12	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
13	13	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
14	14	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
15	15	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
16	16	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
17	17	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
18	18	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
19	19	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
20	20	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
21	21	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
22	22	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
23	23	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
24	24	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
25	25	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
26	26	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
27	27	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
28	28	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
29	29	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
30	30	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
31	31	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
32	32	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
33	33	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
34	34	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
35	35	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
36	36	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
37	37	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
38	38	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
39	39	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
40	40	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
41	41	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
42	42	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
43	43	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
44	44	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
45	45	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
46	46	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
47	47	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
48	48	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
49	49	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
50	50	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
51	51	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
52	52	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
53	53	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
54	54	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
55	55	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
56	56	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
57	57	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
58	58	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
59	59	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
60	60	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
61	61	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
62	62	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
63	63	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
64	64	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
65	65	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
66	66	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
67	67	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
68	68	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
69	69	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
70	70	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
71	71	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
72	72	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
73	73	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
74	74	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
75	75	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
76	76	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
77	77	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
78	78	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
79	79	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
80	80	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
81	81	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
82	82	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
83	83	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
84	84	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
85	85	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
86	86	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
87	87	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
88	88	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
89	89	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
90	90	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
91	91	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
92	92	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
93	93	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
94	94	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
95	95	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
96	96	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
97	97	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
98	98	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
99	99	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
100	100	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
101	101	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
102	102	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
103	103	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
104	104	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
105	105	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
106	106	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
107	107	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
108	108	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
109	109	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
110	110	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
111	111	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
112	112	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
113	113	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
114	114	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
115	115	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
116	116	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
117	117	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
118	118	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
119	119	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
120	120	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
121	121	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
122	122	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
123	123	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
124	124	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
125	125	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
126	126	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
127	127	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
128	128	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
129	129	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
130	130	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
131	131	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
132	132	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
133	133	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
134	134	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
135	135	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
136	136	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
137	137	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
138	138	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
139	139	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
140	140	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
141	141	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
142	142	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
143	143	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
144	144	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
145	145	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
146	146	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
147	147	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
148	148	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
149	149	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
150	150	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
151	151	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
152	152	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
153	153	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
154	154	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
155	155	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
156	156	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
157	157	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
158	158	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
159	159	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
160	160	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
161	161	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
162	162	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
163	163	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
164	164	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
165	165	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
166	166	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
167	167	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
168	168	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
169	169	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
170	170	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
171	171	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
172	172	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
173	173	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
174	174	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
175	175	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
176	176	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
177	177	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
178	178	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
179	179	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
180	180	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
181	181	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
182	182	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
183	183	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
184	184	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
185	185	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
186	186	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
187	187	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
188	188	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
189	189	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
190	190	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
191	191	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
192	192	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
193	193	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
194	194	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
195	195	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
196	196	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
197	197	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
198	198	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
199	199	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
200	200	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
201	201	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
202	202	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
203	203	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
204	204	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
205	205	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
206	206	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
207	207	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
208	208	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
209	209	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
210	210	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
211	211	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
212	212	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
213	213	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
214	214	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
215	215	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
216	216	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
217	217	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
218	218	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
219	219	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
220	220	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
221	221	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
222	222	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
223	223	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
224	224	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
225	225	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
226	226	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
227	227	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
228	228	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
229	229	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
230	230	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
231	231	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
232	232	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
233	233	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
234	234	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
235	235	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
236	236	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
237	237	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
238	238	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
239	239	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
240	240	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
241	241	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
242	242	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
243	243	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
244	244	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
245	245	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
246	246	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
247	247	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
248	248	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
249	249	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
250	250	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
251	251	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
252	252	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
253	253	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
254	254	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
255	255	V	V	V	V	V	V	V	V	V	V	V	V	▷	V	▷	▷	V	V	▷	V	V	V	V	V	X	X	X	X	▷	▷	▷	V	X	V	X	X
256	256	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
257	257	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
258	258	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
259	259	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
260	260	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
261	261	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
262	262	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
263	263	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
264	264	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
265	265	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
266	266	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
267	267	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
268	268	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X	X
269	269	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
270	270	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
271	271	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
272	272	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
273	273	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
274	274	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
275	275	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
276	276	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
277	277	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
278	278	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
279	279	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
280	280	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
281	281	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
282	282	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
283	283	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
284	284	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
285	285	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
286	286	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
287	287	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
288	288	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
289	289	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
290	290	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
291	291	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
292	292	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
293	293	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
294	294	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
295	295	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
296	296	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
297	297	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
298	298	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
299	299	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
300	300	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
301	301	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
302	302	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
303	303	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
304	304	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
305	305	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
306	306	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
307	307	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
308	308	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
309	309	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
310	310	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
311	311	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
312	312	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
313	313	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
314	314	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
315	315	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
316	316	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
317	317	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
318	318	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
319	319	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
320	320	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
321	321	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
322	322	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
323	323	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
324	324	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
325	325	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
326	326	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
327	327	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
328	328	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
329	329	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
330	330	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
331	331	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
332	332	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
333	333	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
334	334	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
335	335	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
336	336	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
337	337	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
338	338	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
339	339	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
340	340	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
341	341	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
342	342	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
343	343	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
344	344	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
345	345	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
346	346	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
347	347	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
348	348	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
349	349	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
350	350	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
351	351	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
352	352	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
353	353	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
354	354	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
355	355	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
356	356	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
357	357	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
358	358	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
359	359	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
360	360	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
361	361	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
362	362	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
363	363	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
364	364	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
365	365	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
366	366	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
367	367	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
368	368	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
369	369	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
370	370	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
371	371	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
372	372	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
373	373	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
374	374	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
375	375	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
376	376	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
377	377	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
378	378	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
379	379	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
380	380	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
381	381	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
382	382	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
383	383	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
384	384	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
385	385	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
386	386	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
387	387	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
388	388	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
389	389	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
390	390	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
391	391	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
392	392	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
393	393	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
394	394	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
395	395	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
396	396	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
397	397	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
398	398	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
399	399	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
400	400	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
401	401	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
402	402	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
403	403	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
404	404	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
405	405	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
406	406	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
407	407	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
408	408	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
409	409	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
410	410	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
411	411	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
412	412	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
413	413	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
414	414	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
415	415	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
416	416	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
417	417	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
418	418	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
419	419	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
420	420	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
421	421	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
422	422	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
423	423	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
424	424	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
425	425	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
426	426	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
427	427	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
428	428	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
429	429	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
430	430	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
431	431	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
432	432	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
433	433	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
434	434	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
435	435	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
436	436	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
437	437	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
438	438	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
439	439	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
440	440	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
441	441	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
442	442	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
443	443	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
444	444	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
445	445	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
446	446	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
447	447	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
448	448	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
449	449	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
450	450	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
451	451	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
452	452	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
453	453	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
454	454	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
455	455	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
456	456	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
457	457	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
458	458	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
459	459	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
460	460	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
461	461	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
462	462	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
463	463	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
464	464	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
465	465	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
466	466	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
467	467	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
468	468	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
469	469	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
470	470	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
471	471	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
472	472	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
473	473	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
474	474	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
475	475	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
476	476	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
477	477	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
478	478	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
479	479	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
480	480	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
481	481	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
482	482	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
483	483	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
484	484	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
485	485	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
486	486	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
487	487	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
488	488	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
489	489	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
490	490	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
491	491	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
492	492	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
493	493	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
494	494	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
495	495	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
496	496	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
497	497	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
498	498	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
499	499	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
500	500	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
501	501	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
502	502	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
503	503	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
504	504	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
505	505	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
506	506	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
507	507	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
508	508	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
509	509	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
510	510	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
511	511	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
512	512	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
513	513	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
514	514	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
515	515	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
516	516	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
517	517	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
518	518	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
519	519	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
520	520	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
521	521	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
522	522	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
523	523	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
524	524	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
525	525	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
526	526	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
527	527	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
528	528	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
529	529	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
530	530	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
531	531	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
532	532	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
533	533	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
534	534	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
535	535	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
536	536	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
537	537	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
538	538	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
539	539	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
540	540	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
541	541	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
542	542	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
543	543	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
544	544	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
545	545	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
546	546	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
547	547	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
548	548	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
549	549	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
550	550	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
551	551	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
552	552	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
553	553	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
554	554	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
555	555	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
556	556	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
557	557	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
558	558	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
559	559	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
560	560	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
561	561	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
562	562	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
563	563	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
564	564	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
565	565	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
566	566	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
567	567	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
568	568	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
569	569	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
570	570	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
571	571	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
572	572	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
573	573	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
574	574	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
575	575	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
576	576	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
577	577	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
578	578	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
579	579	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
580	580	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
581	581	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
582	582	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
583	583	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
584	584	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
585	585	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
586	586	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
587	587	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
588	588	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
589	589	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
590	590	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
591	591	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
592	592	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
593	593	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
594	594	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
595	595	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
596	596	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
597	597	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
598	598	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
599	599	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
600	600	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
601	601	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
602	602	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
603	603	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
604	604	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
605	605	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
606	606	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
607	607	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
608	608	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
609	609	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
610	610	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
611	611	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
612	612	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
613	613	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
614	614	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
615	615	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
616	616	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
617	617	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
618	618	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
619	619	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
620	620	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
621	621	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
622	622	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
623	623	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
624	624	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
625	625	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
626	626	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
627	627	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
628	628	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
629	629	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
630	630	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
631	631	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
632	632	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
633	633	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
634	634	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
635	635	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
636	636	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
637	637	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
638	638	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
639	639	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
640	640	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
641	641	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
642	642	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
643	643	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
644	644	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
645	645	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
646	646	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
647	647	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
648	648	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
649	649	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
650	650	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
651	651	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
652	652	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
653	653	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
654	654	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
655	655	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
656	656	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
657	657	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
658	658	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
659	659	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
660	660	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
661	661	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
662	662	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
663	663	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
664	664	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
665	665	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
666	666	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
667	667	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
668	668	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
669	669	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
670	670	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
671	671	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
672	672	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
673	673	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
674	674	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
675	675	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
676	676	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
677	677	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
678	678	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
679	679	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
680	680	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
681	681	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
682	682	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
683	683	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
684	684	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
685	685	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
686	686	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
687	687	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
688	688	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
689	689	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
690	690	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
691	691	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
692	692	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
693	693	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
694	694	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
695	695	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
696	696	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
697	697	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
698	698	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
699	699	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
700	700	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
701	701	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
702	702	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
703	703	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
704	704	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
705	705	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
706	706	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
707	707	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
708	708	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
709	709	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
710	710	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
711	711	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
712	712	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
713	713	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
714	714	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
715	715	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
716	716	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
717	717	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
718	718	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
719	719	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
720	720	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
721	721	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
722	722	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
723	723	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
724	724	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
725	725	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
726	726	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
727	727	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
728	728	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
729	729	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
730	730	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
731	731	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
732	732	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
733	733	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
734	734	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
735	735	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
736	736	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
737	737	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
738	738	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
739	739	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
740	740	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
741	741	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
742	742	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
743	743	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
744	744	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
745	745	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
746	746	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
747	747	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
748	748	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
749	749	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
750	750	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
751	751	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
752	752	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
753	753	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
754	754	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
755	755	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
756	756	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
757	757	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
758	758	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
759	759	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
760	760	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
761	761	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
762	762	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
763	763	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
764	764	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
765	765	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
766	766	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
767	767	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
768	768	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
769	769	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
770	770	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
771	771	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
772	772	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
773	773	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
774	774	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
775	775	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
776	776	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
777	777	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
778	778	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
779	779	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
780	780	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
781	781	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
782	782	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
783	783	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
784	784	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
785	785	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
786	786	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
787	787	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
788	788	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
789	789	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
790	790	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
791	791	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
792	792	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
793	793	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
794	794	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
795	795	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
796	796	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
797	797	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
798	798	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
799	799	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
800	800	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
801	801	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
802	802	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
803	803	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
804	804	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
805	805	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
806	806	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
807	807	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
808	808	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
809	809	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
810	810	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
811	811	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
812	812	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
813	813	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
814	814	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
815	815	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
816	816	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
817	817	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
818	818	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
819	819	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
820	820	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
821	821	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
822	822	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
823	823	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
824	824	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
825	825	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
826	826	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
827	827	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
828	828	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
829	829	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
830	830	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
831	831	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
832	832	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
833	833	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
834	834	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
835	835	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
836	836	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
837	837	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
838	838	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
839	839	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
840	840	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
841	841	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
842	842	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
843	843	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
844	844	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
845	845	V	▷	▷	▷	▷	▷	▷	▷	▷	V	V	V	▷	V	▷	▷	V	▷	▷	V	V	▷	V	V	X	X	X	X	X	X	X	V	X	▷	X	X
846	846	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
847	847	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
848	848	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
849	849	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
850	850	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
851	851	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
852	852	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
853	853	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
854	854	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
855	855	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
856	856	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
857	857	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
858	858	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
859	859	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
860	860	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
861	861	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
862	862	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
863	863	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
864	864	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
865	865	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
866	866	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
867	867	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
868	868	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
869	869	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
870	870	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
871	871	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
872	872	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
873	873	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
874	874	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
875	875	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
876	876	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
877	877	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
878	878	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
879	879	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
880	880	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
881	881	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
882	882	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
883	883	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
884	884	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
885	885	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
886	886	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
887	887	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
888	888	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
889	889	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
890	890	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
891	891	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
892	892	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
893	893	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
894	894	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
895	895	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
896	896	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
897	897	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
898	898	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
899	899	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
900	900	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
901	901	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
902	902	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
903	903	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
904	904	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
905	905	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
906	906	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
907	907	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
908	908	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
909	909	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
910	910	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
911	911	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
912	912	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
913	913	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
914	914	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
915	915	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
916	916	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
917	917	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
918	918	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
919	919	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
920	920	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
921	921	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
922	922	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
923	923	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
924	924	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
925	925	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
926	926	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
927	927	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
928	928	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
929	929	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
930	930	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
931	931	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
932	932	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
933	933	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
934	934	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
935	935	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
936	936	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
937	937	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
938	938	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
939	939	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
940	940	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
941	941	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
942	942	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
943	943	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
944	944	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
945	945	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
946	946	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
947	947	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
948	948	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
949	949	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
950	950	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
951	951	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
952	952	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
953	953	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
954	954	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
955	955	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
956	956	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
957	957	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
958	958	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
959	959	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
960	960	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
961	961	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
962	962	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
963	963	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
964	964	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
965	965	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
966	966	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
967	967	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
968	968	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
969	969	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
970	970	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
971	971	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
972	972	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
973	973	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
974	974	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
975	975	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
976	976	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
977	977	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
978	978	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
979	979	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
980	980	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
981	981	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
982	982	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
983	983	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
984	984	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
985	985	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
986	986	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
987	987	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
988	988	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
989	989	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
990	990	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
991	991	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
992	992	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
993	993	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
994	994	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
995	995	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
996	996	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
997	997	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
998	998	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
999	999	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1000	1000	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1001	1001	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1002	1002	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1003	1003	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1004	1004	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1005	1005	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1006	1006	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1007	1007	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1008	1008	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1009	1009	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1010	1010	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1011	1011	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1012	1012	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1013	1013	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1014	1014	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1015	1015	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1016	1016	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
1017	1017	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum	NA_Enum
\.


--
-- TOC entry 5012 (class 0 OID 16411)
-- Dependencies: 219
-- Data for Name: film_butter_paper; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.film_butter_paper (id, project_id, vet_vvc_avet, scratch_averse_avet_no_foil, butter_paper_gtf, butter_paper_ztf) FROM stdin;
1	1	\N	\N	\N	\N
2	2	\N	\N	\N	\N
3	3	\N	\N	\N	\N
4	4	\N	\N	\N	\N
5	5	\N	\N	\N	\N
6	6	\N	\N	\N	\N
7	7	\N	\N	\N	\N
8	8	\N	\N	\N	\N
9	9	\N	\N	\N	\N
10	10	\N	\N	\N	\N
11	11	\N	\N	\N	\N
12	12	\N	\N	\N	\N
13	13	\N	\N	\N	\N
14	14	\N	\N	\N	\N
15	15	\N	\N	\N	\N
16	16	\N	\N	\N	\N
17	17	\N	\N	\N	\N
18	18	\N	\N	\N	\N
19	19	\N	\N	\N	\N
20	20	\N	\N	\N	\N
21	21	\N	\N	\N	\N
22	22	\N	\N	\N	\N
23	23	\N	\N	\N	\N
24	24	\N	\N	\N	\N
25	25	\N	\N	\N	\N
26	26	\N	\N	\N	\N
27	27	\N	\N	\N	\N
28	28	\N	\N	\N	\N
29	29	\N	\N	\N	\N
30	30	\N	\N	\N	\N
31	31	\N	\N	\N	\N
32	32	\N	\N	\N	\N
33	33	\N	\N	\N	\N
34	34	\N	\N	\N	\N
35	35	\N	\N	\N	\N
36	36	\N	\N	\N	\N
37	37	\N	\N	\N	\N
38	38	\N	\N	\N	\N
39	39	\N	\N	\N	\N
40	40	\N	\N	\N	\N
41	41	\N	\N	\N	\N
42	42	\N	\N	\N	\N
43	43	\N	\N	\N	\N
44	44	\N	\N	\N	\N
45	45	\N	\N	\N	\N
46	46	\N	\N	\N	\N
47	47	\N	\N	\N	\N
48	48	\N	\N	\N	\N
49	49	\N	\N	\N	\N
50	50	\N	\N	\N	\N
51	51	\N	\N	\N	\N
52	52	\N	\N	\N	\N
53	53	\N	\N	\N	\N
54	54	\N	\N	\N	\N
55	55	\N	\N	\N	\N
56	56	\N	\N	\N	\N
57	57	\N	\N	\N	\N
58	58	\N	\N	\N	\N
59	59	\N	\N	\N	\N
60	60	\N	\N	\N	\N
61	61	X	X	X	X
62	62	X	X	X	X
63	63	\N	\N	\N	\N
64	64	\N	\N	\N	\N
65	65	\N	\N	\N	\N
66	66	\N	\N	\N	\N
67	67	X	X	X	X
68	68	X	X	X	X
69	69	X	X	X	X
70	70	X	X	X	X
71	71	X	X	X	X
72	72	X	X	X	X
73	73	X	X	X	X
74	74	X	X	X	X
75	75	X	X	X	X
76	76	X	X	X	X
77	77	X	X	X	X
78	78	X	X	X	X
79	79	X	X	X	X
80	80	X	X	X	X
81	81	X	X	X	X
82	82	X	X	X	X
83	83	X	X	X	X
84	84	X	X	X	X
85	85	X	X	X	X
86	86	X	X	X	X
87	87	X	X	X	X
88	88	X	X	X	X
89	89	X	X	X	X
90	90	X	X	X	X
91	91	X	X	X	X
92	92	X	X	X	X
93	93	X	X	X	X
94	94	X	X	X	X
95	95	X	X	X	X
96	96	X	X	X	X
97	97	X	X	X	X
98	98	X	X	X	X
99	99	X	X	X	X
100	100	X	X	X	X
101	101	X	X	X	X
102	102	X	X	X	X
103	103	X	X	X	X
104	104	X	X	X	X
105	105	X	X	X	X
106	106	X	X	X	X
107	107	X	X	X	X
108	108	X	X	X	X
109	109	X	X	X	X
110	110	X	X	X	X
111	111	X	X	X	X
112	112	X	X	X	X
113	113	X	X	X	X
114	114	X	X	X	X
115	115	X	X	X	X
116	116	X	X	X	X
117	117	X	X	X	X
118	118	X	X	X	X
119	119	X	X	X	X
120	120	X	X	X	X
121	121	X	X	X	X
122	122	X	X	X	X
123	123	X	X	X	X
124	124	\N	\N	\N	\N
125	125	\N	\N	\N	\N
126	126	\N	\N	\N	\N
127	127	\N	\N	\N	\N
128	128	\N	\N	\N	\N
129	129	\N	\N	\N	\N
130	130	\N	\N	\N	\N
131	131	\N	\N	\N	\N
132	132	\N	\N	\N	\N
133	133	\N	\N	\N	\N
134	134	\N	\N	\N	\N
135	135	\N	\N	\N	\N
136	136	\N	\N	\N	\N
137	137	\N	\N	\N	\N
138	138	\N	\N	\N	\N
139	139	\N	\N	\N	\N
140	140	\N	\N	\N	\N
141	141	\N	\N	\N	\N
142	142	\N	\N	\N	\N
143	143	\N	\N	\N	\N
144	144	\N	\N	\N	\N
145	145	\N	\N	\N	\N
146	146	\N	\N	\N	\N
147	147	\N	\N	\N	\N
148	148	\N	\N	\N	\N
149	149	\N	\N	\N	\N
150	150	\N	\N	\N	\N
151	151	\N	\N	\N	\N
152	152	\N	\N	\N	\N
153	153	X	X	X	X
154	154	X	X	X	X
155	155	X	X	X	X
156	156	X	X	X	X
157	157	X	X	X	X
158	158	X	X	X	X
159	159	X	X	X	X
160	160	X	X	X	X
161	161	X	X	X	X
162	162	X	X	X	X
163	163	X	X	X	X
164	164	X	X	X	X
165	165	X	X	X	X
166	166	X	X	X	X
167	167	X	X	X	X
168	168	X	X	X	X
169	169	X	X	X	X
170	170	X	X	X	X
171	171	X	X	X	X
172	172	X	X	X	X
173	173	X	X	X	X
174	174	X	X	X	X
175	175	X	X	X	X
176	176	\N	\N	\N	\N
177	177	\N	\N	\N	\N
178	178	\N	\N	\N	\N
179	179	\N	\N	\N	\N
180	180	X	X	X	X
181	181	X	X	X	X
182	182	X	X	X	X
183	183	X	X	X	X
184	184	X	X	X	X
185	185	X	X	X	X
186	186	X	X	X	X
187	187	X	X	X	X
188	188	X	X	X	X
189	189	X	X	X	X
190	190	X	X	X	X
191	191	X	X	X	X
192	192	X	X	X	X
193	193	X	X	X	X
194	194	X	X	X	X
195	195	X	X	X	X
196	196	X	X	X	X
197	197	X	X	X	X
198	198	X	X	X	X
199	199	X	X	X	X
200	200	X	X	X	X
201	201	X	X	X	X
202	202	X	X	X	X
203	203	X	X	X	X
204	204	X	X	X	X
205	205	X	X	X	X
206	206	X	X	X	X
207	207	X	X	X	X
208	208	X	X	X	X
209	209	X	X	X	X
210	210	X	X	X	X
211	211	X	X	X	X
212	212	X	X	X	X
213	213	X	X	X	X
214	214	X	X	X	X
215	215	X	X	X	X
216	216	X	X	X	X
217	217	X	X	X	X
218	218	X	X	X	X
219	219	X	X	X	X
220	220	X	X	X	X
221	221	X	X	X	X
222	222	X	X	X	X
223	223	X	X	X	X
224	224	X	X	X	X
225	225	\N	\N	\N	\N
226	226	\N	\N	\N	\N
227	227	\N	\N	\N	\N
228	228	\N	\N	\N	\N
229	229	\N	\N	\N	\N
230	230	\N	\N	\N	\N
231	231	\N	\N	\N	\N
232	232	\N	\N	\N	\N
233	233	\N	\N	\N	\N
234	234	\N	\N	\N	\N
235	235	\N	\N	\N	\N
236	236	\N	\N	\N	\N
237	237	\N	\N	\N	\N
238	238	\N	\N	\N	\N
239	239	\N	\N	\N	\N
240	240	\N	\N	\N	\N
241	241	\N	\N	\N	\N
242	242	\N	\N	\N	\N
243	243	\N	\N	\N	\N
244	244	X	X	X	X
245	245	X	X	X	X
246	246	X	X	X	X
247	247	X	X	X	X
248	248	X	X	X	X
249	249	X	X	X	X
250	250	X	X	X	X
251	251	X	X	X	X
252	252	X	X	X	X
253	253	X	X	X	X
254	254	X	X	X	X
255	255	X	X	X	X
256	256	X	X	X	X
257	257	X	X	X	X
258	258	X	X	X	X
259	259	X	X	X	X
260	260	X	X	X	X
261	261	X	X	X	X
262	262	X	X	X	X
263	263	X	X	X	X
264	264	X	X	X	X
265	265	X	X	X	X
266	266	X	X	X	X
267	267	X	X	X	X
268	268	X	X	X	X
269	269	X	X	X	X
270	270	X	X	X	X
271	271	\N	\N	\N	\N
272	272	\N	\N	\N	\N
273	273	\N	\N	\N	\N
274	274	\N	\N	\N	\N
275	275	\N	\N	\N	\N
276	276	\N	\N	\N	\N
277	277	\N	\N	\N	\N
278	278	\N	\N	\N	\N
279	279	\N	\N	\N	\N
280	280	\N	\N	\N	\N
281	281	\N	\N	\N	\N
282	282	\N	\N	\N	\N
283	283	\N	\N	\N	\N
284	284	\N	\N	\N	\N
285	285	X	X	X	X
286	286	X	X	X	X
287	287	X	X	X	X
288	288	X	X	X	X
289	289	X	X	X	X
290	290	X	X	X	X
291	291	X	X	X	X
292	292	X	X	X	X
293	293	X	X	X	X
294	294	X	X	X	X
295	295	X	X	X	X
296	296	X	X	X	X
297	297	X	X	X	X
298	298	X	X	X	X
299	299	X	X	X	X
300	300	X	X	X	X
301	301	X	X	X	X
302	302	X	X	X	X
303	303	X	X	X	X
304	304	X	X	X	X
305	305	X	X	X	X
306	306	X	X	X	X
307	307	X	X	X	X
308	308	X	X	X	X
309	309	X	X	X	X
310	310	X	X	X	X
311	311	X	X	X	X
312	312	X	X	X	X
313	313	X	X	X	X
314	314	X	X	X	X
315	315	X	X	X	X
316	316	X	X	X	X
317	317	X	X	X	X
318	318	X	X	X	X
319	319	X	X	X	X
320	320	X	X	X	X
321	321	X	X	X	X
322	322	X	X	X	X
323	323	X	X	X	X
324	324	X	X	X	X
325	325	X	X	X	X
326	326	X	X	X	X
327	327	X	X	X	X
328	328	X	X	X	X
329	329	X	X	X	X
330	330	X	X	X	X
331	331	X	X	X	X
332	332	X	X	X	X
333	333	X	X	X	X
334	334	X	X	X	X
335	335	X	X	X	X
336	336	X	X	X	X
337	337	X	X	X	X
338	338	X	X	X	X
339	339	X	X	X	X
340	340	X	X	X	X
341	341	X	X	X	X
342	342	X	X	X	X
343	343	X	X	X	X
344	344	X	X	X	X
345	345	X	X	X	X
346	346	X	X	X	X
347	347	X	X	X	X
348	348	X	X	X	X
349	349	X	X	X	X
350	350	X	X	X	X
351	351	X	X	X	X
352	352	X	X	X	X
353	353	X	X	X	X
354	354	X	X	X	X
355	355	X	X	X	X
356	356	X	X	X	X
357	357	X	X	X	X
358	358	X	X	X	X
359	359	X	X	X	X
360	360	X	X	X	X
361	361	X	X	X	X
362	362	X	X	X	X
363	363	X	X	X	X
364	364	X	X	X	X
365	365	X	X	X	X
366	366	X	X	X	X
367	367	X	X	X	X
368	368	X	X	X	X
369	369	X	X	X	X
370	370	X	X	X	X
371	371	X	X	X	X
372	372	X	X	X	X
373	373	X	X	X	X
374	374	X	X	X	X
375	375	X	X	X	X
376	376	X	X	X	X
377	377	X	X	X	X
378	378	X	X	X	X
379	379	X	X	X	X
380	380	X	X	X	X
381	381	X	X	X	X
382	382	X	X	X	X
383	383	X	X	X	X
384	384	X	X	X	X
385	385	X	X	X	X
386	386	X	X	X	X
387	387	X	X	X	X
388	388	X	X	X	X
389	389	X	X	X	X
390	390	X	X	X	X
391	391	X	X	X	X
392	392	X	X	X	X
393	393	X	X	X	X
394	394	X	X	X	X
395	395	X	X	X	X
396	396	X	X	X	X
397	397	X	X	X	X
398	398	X	X	X	X
399	399	X	X	X	X
400	400	X	X	X	X
401	401	\N	\N	\N	\N
402	402	\N	\N	\N	\N
403	403	\N	\N	\N	\N
404	404	\N	\N	\N	\N
405	405	X	X	X	X
406	406	X	X	X	X
407	407	X	X	X	X
408	408	X	X	X	X
409	409	X	X	X	X
410	410	X	X	X	X
411	411	X	X	X	X
412	412	X	X	X	X
413	413	X	X	X	X
414	414	X	X	X	X
415	415	X	X	X	X
416	416	X	X	X	X
417	417	X	X	X	X
418	418	X	X	X	X
419	419	X	X	X	X
420	420	X	X	X	X
421	421	X	X	X	X
422	422	X	X	X	X
423	423	X	X	X	X
424	424	X	X	X	X
425	425	X	X	X	X
426	426	X	X	X	X
427	427	X	X	X	X
428	428	X	X	X	X
429	429	X	X	X	X
430	430	X	X	X	X
431	431	X	X	X	X
432	432	X	X	X	X
433	433	X	X	X	X
434	434	X	X	X	X
435	435	X	X	X	X
436	436	X	X	X	X
437	437	X	X	X	X
438	438	X	X	X	X
439	439	X	X	X	X
440	440	X	X	X	X
441	441	X	X	X	X
442	442	X	X	X	X
443	443	X	X	X	X
444	444	X	X	X	X
445	445	X	X	X	X
446	446	X	X	X	X
447	447	X	X	X	X
448	448	X	X	X	X
449	449	X	X	X	X
450	450	X	X	X	X
451	451	X	X	X	X
452	452	X	X	X	X
453	453	X	X	X	X
454	454	X	X	X	X
455	455	X	X	X	X
456	456	X	X	X	X
457	457	X	X	X	X
458	458	X	X	X	X
459	459	X	X	X	X
460	460	X	X	X	X
461	461	\N	\N	\N	\N
462	462	X	X	X	X
463	463	X	X	X	X
464	464	X	X	X	X
465	465	X	X	X	X
466	466	X	X	X	X
467	467	X	X	X	X
468	468	X	X	X	X
469	469	X	X	X	X
470	470	X	X	X	X
471	471	X	X	X	X
472	472	X	X	X	X
473	473	X	X	X	X
474	474	X	X	X	X
475	475	X	X	X	X
476	476	X	X	X	X
477	477	X	X	X	X
478	478	X	X	X	X
479	479	X	X	X	X
480	480	X	X	X	X
481	481	X	X	X	X
482	482	X	X	X	X
483	483	X	X	X	X
484	484	X	X	X	X
485	485	X	X	X	X
486	486	X	X	X	X
487	487	X	X	X	X
488	488	X	X	X	X
489	489	X	X	X	X
490	490	X	X	X	X
491	491	X	X	X	X
492	492	X	X	X	X
493	493	X	X	X	X
494	494	X	X	X	X
495	495	X	X	X	X
496	496	X	X	X	X
497	497	X	X	X	X
498	498	X	X	X	X
499	499	X	X	X	X
500	500	X	X	X	X
501	501	X	X	X	X
502	502	X	X	X	X
503	503	X	X	X	X
504	504	X	X	X	X
505	505	X	X	X	X
506	506	X	X	X	X
507	507	X	X	X	X
508	508	X	X	X	X
509	509	X	X	X	X
510	510	X	X	X	X
511	511	X	X	X	X
512	512	X	X	X	X
513	513	X	X	X	X
514	514	X	X	X	X
515	515	X	X	X	X
516	516	X	X	X	X
517	517	X	X	X	X
518	518	X	X	X	X
519	519	X	X	X	X
520	520	X	X	X	X
521	521	X	X	X	X
522	522	X	X	X	X
523	523	X	X	X	X
524	524	X	X	X	X
525	525	X	X	X	X
526	526	X	X	X	X
527	527	X	X	X	X
528	528	X	X	X	X
529	529	X	X	X	X
530	530	X	X	X	X
531	531	X	X	X	X
532	532	X	X	X	X
533	533	X	X	X	X
534	534	X	X	X	X
535	535	X	X	X	X
536	536	X	X	X	X
537	537	X	X	X	X
538	538	X	X	X	X
539	539	X	X	X	X
540	540	X	X	X	X
541	541	X	X	X	X
542	542	X	X	X	X
543	543	X	X	X	X
544	544	X	X	X	X
545	545	X	X	X	X
546	546	X	X	X	X
547	547	X	X	X	X
548	548	X	X	X	X
549	549	X	X	X	X
550	550	X	X	X	X
551	551	X	X	X	X
552	552	X	X	X	X
553	553	X	X	X	X
554	554	X	X	X	X
555	555	X	X	X	X
556	556	X	X	X	X
557	557	X	X	X	X
558	558	X	X	X	X
559	559	X	X	X	X
560	560	X	X	X	X
561	561	X	X	X	X
562	562	X	X	X	X
563	563	X	X	X	X
564	564	X	X	X	X
565	565	X	X	X	X
566	566	X	X	X	X
567	567	X	X	X	X
568	568	X	X	X	X
569	569	X	X	X	X
570	570	X	X	X	X
571	571	X	X	X	X
572	572	X	X	X	X
573	573	X	X	X	X
574	574	X	X	X	X
575	575	X	X	X	X
576	576	X	X	X	X
577	577	X	X	X	X
578	578	X	X	X	X
579	579	X	X	X	X
580	580	X	X	X	X
581	581	X	X	X	X
582	582	X	X	X	X
583	583	X	X	X	X
584	584	X	X	X	X
585	585	X	X	X	X
586	586	X	X	X	X
587	587	X	X	X	X
588	588	X	X	X	X
589	589	X	X	X	X
590	590	X	X	X	X
591	591	X	X	X	X
592	592	X	X	X	X
593	593	X	X	X	X
594	594	X	X	X	X
595	595	X	X	X	X
596	596	X	X	X	X
597	597	X	X	X	X
598	598	X	X	X	X
599	599	X	X	X	X
600	600	X	X	X	X
601	601	X	X	X	X
602	602	X	X	X	X
603	603	X	X	X	X
604	604	X	X	X	X
605	605	X	X	X	X
606	606	X	X	X	X
607	607	X	X	X	X
608	608	X	X	X	X
609	609	X	X	X	X
610	610	X	X	X	X
611	611	X	X	X	X
612	612	X	X	X	X
613	613	X	X	X	X
614	614	X	X	X	X
615	615	X	X	X	X
616	616	X	X	X	X
617	617	X	X	X	X
618	618	X	X	X	X
619	619	X	X	X	X
620	620	X	X	X	X
621	621	X	X	X	X
622	622	X	X	X	X
623	623	X	X	X	X
624	624	X	X	X	X
625	625	X	X	X	X
626	626	X	X	X	X
627	627	X	X	X	X
628	628	X	X	X	X
629	629	X	X	X	X
630	630	X	X	X	X
631	631	X	X	X	X
632	632	X	X	X	X
633	633	X	X	X	X
634	634	X	X	X	X
635	635	X	X	X	X
636	636	X	X	X	X
637	637	X	X	X	X
638	638	X	X	X	X
639	639	X	X	X	X
640	640	X	X	X	X
641	641	X	X	X	X
642	642	X	X	X	X
643	643	X	X	X	X
644	644	X	X	X	X
645	645	X	X	X	X
646	646	X	X	X	X
647	647	\N	\N	\N	\N
648	648	\N	\N	\N	\N
649	649	\N	\N	\N	\N
650	650	\N	\N	\N	\N
651	651	\N	\N	\N	\N
652	652	\N	\N	\N	\N
653	653	\N	\N	\N	\N
654	654	\N	\N	\N	\N
655	655	\N	\N	\N	\N
656	656	\N	\N	\N	\N
657	657	\N	\N	\N	\N
658	658	\N	\N	\N	\N
659	659	\N	\N	\N	\N
660	660	\N	\N	\N	\N
661	661	V	V	V	V
662	662	V	V	V	V
663	663	V	V	V	V
664	664	V	V	V	V
665	665	V	V	V	V
666	666	V	V	V	V
667	667	V	V	V	V
668	668	V	V	V	V
669	669	V	V	V	V
670	670	V	V	V	V
671	671	V	V	V	V
672	672	V	V	V	V
673	673	V	V	V	V
674	674	V	V	V	V
675	675	V	V	V	V
676	676	V	V	V	V
677	677	V	V	V	V
678	678	V	V	V	V
679	679	V	V	V	V
680	680	V	V	V	V
681	681	V	V	V	V
682	682	V	V	V	V
683	683	V	V	V	V
684	684	V	V	V	V
685	685	V	V	V	V
686	686	V	V	V	V
687	687	V	V	V	V
688	688	V	V	V	V
689	689	V	V	V	V
690	690	V	V	V	V
691	691	V	V	V	V
692	692	V	V	V	V
693	693	V	V	V	V
694	694	V	V	V	V
695	695	V	V	V	V
696	696	V	V	V	V
697	697	V	V	X	X
698	698	V	V	X	X
699	699	V	V	X	X
700	700	V	V	X	X
701	701	V	V	X	X
702	702	V	V	X	X
703	703	V	V	X	X
704	704	V	V	X	X
705	705	V	V	X	X
706	706	V	V	X	X
707	707	V	V	X	X
708	708	V	V	X	X
709	709	V	V	X	X
710	710	V	V	X	X
711	711	V	V	X	X
712	712	V	V	X	X
713	713	V	V	X	X
714	714	V	V	X	X
715	715	V	V	X	X
716	716	V	V	X	X
717	717	V	V	X	X
718	718	V	V	X	X
719	719	V	V	X	X
720	720	V	V	X	X
721	721	V	V	X	X
722	722	V	V	X	X
723	723	V	V	X	X
724	724	V	V	X	X
725	725	V	V	X	X
726	726	V	V	X	X
727	727	V	V	X	X
728	728	V	V	X	X
729	729	V	V	X	X
730	730	V	V	X	X
731	731	V	V	X	X
732	732	V	V	X	X
733	733	V	V	X	X
734	734	V	V	X	X
735	735	V	V	X	X
736	736	V	V	X	X
737	737	V	V	X	X
738	738	V	V	X	X
739	739	V	V	X	X
740	740	V	V	X	X
741	741	V	V	X	X
742	742	V	V	X	X
743	743	V	V	X	X
744	744	V	V	X	X
745	745	V	V	X	X
746	746	V	V	X	X
747	747	V	V	X	X
748	748	V	V	X	X
749	749	V	V	X	X
750	750	V	V	X	X
751	751	V	V	X	X
752	752	V	V	X	X
753	753	V	V	X	X
754	754	V	V	X	X
755	755	V	V	X	X
756	756	V	V	X	X
757	757	V	V	X	X
758	758	V	V	X	X
759	759	V	V	X	X
760	760	V	V	X	X
761	761	V	V	X	X
762	762	V	V	X	X
763	763	V	V	X	X
764	764	V	V	X	X
765	765	V	V	X	X
766	766	V	V	X	X
767	767	V	V	X	X
768	768	V	V	X	X
769	769	V	V	X	X
770	770	V	V	X	X
771	771	V	V	X	X
772	772	V	V	X	X
773	773	V	V	X	X
774	774	V	V	X	X
775	775	V	V	X	X
776	776	V	V	X	X
777	777	V	V	X	X
778	778	V	V	X	X
779	779	V	V	X	X
780	780	V	V	X	X
781	781	V	V	X	X
782	782	V	V	X	X
783	783	V	V	X	X
784	784	V	V	X	X
785	785	V	V	X	X
786	786	V	V	X	X
787	787	V	V	X	X
788	788	V	V	X	X
789	789	V	V	X	X
790	790	V	V	X	X
791	791	V	V	X	X
792	792	V	V	X	X
793	793	V	V	X	X
794	794	V	V	X	X
795	795	V	V	X	X
796	796	V	V	X	X
797	797	V	V	X	X
798	798	V	V	X	X
799	799	X	X	X	X
800	800	X	X	X	X
801	801	X	X	X	X
802	802	X	X	X	X
803	803	X	X	X	X
804	804	X	X	X	X
805	805	X	X	X	X
806	806	X	X	X	X
807	807	X	X	X	X
808	808	X	X	X	X
809	809	X	X	X	X
810	810	X	X	X	X
811	811	X	X	X	X
812	812	X	X	X	X
813	813	X	X	X	X
814	814	X	X	X	X
815	815	X	X	X	X
816	816	X	X	X	X
817	817	X	X	X	X
818	818	X	X	X	X
819	819	X	X	X	X
820	820	X	X	X	X
821	821	X	X	X	X
822	822	X	X	X	X
823	823	X	X	X	X
824	824	X	X	X	X
825	825	X	X	X	X
826	826	X	X	X	X
827	827	X	X	X	X
828	828	X	X	X	X
829	829	X	X	X	X
830	830	X	X	X	X
831	831	X	X	X	X
832	832	X	X	X	X
833	833	X	X	X	X
834	834	X	X	X	X
835	835	X	X	X	X
836	836	X	X	X	X
837	837	X	X	X	X
838	838	X	X	X	X
839	839	X	X	X	X
840	840	X	X	X	X
841	841	X	X	X	X
842	842	X	X	X	X
843	843	X	X	X	X
844	844	X	X	X	X
845	845	X	X	X	X
846	846	X	X	X	X
847	847	X	X	X	X
848	848	X	X	X	X
849	849	X	X	X	X
850	850	X	X	X	X
851	851	X	X	X	X
852	852	X	X	X	X
853	853	X	X	X	X
854	854	X	X	X	X
855	855	X	X	X	X
856	856	X	X	X	X
857	857	X	X	X	X
858	858	X	X	X	X
859	859	X	X	X	X
860	860	V	V	X	X
861	861	V	V	X	X
862	862	V	V	X	X
863	863	V	V	X	X
864	864	V	V	X	X
865	865	V	V	X	X
866	866	V	V	X	X
867	867	V	V	X	X
868	868	V	V	X	X
869	869	V	V	X	X
870	870	V	V	X	X
871	871	V	V	X	X
872	872	V	V	X	X
873	873	V	V	X	X
874	874	V	V	X	X
875	875	V	V	X	X
876	876	V	V	X	X
877	877	V	V	X	X
878	878	V	V	X	X
879	879	V	V	X	X
880	880	V	V	X	X
881	881	V	V	X	X
882	882	V	V	X	X
883	883	V	V	X	X
884	884	V	V	X	X
885	885	V	V	X	X
886	886	V	V	X	X
887	887	V	V	X	X
888	888	V	V	X	X
889	889	V	V	X	X
890	890	V	V	X	X
891	891	V	V	X	X
892	892	V	V	X	X
893	893	V	V	X	X
894	894	V	V	X	X
895	895	V	V	X	X
896	896	V	V	X	X
897	897	V	V	X	X
898	898	V	V	X	X
899	899	V	V	X	X
900	900	V	V	X	X
901	901	X	X	X	X
902	902	X	X	X	X
903	903	X	X	X	X
904	904	X	X	X	X
905	905	X	X	X	X
906	906	X	X	X	X
907	907	X	X	X	X
908	908	X	X	X	X
909	909	X	X	X	X
910	910	X	X	X	X
911	911	X	X	X	X
912	912	X	X	X	X
913	913	X	X	X	X
914	914	X	X	X	X
915	915	X	X	X	X
916	916	X	X	X	X
917	917	X	X	X	X
918	918	X	X	X	X
919	919	X	X	X	X
920	920	X	X	X	X
921	921	X	X	X	X
922	922	X	X	X	X
923	923	X	X	X	X
924	924	X	X	X	X
925	925	X	X	X	X
926	926	X	X	X	X
927	927	X	X	X	X
928	928	X	X	X	X
929	929	X	X	X	X
930	930	X	X	X	X
931	931	X	X	X	X
932	932	\N	\N	\N	\N
933	933	\N	\N	\N	\N
934	934	\N	\N	\N	\N
935	935	\N	\N	\N	\N
936	936	\N	\N	\N	\N
937	937	\N	\N	\N	\N
938	938	\N	\N	\N	\N
939	939	\N	\N	\N	\N
940	940	\N	\N	\N	\N
941	941	\N	\N	\N	\N
942	942	\N	\N	\N	\N
943	943	\N	\N	\N	\N
944	944	\N	\N	\N	\N
945	945	\N	\N	\N	\N
946	946	\N	\N	\N	\N
947	947	\N	\N	\N	\N
948	948	\N	\N	\N	\N
949	949	\N	\N	\N	\N
950	950	\N	\N	\N	\N
951	951	\N	\N	\N	\N
952	952	\N	\N	\N	\N
953	953	X	X	X	X
954	954	X	X	X	X
955	955	X	X	X	X
956	956	X	X	X	X
957	957	X	X	X	X
958	958	X	X	X	X
959	959	X	X	X	X
960	960	X	X	X	X
961	961	X	X	X	X
962	962	X	X	X	X
963	963	X	X	X	X
964	964	X	X	X	X
965	965	X	X	X	X
966	966	X	X	X	X
967	967	X	X	X	X
968	968	X	X	X	X
969	969	X	X	X	X
970	970	X	X	X	X
971	971	V	V	V	V
972	972	V	V	V	V
973	973	V	V	V	V
974	974	X	X	X	X
975	975	X	X	X	X
976	976	\N	\N	\N	\N
977	977	\N	\N	\N	\N
978	978	\N	\N	\N	\N
979	979	X	X	X	X
980	980	X	X	X	X
981	981	X	X	X	X
982	982	X	X	X	X
983	983	X	X	X	X
984	984	X	X	X	X
985	985	X	X	X	X
986	986	X	X	X	X
987	987	X	X	X	X
988	988	X	X	X	X
989	989	X	X	X	X
990	990	X	X	X	X
991	991	X	X	X	X
992	992	X	X	X	X
993	993	X	X	X	X
994	994	X	X	X	X
995	995	X	X	X	X
996	996	X	X	X	X
997	997	X	X	X	X
998	998	X	X	X	X
999	999	X	X	X	X
1000	1000	X	X	X	X
1001	1001	X	X	X	X
1002	1002	X	X	X	X
1003	1003	X	X	X	X
1004	1004	X	X	X	X
1005	1005	X	X	X	X
1006	1006	X	X	X	X
1007	1007	X	X	X	X
1008	1008	X	X	X	X
1009	1009	X	X	X	X
1010	1010	X	X	X	X
1011	1011	X	X	X	X
1012	1012	X	X	X	X
1013	1013	X	X	X	X
1014	1014	X	X	X	X
1015	1015	X	X	X	X
1016	1016	X	X	X	X
1017	1017	X	X	X	X
\.


--
-- TOC entry 5014 (class 0 OID 16415)
-- Dependencies: 221
-- Data for Name: gold_foil_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.gold_foil_type (id, project_id, flat_hot_stamping, embossed_gold_stamping, refractive_holographic_patterned_textured_hot_stamping, post_hot_stamping_embossing_debossing) FROM stdin;
1	1	\N	\N	\N	\N
2	2	\N	\N	\N	\N
3	3	\N	\N	\N	\N
4	4	\N	\N	\N	\N
5	5	\N	\N	\N	\N
6	6	\N	\N	\N	\N
7	7	\N	\N	\N	\N
8	8	\N	\N	\N	\N
9	9	\N	\N	\N	\N
10	10	\N	\N	\N	\N
11	11	\N	\N	\N	\N
12	12	\N	\N	\N	\N
13	13	\N	\N	\N	\N
14	14	\N	\N	\N	\N
15	15	\N	\N	\N	\N
16	16	\N	\N	\N	\N
17	17	\N	\N	\N	\N
18	18	\N	\N	\N	\N
19	19	\N	\N	\N	\N
20	20	\N	\N	\N	\N
21	21	\N	\N	\N	\N
22	22	\N	\N	\N	\N
23	23	\N	\N	\N	\N
24	24	\N	\N	\N	\N
25	25	\N	\N	\N	\N
26	26	\N	\N	\N	\N
27	27	\N	\N	\N	\N
28	28	\N	\N	\N	\N
29	29	\N	\N	\N	\N
30	30	\N	\N	\N	\N
31	31	\N	\N	\N	\N
32	32	\N	\N	\N	\N
33	33	\N	\N	\N	\N
34	34	\N	\N	\N	\N
35	35	\N	\N	\N	\N
36	36	\N	\N	\N	\N
37	37	\N	\N	\N	\N
38	38	\N	\N	\N	\N
39	39	\N	\N	\N	\N
40	40	\N	\N	\N	\N
41	41	\N	\N	\N	\N
42	42	\N	\N	\N	\N
43	43	\N	\N	\N	\N
44	44	\N	\N	\N	\N
45	45	\N	\N	\N	\N
46	46	\N	\N	\N	\N
47	47	\N	\N	\N	\N
48	48	\N	\N	\N	\N
49	49	\N	\N	\N	\N
50	50	\N	\N	\N	\N
51	51	\N	\N	\N	\N
52	52	\N	\N	\N	\N
53	53	\N	\N	\N	\N
54	54	\N	\N	\N	\N
55	55	\N	\N	\N	\N
56	56	\N	\N	\N	\N
57	57	\N	\N	\N	\N
58	58	\N	\N	\N	\N
59	59	\N	\N	\N	\N
60	60	\N	\N	\N	\N
61	61	V	X	X	X
62	62	V	X	X	X
63	63	\N	\N	\N	\N
64	64	\N	\N	\N	\N
65	65	\N	\N	\N	\N
66	66	\N	\N	\N	\N
67	67	V	X	X	X
68	68	V	X	X	X
69	69	V	X	X	X
70	70	V	X	X	X
71	71	V	X	X	X
72	72	V	X	X	X
73	73	V	X	X	X
74	74	V	X	X	X
75	75	V	X	X	X
76	76	V	X	X	X
77	77	V	X	X	X
78	78	V	X	X	X
79	79	V	X	X	X
80	80	V	X	X	X
81	81	V	X	X	X
82	82	V	X	X	X
83	83	V	X	X	X
84	84	V	X	X	X
85	85	V	X	X	X
86	86	V	X	X	X
87	87	V	X	X	X
88	88	V	X	X	X
89	89	V	X	X	X
90	90	V	X	X	X
91	91	V	X	X	X
92	92	V	X	X	X
93	93	V	X	X	X
94	94	V	X	X	X
95	95	V	X	X	X
96	96	V	X	X	X
97	97	V	X	X	X
98	98	V	X	X	X
99	99	V	X	X	X
100	100	V	X	X	X
101	101	V	X	X	X
102	102	V	X	X	X
103	103	V	X	X	X
104	104	V	X	X	X
105	105	V	NA_Enum	NA_Enum	NA_Enum
106	106	V	NA_Enum	NA_Enum	NA_Enum
107	107	V	NA_Enum	NA_Enum	NA_Enum
108	108	V	NA_Enum	NA_Enum	NA_Enum
109	109	V	NA_Enum	NA_Enum	NA_Enum
110	110	V	NA_Enum	NA_Enum	NA_Enum
111	111	V	NA_Enum	NA_Enum	NA_Enum
112	112	V	NA_Enum	NA_Enum	NA_Enum
113	113	V	NA_Enum	NA_Enum	NA_Enum
114	114	V	NA_Enum	NA_Enum	NA_Enum
115	115	V	NA_Enum	NA_Enum	NA_Enum
116	116	V	NA_Enum	NA_Enum	NA_Enum
117	117	V	NA_Enum	NA_Enum	NA_Enum
118	118	V	NA_Enum	NA_Enum	NA_Enum
119	119	V	NA_Enum	NA_Enum	NA_Enum
120	120	V	NA_Enum	NA_Enum	NA_Enum
121	121	V	NA_Enum	NA_Enum	NA_Enum
122	122	V	NA_Enum	NA_Enum	NA_Enum
123	123	V	NA_Enum	NA_Enum	NA_Enum
124	124	\N	\N	\N	\N
125	125	\N	\N	\N	\N
126	126	\N	\N	\N	\N
127	127	\N	\N	\N	\N
128	128	\N	\N	\N	\N
129	129	\N	\N	\N	\N
130	130	\N	\N	\N	\N
131	131	\N	\N	\N	\N
132	132	\N	\N	\N	\N
133	133	\N	\N	\N	\N
134	134	\N	\N	\N	\N
135	135	\N	\N	\N	\N
136	136	\N	\N	\N	\N
137	137	\N	\N	\N	\N
138	138	\N	\N	\N	\N
139	139	\N	\N	\N	\N
140	140	\N	\N	\N	\N
141	141	\N	\N	\N	\N
142	142	\N	\N	\N	\N
143	143	\N	\N	\N	\N
144	144	\N	\N	\N	\N
145	145	\N	\N	\N	\N
146	146	\N	\N	\N	\N
147	147	\N	\N	\N	\N
148	148	\N	\N	\N	\N
149	149	\N	\N	\N	\N
150	150	\N	\N	\N	\N
151	151	\N	\N	\N	\N
152	152	\N	\N	\N	\N
153	153	V	V	V	V
154	154	V	V	V	V
155	155	V	V	V	V
156	156	V	V	V	V
157	157	V	V	V	V
158	158	V	V	V	V
159	159	V	V	V	V
160	160	V	V	V	V
161	161	V	V	V	V
162	162	V	V	V	V
163	163	V	V	V	V
164	164	V	V	V	V
165	165	V	V	V	V
166	166	V	V	V	V
167	167	V	V	V	V
168	168	V	V	V	V
169	169	V	V	V	V
170	170	V	V	V	V
171	171	V	V	V	V
172	172	V	V	V	V
173	173	V	V	V	V
174	174	V	V	V	V
175	175	V	V	V	V
176	176	\N	\N	\N	\N
177	177	\N	\N	\N	\N
178	178	\N	\N	\N	\N
179	179	\N	\N	\N	\N
180	180	V	X	X	X
181	181	V	X	X	X
182	182	V	X	X	X
183	183	V	X	X	X
184	184	V	X	X	X
185	185	V	X	X	X
186	186	V	X	X	X
187	187	V	X	X	X
188	188	V	X	X	X
189	189	V	X	X	X
190	190	V	X	X	X
191	191	V	X	X	X
192	192	V	X	X	X
193	193	V	X	X	X
194	194	V	X	X	X
195	195	V	X	X	X
196	196	V	X	X	X
197	197	V	X	X	X
198	198	V	X	X	X
199	199	V	X	X	X
200	200	V	X	X	X
201	201	V	X	X	X
202	202	V	X	X	X
203	203	V	X	X	X
204	204	V	X	X	X
205	205	V	X	X	X
206	206	V	X	X	X
207	207	V	X	X	X
208	208	V	X	X	X
209	209	V	X	X	X
210	210	V	X	X	X
211	211	V	X	X	X
212	212	V	X	X	X
213	213	V	X	X	X
214	214	V	X	X	X
215	215	V	X	X	X
216	216	V	NA_Enum	NA_Enum	NA_Enum
217	217	V	NA_Enum	NA_Enum	NA_Enum
218	218	V	NA_Enum	NA_Enum	NA_Enum
219	219	V	NA_Enum	NA_Enum	NA_Enum
220	220	V	NA_Enum	NA_Enum	NA_Enum
221	221	V	NA_Enum	NA_Enum	NA_Enum
222	222	V	NA_Enum	NA_Enum	NA_Enum
223	223	V	NA_Enum	NA_Enum	NA_Enum
224	224	V	NA_Enum	NA_Enum	NA_Enum
225	225	\N	\N	\N	\N
226	226	\N	\N	\N	\N
227	227	\N	\N	\N	\N
228	228	\N	\N	\N	\N
229	229	\N	\N	\N	\N
230	230	\N	\N	\N	\N
231	231	\N	\N	\N	\N
232	232	\N	\N	\N	\N
233	233	\N	\N	\N	\N
234	234	\N	\N	\N	\N
235	235	\N	\N	\N	\N
236	236	\N	\N	\N	\N
237	237	\N	\N	\N	\N
238	238	\N	\N	\N	\N
239	239	\N	\N	\N	\N
240	240	\N	\N	\N	\N
241	241	\N	\N	\N	\N
242	242	\N	\N	\N	\N
243	243	\N	\N	\N	\N
244	244	V	X	X	X
245	245	V	X	X	X
246	246	V	X	X	X
247	247	V	X	X	X
248	248	V	X	X	X
249	249	V	X	X	X
250	250	V	X	X	X
251	251	V	X	X	X
252	252	V	X	X	X
253	253	V	X	X	X
254	254	V	X	X	X
255	255	V	X	X	X
256	256	V	NA_Enum	NA_Enum	NA_Enum
257	257	V	NA_Enum	NA_Enum	NA_Enum
258	258	V	NA_Enum	NA_Enum	NA_Enum
259	259	V	NA_Enum	NA_Enum	NA_Enum
260	260	V	NA_Enum	NA_Enum	NA_Enum
261	261	V	NA_Enum	NA_Enum	NA_Enum
262	262	V	NA_Enum	NA_Enum	NA_Enum
263	263	V	NA_Enum	NA_Enum	NA_Enum
264	264	V	NA_Enum	NA_Enum	NA_Enum
265	265	V	NA_Enum	NA_Enum	NA_Enum
266	266	V	NA_Enum	NA_Enum	NA_Enum
267	267	V	NA_Enum	NA_Enum	NA_Enum
268	268	V	NA_Enum	NA_Enum	NA_Enum
269	269	V	NA_Enum	NA_Enum	NA_Enum
270	270	V	NA_Enum	NA_Enum	NA_Enum
271	271	\N	\N	\N	\N
272	272	\N	\N	\N	\N
273	273	\N	\N	\N	\N
274	274	\N	\N	\N	\N
275	275	\N	\N	\N	\N
276	276	\N	\N	\N	\N
277	277	\N	\N	\N	\N
278	278	\N	\N	\N	\N
279	279	\N	\N	\N	\N
280	280	\N	\N	\N	\N
281	281	\N	\N	\N	\N
282	282	\N	\N	\N	\N
283	283	\N	\N	\N	\N
284	284	\N	\N	\N	\N
285	285	V	NA_Enum	NA_Enum	NA_Enum
286	286	V	NA_Enum	NA_Enum	NA_Enum
287	287	V	NA_Enum	NA_Enum	NA_Enum
288	288	V	NA_Enum	NA_Enum	NA_Enum
289	289	V	NA_Enum	NA_Enum	NA_Enum
290	290	V	NA_Enum	NA_Enum	NA_Enum
291	291	V	NA_Enum	NA_Enum	NA_Enum
292	292	V	NA_Enum	NA_Enum	NA_Enum
293	293	V	NA_Enum	NA_Enum	NA_Enum
294	294	V	NA_Enum	NA_Enum	NA_Enum
295	295	V	NA_Enum	NA_Enum	NA_Enum
296	296	V	NA_Enum	NA_Enum	NA_Enum
297	297	V	NA_Enum	NA_Enum	NA_Enum
298	298	V	NA_Enum	NA_Enum	NA_Enum
299	299	V	NA_Enum	NA_Enum	NA_Enum
300	300	V	NA_Enum	NA_Enum	NA_Enum
301	301	V	NA_Enum	NA_Enum	NA_Enum
302	302	V	NA_Enum	NA_Enum	NA_Enum
303	303	V	NA_Enum	NA_Enum	NA_Enum
304	304	V	NA_Enum	NA_Enum	NA_Enum
305	305	V	NA_Enum	NA_Enum	NA_Enum
306	306	V	NA_Enum	NA_Enum	NA_Enum
307	307	V	NA_Enum	NA_Enum	NA_Enum
308	308	V	NA_Enum	NA_Enum	NA_Enum
309	309	V	NA_Enum	NA_Enum	NA_Enum
310	310	V	NA_Enum	NA_Enum	NA_Enum
311	311	V	NA_Enum	NA_Enum	NA_Enum
312	312	V	NA_Enum	NA_Enum	NA_Enum
313	313	V	NA_Enum	NA_Enum	NA_Enum
314	314	V	NA_Enum	NA_Enum	NA_Enum
315	315	V	NA_Enum	NA_Enum	NA_Enum
316	316	V	NA_Enum	NA_Enum	NA_Enum
317	317	V	NA_Enum	NA_Enum	NA_Enum
318	318	V	NA_Enum	NA_Enum	NA_Enum
319	319	V	NA_Enum	NA_Enum	NA_Enum
320	320	V	NA_Enum	NA_Enum	NA_Enum
321	321	V	NA_Enum	NA_Enum	NA_Enum
322	322	V	NA_Enum	NA_Enum	NA_Enum
323	323	V	NA_Enum	NA_Enum	NA_Enum
324	324	V	NA_Enum	NA_Enum	NA_Enum
325	325	V	NA_Enum	NA_Enum	NA_Enum
326	326	V	NA_Enum	NA_Enum	NA_Enum
327	327	V	NA_Enum	NA_Enum	NA_Enum
328	328	V	NA_Enum	NA_Enum	NA_Enum
329	329	V	NA_Enum	NA_Enum	NA_Enum
330	330	V	NA_Enum	NA_Enum	NA_Enum
331	331	V	NA_Enum	NA_Enum	NA_Enum
332	332	V	NA_Enum	NA_Enum	NA_Enum
333	333	V	NA_Enum	NA_Enum	NA_Enum
334	334	V	NA_Enum	NA_Enum	NA_Enum
335	335	V	NA_Enum	NA_Enum	NA_Enum
336	336	V	NA_Enum	NA_Enum	NA_Enum
337	337	V	NA_Enum	NA_Enum	NA_Enum
338	338	V	NA_Enum	NA_Enum	NA_Enum
339	339	V	NA_Enum	NA_Enum	NA_Enum
340	340	V	NA_Enum	NA_Enum	NA_Enum
341	341	V	NA_Enum	NA_Enum	NA_Enum
342	342	V	NA_Enum	NA_Enum	NA_Enum
343	343	V	NA_Enum	NA_Enum	NA_Enum
344	344	V	NA_Enum	NA_Enum	NA_Enum
345	345	V	NA_Enum	NA_Enum	NA_Enum
346	346	V	NA_Enum	NA_Enum	NA_Enum
347	347	V	NA_Enum	NA_Enum	NA_Enum
348	348	V	NA_Enum	NA_Enum	NA_Enum
349	349	V	NA_Enum	NA_Enum	NA_Enum
350	350	V	NA_Enum	NA_Enum	NA_Enum
351	351	V	NA_Enum	NA_Enum	NA_Enum
352	352	V	NA_Enum	NA_Enum	NA_Enum
353	353	V	NA_Enum	NA_Enum	NA_Enum
354	354	V	NA_Enum	NA_Enum	NA_Enum
355	355	V	NA_Enum	NA_Enum	NA_Enum
356	356	V	NA_Enum	NA_Enum	NA_Enum
357	357	V	NA_Enum	NA_Enum	NA_Enum
358	358	V	NA_Enum	NA_Enum	NA_Enum
359	359	V	NA_Enum	NA_Enum	NA_Enum
360	360	V	NA_Enum	NA_Enum	NA_Enum
361	361	V	NA_Enum	NA_Enum	NA_Enum
362	362	V	NA_Enum	NA_Enum	NA_Enum
363	363	V	NA_Enum	NA_Enum	NA_Enum
364	364	V	NA_Enum	NA_Enum	NA_Enum
365	365	V	NA_Enum	NA_Enum	NA_Enum
366	366	V	NA_Enum	NA_Enum	NA_Enum
367	367	V	NA_Enum	NA_Enum	NA_Enum
368	368	V	NA_Enum	NA_Enum	NA_Enum
369	369	V	NA_Enum	NA_Enum	NA_Enum
370	370	V	NA_Enum	NA_Enum	NA_Enum
371	371	V	NA_Enum	NA_Enum	NA_Enum
372	372	V	NA_Enum	NA_Enum	NA_Enum
373	373	V	NA_Enum	NA_Enum	NA_Enum
374	374	V	NA_Enum	NA_Enum	NA_Enum
375	375	V	NA_Enum	NA_Enum	NA_Enum
376	376	V	NA_Enum	NA_Enum	NA_Enum
377	377	V	NA_Enum	NA_Enum	NA_Enum
378	378	V	NA_Enum	NA_Enum	NA_Enum
379	379	V	NA_Enum	NA_Enum	NA_Enum
380	380	V	NA_Enum	NA_Enum	NA_Enum
381	381	V	NA_Enum	NA_Enum	NA_Enum
382	382	V	NA_Enum	NA_Enum	NA_Enum
383	383	V	NA_Enum	NA_Enum	NA_Enum
384	384	V	NA_Enum	NA_Enum	NA_Enum
385	385	V	NA_Enum	NA_Enum	NA_Enum
386	386	V	NA_Enum	NA_Enum	NA_Enum
387	387	V	NA_Enum	NA_Enum	NA_Enum
388	388	V	NA_Enum	NA_Enum	NA_Enum
389	389	V	NA_Enum	NA_Enum	NA_Enum
390	390	V	NA_Enum	NA_Enum	NA_Enum
391	391	V	NA_Enum	NA_Enum	NA_Enum
392	392	V	NA_Enum	NA_Enum	NA_Enum
393	393	V	NA_Enum	NA_Enum	NA_Enum
394	394	V	NA_Enum	NA_Enum	NA_Enum
395	395	V	NA_Enum	NA_Enum	NA_Enum
396	396	V	NA_Enum	NA_Enum	NA_Enum
397	397	V	NA_Enum	NA_Enum	NA_Enum
398	398	V	NA_Enum	NA_Enum	NA_Enum
399	399	V	NA_Enum	NA_Enum	NA_Enum
400	400	V	NA_Enum	NA_Enum	NA_Enum
401	401	\N	\N	\N	\N
402	402	\N	\N	\N	\N
403	403	\N	\N	\N	\N
404	404	\N	\N	\N	\N
405	405	V	X	X	X
406	406	V	X	X	X
407	407	V	X	X	X
408	408	V	X	X	X
409	409	V	X	X	X
410	410	V	X	X	X
411	411	V	X	X	X
412	412	V	X	X	X
413	413	V	X	X	X
414	414	V	X	X	X
415	415	V	X	X	X
416	416	V	X	X	X
417	417	V	X	X	X
418	418	V	X	X	X
419	419	V	X	X	X
420	420	V	X	X	X
421	421	V	X	X	X
422	422	V	X	X	X
423	423	V	X	X	X
424	424	V	X	X	X
425	425	V	X	X	X
426	426	V	X	X	X
427	427	V	X	X	X
428	428	V	X	X	X
429	429	V	X	X	X
430	430	V	X	X	X
431	431	V	X	X	X
432	432	V	X	X	X
433	433	V	X	X	X
434	434	V	X	X	X
435	435	V	X	X	X
436	436	V	X	X	X
437	437	V	X	X	X
438	438	V	X	X	X
439	439	V	X	X	X
440	440	V	X	X	X
441	441	V	X	X	X
442	442	V	X	X	X
443	443	V	X	X	X
444	444	V	X	X	X
445	445	V	X	X	X
446	446	V	X	X	X
447	447	V	X	X	X
448	448	V	X	X	X
449	449	V	X	X	X
450	450	V	X	X	X
451	451	V	X	X	X
452	452	V	X	X	X
453	453	V	X	X	X
454	454	V	X	X	X
455	455	V	X	X	X
456	456	V	X	X	X
457	457	V	X	X	X
458	458	V	X	X	X
459	459	V	X	X	X
460	460	V	X	X	X
461	461	\N	\N	\N	\N
462	462	V	X	X	X
463	463	V	X	X	X
464	464	V	X	X	X
465	465	V	X	X	X
466	466	V	X	X	X
467	467	V	X	X	X
468	468	V	X	X	X
469	469	V	X	X	X
470	470	V	X	X	X
471	471	V	X	X	X
472	472	V	X	X	X
473	473	V	X	X	X
474	474	V	X	X	X
475	475	V	X	X	X
476	476	V	X	X	X
477	477	V	X	X	X
478	478	V	X	X	X
479	479	V	X	X	X
480	480	V	X	X	X
481	481	V	X	X	X
482	482	V	X	X	X
483	483	V	X	X	X
484	484	V	X	X	X
485	485	V	X	X	X
486	486	V	X	X	X
487	487	V	X	X	X
488	488	V	X	X	X
489	489	V	X	X	X
490	490	V	X	X	X
491	491	V	X	X	X
492	492	V	X	X	X
493	493	V	X	X	X
494	494	V	X	X	X
495	495	V	X	X	X
496	496	V	X	X	X
497	497	V	X	X	X
498	498	V	X	X	X
499	499	V	X	X	X
500	500	V	X	X	X
501	501	V	X	X	X
502	502	V	X	X	X
503	503	V	X	X	X
504	504	V	X	X	X
505	505	V	X	X	X
506	506	V	X	X	X
507	507	V	X	X	X
508	508	V	X	X	X
509	509	V	X	X	X
510	510	V	X	X	X
511	511	V	X	X	X
512	512	V	X	X	X
513	513	V	X	X	X
514	514	V	X	X	X
515	515	V	X	X	X
516	516	V	X	X	X
517	517	V	X	X	X
518	518	V	X	X	X
519	519	V	X	X	X
520	520	V	X	X	X
521	521	V	X	X	X
522	522	V	X	X	X
523	523	V	X	X	X
524	524	V	X	X	X
525	525	V	X	X	X
526	526	V	X	X	X
527	527	V	X	X	X
528	528	V	X	X	X
529	529	V	X	X	X
530	530	V	X	X	X
531	531	V	X	X	X
532	532	V	X	X	X
533	533	V	X	X	X
534	534	V	X	X	X
535	535	V	X	X	X
536	536	V	X	X	X
537	537	V	X	X	X
538	538	V	X	X	X
539	539	V	X	X	X
540	540	V	X	X	X
541	541	V	X	X	X
542	542	V	X	X	X
543	543	V	X	X	X
544	544	V	X	X	X
545	545	V	X	X	X
546	546	V	X	X	X
547	547	V	X	X	X
548	548	V	X	X	X
549	549	V	X	X	X
550	550	V	X	X	X
551	551	V	X	X	X
552	552	V	X	X	X
553	553	V	X	X	X
554	554	V	X	X	X
555	555	V	X	X	X
556	556	V	X	X	X
557	557	V	X	X	X
558	558	V	X	X	X
559	559	V	X	X	X
560	560	V	X	X	X
561	561	V	X	X	X
562	562	V	X	X	X
563	563	V	X	X	X
564	564	V	X	X	X
565	565	V	X	X	X
566	566	V	X	X	X
567	567	V	X	X	X
568	568	V	X	X	X
569	569	V	X	X	X
570	570	V	X	X	X
571	571	V	X	X	X
572	572	V	X	X	X
573	573	V	X	X	X
574	574	V	X	X	X
575	575	V	X	X	X
576	576	V	X	X	X
577	577	V	X	X	X
578	578	V	X	X	X
579	579	V	X	X	X
580	580	V	X	X	X
581	581	V	X	X	X
582	582	V	X	X	X
583	583	V	X	X	X
584	584	V	X	X	X
585	585	V	X	X	X
586	586	V	X	X	X
587	587	V	X	X	X
588	588	V	X	X	X
589	589	V	X	X	X
590	590	V	X	X	X
591	591	V	X	X	X
592	592	V	X	X	X
593	593	V	X	X	X
594	594	V	X	X	X
595	595	V	X	X	X
596	596	V	X	X	X
597	597	V	X	X	X
598	598	V	X	X	X
599	599	V	X	X	X
600	600	V	X	X	X
601	601	V	X	X	X
602	602	V	X	X	X
603	603	V	X	X	X
604	604	V	X	X	X
605	605	V	X	X	X
606	606	V	X	X	X
607	607	V	X	X	X
608	608	V	X	X	X
609	609	V	X	X	X
610	610	V	X	X	X
611	611	V	X	X	X
612	612	V	X	X	X
613	613	V	X	X	X
614	614	V	X	X	X
615	615	V	X	X	X
616	616	V	X	X	X
617	617	V	X	X	X
618	618	V	X	X	X
619	619	V	X	X	X
620	620	V	X	X	X
621	621	V	X	X	X
622	622	V	X	X	X
623	623	V	X	X	X
624	624	V	X	X	X
625	625	V	X	X	X
626	626	V	X	X	X
627	627	V	X	X	X
628	628	V	X	X	X
629	629	V	X	X	X
630	630	V	X	X	X
631	631	V	X	X	X
632	632	V	X	X	X
633	633	V	X	X	X
634	634	V	X	X	X
635	635	V	X	X	X
636	636	V	X	X	X
637	637	V	X	X	X
638	638	V	X	X	X
639	639	V	X	X	X
640	640	V	X	X	X
641	641	V	X	X	X
642	642	V	X	X	X
643	643	V	X	X	X
644	644	V	X	X	X
645	645	V	X	X	X
646	646	V	X	X	X
647	647	\N	\N	\N	\N
648	648	\N	\N	\N	\N
649	649	\N	\N	\N	\N
650	650	\N	\N	\N	\N
651	651	\N	\N	\N	\N
652	652	\N	\N	\N	\N
653	653	\N	\N	\N	\N
654	654	\N	\N	\N	\N
655	655	\N	\N	\N	\N
656	656	\N	\N	\N	\N
657	657	\N	\N	\N	\N
658	658	\N	\N	\N	\N
659	659	\N	\N	\N	\N
660	660	\N	\N	\N	\N
661	661	V	V	V	V
662	662	V	V	V	V
663	663	V	V	V	V
664	664	V	V	V	V
665	665	V	V	V	V
666	666	V	V	V	V
667	667	V	V	V	V
668	668	V	V	V	V
669	669	V	V	V	V
670	670	V	V	V	V
671	671	V	V	V	V
672	672	V	V	V	V
673	673	V	V	V	V
674	674	V	V	V	V
675	675	V	V	V	V
676	676	V	V	V	V
677	677	V	V	V	V
678	678	V	V	V	V
679	679	V	V	V	V
680	680	V	V	V	V
681	681	V	V	V	V
682	682	V	V	V	V
683	683	V	V	V	V
684	684	V	V	V	V
685	685	V	V	V	V
686	686	V	V	V	V
687	687	V	V	V	V
688	688	V	V	V	V
689	689	V	V	V	V
690	690	V	V	V	V
691	691	V	V	V	V
692	692	V	V	V	V
693	693	V	V	V	V
694	694	V	V	V	V
695	695	V	V	V	V
696	696	V	V	V	V
697	697	V	X	X	X
698	698	V	X	X	X
699	699	V	X	X	X
700	700	V	X	X	X
701	701	V	X	X	X
702	702	V	X	X	X
703	703	V	X	X	X
704	704	V	X	X	X
705	705	V	X	X	X
706	706	V	X	X	X
707	707	V	X	X	X
708	708	V	X	X	X
709	709	V	X	X	X
710	710	V	X	X	X
711	711	V	X	X	X
712	712	V	X	X	X
713	713	V	X	X	X
714	714	V	X	X	X
715	715	V	X	X	X
716	716	V	X	X	X
717	717	V	X	X	X
718	718	V	X	X	X
719	719	V	X	X	X
720	720	V	X	X	X
721	721	V	X	X	X
722	722	V	X	X	X
723	723	V	X	X	X
724	724	V	X	X	X
725	725	V	X	X	X
726	726	V	X	X	X
727	727	V	X	X	X
728	728	V	X	X	X
729	729	V	X	X	X
730	730	V	X	X	X
731	731	V	X	X	X
732	732	V	X	X	X
733	733	V	X	X	X
734	734	V	V	V	V
735	735	V	V	V	V
736	736	V	V	V	V
737	737	V	V	V	V
738	738	V	V	V	V
739	739	V	V	V	V
740	740	V	V	V	V
741	741	V	V	V	V
742	742	V	V	V	V
743	743	V	V	V	V
744	744	V	V	V	V
745	745	V	V	V	V
746	746	V	V	V	V
747	747	V	V	V	V
748	748	V	V	V	V
749	749	V	V	V	V
750	750	V	V	V	V
751	751	V	V	V	V
752	752	V	V	V	V
753	753	V	V	V	V
754	754	V	V	V	V
755	755	V	V	V	V
756	756	V	V	V	V
757	757	V	V	V	V
758	758	V	V	V	V
759	759	V	V	V	V
760	760	V	V	V	V
761	761	V	V	V	V
762	762	V	V	V	V
763	763	V	V	V	V
764	764	V	V	V	V
765	765	V	V	V	V
766	766	V	V	V	V
767	767	V	V	V	V
768	768	V	V	V	V
769	769	V	V	V	V
770	770	V	V	V	V
771	771	V	V	V	V
772	772	V	V	V	V
773	773	V	V	V	V
774	774	V	V	V	V
775	775	V	V	V	V
776	776	V	V	V	V
777	777	V	V	V	V
778	778	V	V	V	V
779	779	V	V	V	V
780	780	V	V	V	V
781	781	V	V	V	V
782	782	V	V	V	V
783	783	V	V	V	V
784	784	V	V	V	V
785	785	V	V	V	V
786	786	V	V	V	V
787	787	V	V	V	V
788	788	V	V	V	V
789	789	V	V	V	V
790	790	V	V	V	V
791	791	V	V	V	V
792	792	V	V	V	V
793	793	V	V	V	V
794	794	V	V	V	V
795	795	V	V	V	V
796	796	V	V	V	V
797	797	V	V	V	V
798	798	V	V	V	V
799	799	V	X	X	X
800	800	V	X	X	X
801	801	V	X	X	X
802	802	V	X	X	X
803	803	V	X	X	X
804	804	V	X	X	X
805	805	V	X	X	X
806	806	V	X	X	X
807	807	V	X	X	X
808	808	V	X	X	X
809	809	V	X	X	X
810	810	V	X	X	X
811	811	V	X	X	X
812	812	V	X	X	X
813	813	V	X	X	X
814	814	V	X	X	X
815	815	V	X	X	X
816	816	V	X	X	X
817	817	V	X	X	X
818	818	V	X	X	X
819	819	V	X	X	X
820	820	V	X	X	X
821	821	V	X	X	X
822	822	V	X	X	X
823	823	V	X	X	X
824	824	V	X	X	X
825	825	V	X	X	X
826	826	V	X	X	X
827	827	V	X	X	X
828	828	V	X	X	X
829	829	V	X	X	X
830	830	V	X	X	X
831	831	V	X	X	X
832	832	V	X	X	X
833	833	V	X	X	X
834	834	V	X	X	X
835	835	V	X	X	X
836	836	V	X	X	X
837	837	V	X	X	X
838	838	V	X	X	X
839	839	V	X	X	X
840	840	V	X	X	X
841	841	V	X	X	X
842	842	V	X	X	X
843	843	V	X	X	X
844	844	V	X	X	X
845	845	V	X	X	X
846	846	V	X	X	X
847	847	V	X	X	X
848	848	V	X	X	X
849	849	V	X	X	X
850	850	V	X	X	X
851	851	V	X	X	X
852	852	V	X	X	X
853	853	V	X	X	X
854	854	V	X	X	X
855	855	V	X	X	X
856	856	V	X	X	X
857	857	V	X	X	X
858	858	V	X	X	X
859	859	V	X	X	X
860	860	V	X	X	X
861	861	V	X	X	X
862	862	V	X	X	X
863	863	V	X	X	X
864	864	V	X	X	X
865	865	V	X	X	X
866	866	V	X	X	X
867	867	V	X	X	X
868	868	V	X	X	X
869	869	V	X	X	X
870	870	V	X	X	X
871	871	V	X	X	X
872	872	V	X	X	X
873	873	V	X	X	X
874	874	V	X	X	X
875	875	V	X	X	X
876	876	V	X	X	X
877	877	V	X	X	X
878	878	V	X	X	X
879	879	V	X	X	X
880	880	V	X	X	X
881	881	V	X	X	X
882	882	V	X	X	X
883	883	V	X	X	X
884	884	V	X	X	X
885	885	V	X	X	X
886	886	V	X	X	X
887	887	V	X	X	X
888	888	V	X	X	X
889	889	V	X	X	X
890	890	V	X	X	X
891	891	V	X	X	X
892	892	V	X	X	X
893	893	V	X	X	X
894	894	V	X	X	X
895	895	V	X	X	X
896	896	V	X	X	X
897	897	V	X	X	X
898	898	V	X	X	X
899	899	V	X	X	X
900	900	V	X	X	X
901	901	V	X	X	X
902	902	V	X	X	X
903	903	V	X	X	X
904	904	V	X	X	X
905	905	V	X	X	X
906	906	V	X	X	X
907	907	V	X	X	X
908	908	V	X	X	X
909	909	V	X	X	X
910	910	V	X	X	X
911	911	V	X	X	X
912	912	V	X	X	X
913	913	V	X	X	X
914	914	V	X	X	X
915	915	V	X	X	X
916	916	V	X	X	X
917	917	V	X	X	X
918	918	V	X	X	X
919	919	V	X	X	X
920	920	V	X	X	X
921	921	V	X	X	X
922	922	V	X	X	X
923	923	V	X	X	X
924	924	V	X	X	X
925	925	V	X	X	X
926	926	V	X	X	X
927	927	V	X	X	X
928	928	V	X	X	X
929	929	V	X	X	X
930	930	V	X	X	X
931	931	V	X	X	X
932	932	\N	\N	\N	\N
933	933	\N	\N	\N	\N
934	934	\N	\N	\N	\N
935	935	\N	\N	\N	\N
936	936	\N	\N	\N	\N
937	937	\N	\N	\N	\N
938	938	\N	\N	\N	\N
939	939	\N	\N	\N	\N
940	940	\N	\N	\N	\N
941	941	\N	\N	\N	\N
942	942	\N	\N	\N	\N
943	943	\N	\N	\N	\N
944	944	\N	\N	\N	\N
945	945	\N	\N	\N	\N
946	946	\N	\N	\N	\N
947	947	\N	\N	\N	\N
948	948	\N	\N	\N	\N
949	949	\N	\N	\N	\N
950	950	\N	\N	\N	\N
951	951	\N	\N	\N	\N
952	952	\N	\N	\N	\N
953	953	V	V	V	V
954	954	V	V	V	V
955	955	V	V	V	V
956	956	V	V	V	V
957	957	V	V	V	V
958	958	V	V	V	V
959	959	V	V	V	V
960	960	V	V	V	V
961	961	V	V	V	V
962	962	V	V	V	V
963	963	V	V	V	V
964	964	V	V	V	V
965	965	V	V	V	V
966	966	V	V	V	V
967	967	V	V	V	V
968	968	V	V	V	V
969	969	V	V	V	V
970	970	V	V	V	V
971	971	V	NA_Enum	NA_Enum	NA_Enum
972	972	V	NA_Enum	NA_Enum	NA_Enum
973	973	V	NA_Enum	NA_Enum	NA_Enum
974	974	V	NA_Enum	NA_Enum	NA_Enum
975	975	V	NA_Enum	NA_Enum	NA_Enum
976	976	\N	\N	\N	\N
977	977	\N	\N	\N	\N
978	978	\N	\N	\N	\N
979	979	V	NA_Enum	NA_Enum	NA_Enum
980	980	V	NA_Enum	NA_Enum	NA_Enum
981	981	V	NA_Enum	NA_Enum	NA_Enum
982	982	V	NA_Enum	NA_Enum	NA_Enum
983	983	V	NA_Enum	NA_Enum	NA_Enum
984	984	V	NA_Enum	NA_Enum	NA_Enum
985	985	V	NA_Enum	NA_Enum	NA_Enum
986	986	V	NA_Enum	NA_Enum	NA_Enum
987	987	V	NA_Enum	NA_Enum	NA_Enum
988	988	V	NA_Enum	NA_Enum	NA_Enum
989	989	V	NA_Enum	NA_Enum	NA_Enum
990	990	V	NA_Enum	NA_Enum	NA_Enum
991	991	V	NA_Enum	NA_Enum	NA_Enum
992	992	V	NA_Enum	NA_Enum	NA_Enum
993	993	V	NA_Enum	NA_Enum	NA_Enum
994	994	V	NA_Enum	NA_Enum	NA_Enum
995	995	V	NA_Enum	NA_Enum	NA_Enum
996	996	V	NA_Enum	NA_Enum	NA_Enum
997	997	V	NA_Enum	NA_Enum	NA_Enum
998	998	V	NA_Enum	NA_Enum	NA_Enum
999	999	V	NA_Enum	NA_Enum	NA_Enum
1000	1000	V	NA_Enum	NA_Enum	NA_Enum
1001	1001	V	NA_Enum	NA_Enum	NA_Enum
1002	1002	V	NA_Enum	NA_Enum	NA_Enum
1003	1003	V	NA_Enum	NA_Enum	NA_Enum
1004	1004	V	NA_Enum	NA_Enum	NA_Enum
1005	1005	V	NA_Enum	NA_Enum	NA_Enum
1006	1006	V	NA_Enum	NA_Enum	NA_Enum
1007	1007	V	NA_Enum	NA_Enum	NA_Enum
1008	1008	V	NA_Enum	NA_Enum	NA_Enum
1009	1009	V	NA_Enum	NA_Enum	NA_Enum
1010	1010	V	NA_Enum	NA_Enum	NA_Enum
1011	1011	V	NA_Enum	NA_Enum	NA_Enum
1012	1012	V	NA_Enum	NA_Enum	NA_Enum
1013	1013	V	NA_Enum	NA_Enum	NA_Enum
1014	1014	V	NA_Enum	NA_Enum	NA_Enum
1015	1015	V	NA_Enum	NA_Enum	NA_Enum
1016	1016	V	NA_Enum	NA_Enum	NA_Enum
1017	1017	V	NA_Enum	NA_Enum	NA_Enum
\.


--
-- TOC entry 5016 (class 0 OID 16419)
-- Dependencies: 223
-- Data for Name: leo_gp_numbers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.leo_gp_numbers (id, project_id, company_number, gp_no) FROM stdin;
1	1	\N	\N
2	2	\N	\N
3	3	\N	\N
4	4	\N	\N
5	5	\N	\N
6	6	\N	\N
7	7	HS#1G	#0636
8	8	HS#1S	#1260
9	9	HS#2RE	\N
10	10	HS#1MS	\N
11	11	\N	\N
12	12	HS#1K	#0694
13	13	HS#1GR	\N
14	14	HS#1PI/HS#1PK	\N
15	15	HS#4MG	\N
16	16	HS#2K	\N
17	17	\N	\N
18	18	\N	\N
19	19	HS#2G	\N
20	20	\N	\N
21	21	HS#1CO	\N
22	22	HS#2G	\N
23	23	\N	\N
24	24	\N	\N
25	25	\N	\N
26	26	\N	\N
27	27	\N	\N
28	28	\N	\N
29	29	\N	\N
30	30	\N	\N
31	31	\N	\N
32	32	\N	\N
33	33	\N	\N
34	34	\N	\N
35	35	\N	\N
36	36	\N	\N
37	37	\N	\N
38	38	\N	\N
39	39	\N	\N
40	40	\N	\N
41	41	\N	\N
42	42	\N	\N
43	43	\N	\N
44	44	\N	\N
45	45	\N	\N
46	46	\N	\N
47	47	\N	\N
48	48	\N	\N
49	49	\N	\N
50	50	\N	\N
51	51	\N	\N
52	52	\N	\N
53	53	\N	\N
54	54	\N	\N
55	55	\N	\N
56	56	\N	\N
57	57	\N	\N
58	58	\N	\N
59	59	\N	\N
60	60	\N	\N
61	61	HS#1S	#1260
62	62	HS#1S	#1260
63	63	\N	\N
64	64	\N	\N
65	65	\N	\N
66	66	\N	\N
67	67	HG#01Y	\N
68	68	HS#1GR	\N
69	69	HS#2PU	\N
70	70	HS#4BL	\N
71	71	HS#3BL	\N
72	72	HS#1PK	\N
73	73	HS#2GR	\N
74	74	HS#3CO	\N
75	75	HS#1MG	\N
76	76	\N	5734
77	77	\N	#0174
78	78	\N	635
79	79	\N	700
80	80	\N	205
81	81	\N	201
82	82	HS#2G	\N
83	83	HS#2RE	\N
84	84	HS#1K	#0694
85	85	\N	5736
86	86	\N	\N
87	87	HS#8MG	\N
88	88	HS#1CO	\N
89	89	HS#6G	\N
90	90	A58-7	\N
91	91	HS#1BL	\N
92	92	HS#1PU	\N
93	93	HS#1S	#1260
94	94	HS#2G	\N
95	95	HS#1G	\N
96	96	HS#4G	\N
97	97	HS#4MG	\N
98	98	HS#2PK	\N
99	99	HS#1MS	\N
100	100	HS#2CO	\N
101	101	HS#5MG	\N
102	102	HS#1RE	\N
103	103	HS#2BL	\N
104	104	\N	\N
105	105	739	\N
106	106	5716	\N
107	107	HG#02S	\N
108	108	HG#01BL	\N
109	109	HG#01RE	\N
110	110	5713	\N
111	111	HG#01GR	\N
112	112	HG#01Y	\N
113	113	5720	\N
114	114	HG#01S	\N
115	115	HG#06S/0698	\N
116	116	HG#08S	\N
117	117	HG#01G	\N
118	118	\N	\N
119	119	HG#14S	\N
120	120	HG#10S	\N
121	121	HG#01Y	\N
122	122	HG#01S	\N
123	123	\N	\N
124	124	\N	\N
125	125	\N	\N
126	126	\N	\N
127	127	\N	\N
128	128	\N	\N
129	129	\N	\N
130	130	\N	\N
131	131	\N	\N
132	132	\N	\N
133	133	\N	\N
134	134	\N	\N
135	135	\N	\N
136	136	\N	\N
137	137	\N	\N
138	138	\N	\N
139	139	\N	\N
140	140	\N	\N
141	141	\N	\N
142	142	\N	\N
143	143	\N	\N
144	144	\N	\N
145	145	\N	\N
146	146	\N	\N
147	147	\N	\N
148	148	\N	\N
149	149	\N	\N
150	150	\N	\N
151	151	\N	\N
152	152	\N	\N
153	153	HS#1RE	\N
154	154	HS#1BL	\N
155	155	\N	\N
156	156	\N	\N
157	157	\N	\N
158	158	\N	#0182
159	159	\N	#0700
160	160	\N	720
161	161	\N	#0201
162	162	HS#1G	#0636
163	163	\N	#0156
164	164	HS#1G	636
165	165	\N	\N
166	166	HS#1GR	\N
167	167	HS#1K	#0694
168	168	\N	\N
169	169	\N	\N
170	170	\N	174
171	171	\N	#0183
172	172	\N	#0156
173	173	\N	201
174	174	HS#1G	636
175	175	\N	#0156
176	176	\N	#5739
177	177	\N	\N
178	178	\N	\N
179	179	\N	\N
180	180	\N	\N
181	181	\N	\N
182	182	\N	\N
183	183	\N	\N
184	184	\N	\N
185	185	\N	\N
186	186	\N	\N
187	187	\N	\N
188	188	\N	\N
189	189	\N	\N
190	190	\N	\N
191	191	\N	\N
192	192	\N	\N
193	193	\N	\N
194	194	\N	\N
195	195	\N	\N
196	196	\N	\N
197	197	\N	\N
198	198	\N	\N
199	199	\N	\N
200	200	\N	\N
201	201	\N	\N
202	202	\N	\N
203	203	\N	\N
204	204	HS#1S	\N
205	205	\N	\N
206	206	\N	\N
207	207	\N	\N
208	208	\N	\N
209	209	\N	\N
210	210	\N	\N
211	211	\N	\N
212	212	\N	\N
213	213	\N	\N
214	214	\N	\N
215	215	\N	\N
216	216	\N	\N
217	217	\N	\N
218	218	\N	\N
219	219	\N	\N
220	220	\N	\N
221	221	\N	\N
222	222	HG#06/0698	\N
223	223	\N	\N
224	224	HG#01PU	\N
225	225	HS#1S	#1260
226	226	\N	\N
227	227	\N	\N
228	228	\N	\N
229	229	\N	\N
230	230	\N	\N
231	231	\N	\N
232	232	\N	\N
233	233	\N	\N
234	234	\N	\N
235	235	\N	\N
236	236	\N	\N
237	237	\N	\N
238	238	\N	\N
239	239	\N	\N
240	240	\N	\N
241	241	\N	\N
242	242	\N	\N
243	243	\N	\N
244	244	HS#1S	#1260
245	245	HS#1MG	\N
246	246	HS#4G	\N
247	247	HS#1CO	\N
248	248	HS#1MS	\N
249	249	HS#1K	\N
250	250	HS#1S	\N
251	251	HS#1MS	\N
252	252	HS#2CO	\N
253	253	\N	\N
254	254	HS#1MG	\N
255	255	HS#4G	\N
256	256	HG#06S	\N
257	257	HG#01G	\N
258	258	HG#01PU	\N
259	259	\N	\N
260	260	HG#16S	\N
261	261	HG#14S	\N
262	262	\N	\N
263	263	#5720	\N
264	264	\N	\N
265	265	\N	\N
266	266	\N	\N
267	267	HG#01S	\N
268	268	\N	\N
269	269	732	\N
270	270	730	\N
271	271	\N	\N
272	272	\N	\N
273	273	\N	\N
274	274	\N	\N
275	275	5800	\N
276	276	\N	\N
277	277	HG#01TR	\N
278	278	5800	\N
279	279	\N	\N
280	280	\N	\N
281	281	\N	\N
282	282	\N	\N
283	283	\N	\N
284	284	\N	\N
285	285	5713	\N
286	286	HG#01S	\N
287	287	\N	\N
288	288	\N	\N
289	289	\N	\N
290	290	\N	\N
291	291	\N	\N
292	292	HG#02S	\N
293	293	\N	\N
294	294	\N	\N
295	295	\N	\N
296	296	\N	\N
297	297	HG#10S	\N
298	298	\N	\N
299	299	\N	\N
300	300	\N	\N
301	301	HG#10S	\N
302	302	HG#15S	\N
303	303	HG#14S	\N
304	304	\N	\N
305	305	\N	\N
306	306	\N	\N
307	307	\N	\N
308	308	\N	\N
309	309	\N	\N
310	310	\N	\N
311	311	\N	\N
312	312	\N	\N
313	313	HG#01RE	\N
314	314	HG#01PU	\N
315	315	\N	\N
316	316	HG#01GR	\N
317	317	\N	\N
318	318	\N	\N
319	319	\N	\N
320	320	HG#03S	\N
321	321	\N	\N
322	322	\N	\N
323	323	\N	\N
324	324	\N	\N
325	325	\N	\N
326	326	\N	\N
327	327	HG#06S/0698	\N
328	328	\N	\N
329	329	\N	\N
330	330	\N	\N
331	331	\N	\N
332	332	\N	\N
333	333	\N	\N
334	334	\N	\N
335	335	\N	\N
336	336	\N	\N
337	337	\N	\N
338	338	\N	\N
339	339	HG#06S	\N
340	340	5716	\N
341	341	739	\N
342	342	\N	\N
343	343	\N	\N
344	344	\N	\N
345	345	\N	\N
346	346	HG#08S	\N
347	347	\N	\N
348	348	\N	\N
349	349	5713	\N
350	350	HG#07S	\N
351	351	\N	\N
352	352	\N	\N
353	353	\N	\N
354	354	\N	\N
355	355	\N	\N
356	356	\N	\N
357	357	\N	\N
358	358	\N	\N
359	359	\N	\N
360	360	\N	\N
361	361	\N	\N
362	362	\N	\N
363	363	\N	\N
364	364	\N	\N
365	365	\N	\N
366	366	\N	\N
367	367	\N	\N
368	368	\N	\N
369	369	\N	\N
370	370	\N	\N
371	371	\N	\N
372	372	\N	\N
373	373	\N	\N
374	374	\N	\N
375	375	\N	\N
376	376	HG#01G	\N
377	377	\N	\N
378	378	\N	\N
379	379	5720	\N
380	380	\N	\N
381	381	\N	\N
382	382	\N	\N
383	383	\N	\N
384	384	\N	\N
385	385	\N	\N
386	386	\N	\N
387	387	\N	\N
388	388	\N	\N
389	389	698	\N
390	390	\N	\N
391	391	\N	\N
392	392	\N	\N
393	393	\N	\N
394	394	\N	\N
395	395	\N	\N
396	396	\N	\N
397	397	740	\N
398	398	HG#01GR	\N
399	399	\N	\N
400	400	\N	\N
401	401	\N	\N
402	402	\N	\N
403	403	\N	\N
404	404	\N	\N
405	405	\N	\N
406	406	\N	\N
407	407	\N	\N
408	408	\N	\N
409	409	\N	\N
410	410	\N	\N
411	411	\N	\N
412	412	\N	\N
413	413	\N	\N
414	414	\N	\N
415	415	\N	\N
416	416	\N	\N
417	417	\N	\N
418	418	\N	\N
419	419	\N	\N
420	420	\N	\N
421	421	\N	\N
422	422	\N	\N
423	423	\N	\N
424	424	\N	\N
425	425	\N	\N
426	426	\N	\N
427	427	\N	\N
428	428	\N	\N
429	429	\N	\N
430	430	\N	\N
431	431	\N	\N
432	432	\N	\N
433	433	\N	\N
434	434	\N	\N
435	435	\N	\N
436	436	\N	\N
437	437	\N	\N
438	438	\N	\N
439	439	\N	\N
440	440	\N	\N
441	441	\N	\N
442	442	\N	\N
443	443	\N	\N
444	444	\N	\N
445	445	\N	\N
446	446	\N	\N
447	447	\N	\N
448	448	\N	\N
449	449	\N	\N
450	450	\N	\N
451	451	\N	\N
452	452	\N	\N
453	453	\N	\N
454	454	\N	\N
455	455	\N	\N
456	456	\N	\N
457	457	\N	\N
458	458	\N	\N
459	459	\N	\N
460	460	\N	\N
461	461	\N	\N
462	462	\N	\N
463	463	\N	\N
464	464	\N	\N
465	465	\N	\N
466	466	\N	\N
467	467	\N	\N
468	468	\N	\N
469	469	\N	\N
470	470	\N	\N
471	471	\N	\N
472	472	\N	\N
473	473	\N	\N
474	474	\N	\N
475	475	\N	\N
476	476	\N	\N
477	477	\N	\N
478	478	\N	\N
479	479	\N	\N
480	480	\N	\N
481	481	\N	\N
482	482	\N	\N
483	483	\N	\N
484	484	\N	\N
485	485	\N	\N
486	486	\N	\N
487	487	\N	\N
488	488	\N	\N
489	489	\N	\N
490	490	\N	\N
491	491	\N	\N
492	492	\N	\N
493	493	\N	\N
494	494	\N	\N
495	495	\N	\N
496	496	\N	\N
497	497	\N	\N
498	498	\N	\N
499	499	\N	\N
500	500	\N	\N
501	501	\N	\N
502	502	\N	\N
503	503	\N	\N
504	504	\N	\N
505	505	\N	\N
506	506	\N	\N
507	507	\N	\N
508	508	\N	\N
509	509	\N	\N
510	510	\N	\N
511	511	\N	\N
512	512	\N	\N
513	513	\N	\N
514	514	\N	\N
515	515	\N	\N
516	516	\N	\N
517	517	\N	\N
518	518	\N	\N
519	519	\N	\N
520	520	\N	\N
521	521	\N	\N
522	522	\N	\N
523	523	\N	\N
524	524	\N	\N
525	525	\N	\N
526	526	\N	\N
527	527	\N	\N
528	528	\N	\N
529	529	\N	\N
530	530	\N	\N
531	531	\N	\N
532	532	\N	\N
533	533	\N	\N
534	534	\N	\N
535	535	\N	\N
536	536	\N	\N
537	537	\N	\N
538	538	\N	\N
539	539	\N	\N
540	540	\N	\N
541	541	\N	\N
542	542	\N	\N
543	543	\N	\N
544	544	\N	\N
545	545	\N	\N
546	546	\N	\N
547	547	\N	\N
548	548	\N	\N
549	549	\N	\N
550	550	\N	\N
551	551	\N	\N
552	552	\N	\N
553	553	\N	\N
554	554	\N	\N
555	555	\N	\N
556	556	\N	\N
557	557	\N	\N
558	558	\N	\N
559	559	\N	\N
560	560	\N	\N
561	561	\N	\N
562	562	\N	\N
563	563	\N	\N
564	564	#5738	#5738
565	565	\N	\N
566	566	\N	\N
567	567	\N	\N
568	568	\N	\N
569	569	\N	\N
570	570	\N	\N
571	571	\N	\N
572	572	\N	\N
573	573	\N	\N
574	574	\N	\N
575	575	\N	\N
576	576	\N	\N
577	577	\N	\N
578	578	\N	\N
579	579	\N	\N
580	580	\N	\N
581	581	\N	\N
582	582	\N	\N
583	583	\N	\N
584	584	\N	\N
585	585	\N	\N
586	586	\N	\N
587	587	\N	\N
588	588	\N	\N
589	589	\N	\N
590	590	\N	\N
591	591	\N	\N
592	592	\N	\N
593	593	#5738	#5738
594	594	\N	\N
595	595	\N	\N
596	596	\N	\N
597	597	\N	\N
598	598	\N	\N
599	599	\N	\N
600	600	\N	\N
601	601	\N	\N
602	602	\N	\N
603	603	\N	\N
604	604	\N	\N
605	605	\N	\N
606	606	\N	\N
607	607	\N	\N
608	608	\N	\N
609	609	\N	\N
610	610	\N	\N
611	611	\N	\N
612	612	\N	\N
613	613	\N	\N
614	614	\N	\N
615	615	\N	\N
616	616	\N	\N
617	617	\N	\N
618	618	\N	\N
619	619	\N	\N
620	620	\N	\N
621	621	\N	\N
622	622	\N	\N
623	623	\N	\N
624	624	\N	\N
625	625	\N	\N
626	626	\N	\N
627	627	\N	\N
628	628	\N	\N
629	629	\N	\N
630	630	\N	\N
631	631	\N	\N
632	632	\N	\N
633	633	\N	\N
634	634	\N	\N
635	635	\N	\N
636	636	\N	\N
637	637	\N	\N
638	638	\N	\N
639	639	\N	\N
640	640	\N	\N
641	641	\N	\N
642	642	\N	\N
643	643	\N	\N
644	644	\N	\N
645	645	\N	\N
646	646	\N	\N
647	647	\N	#0700
648	648	\N	#0174
649	649	\N	#0149
650	650	\N	#0636
651	651	HS#1MS	\N
652	652	\N	\N
653	653	\N	\N
654	654	\N	\N
655	655	\N	\N
656	656	\N	\N
657	657	\N	\N
658	658	\N	\N
659	659	\N	\N
660	660	\N	\N
661	661	\N	#0634
662	662	\N	#0156
663	663	\N	#0182
664	664	\N	#0700
665	665	\N	#0174
666	666	\N	149
667	667	HS#1G	#0636
668	668	\N	#0700
669	669	\N	#0156
670	670	HS#1K	#0694
671	671	HS#1G	#0636
672	672	\N	#5738
673	673	HS#1S	#1260
674	674	\N	#0149
675	675	\N	#0174
676	676	\N	#0183
677	677	\N	634
678	678	HS#1K	694
679	679	\N	751
680	680	HS#1S	#1260
681	681	\N	751
682	682	\N	5738
683	683	\N	205
684	684	\N	#0635
685	685	\N	#0183
686	686	\N	691
687	687	\N	182
688	688	\N	#0205
689	689	\N	#5734
690	690	\N	720
691	691	\N	#0720
692	692	\N	2152
693	693	\N	5734
694	694	\N	201
695	695	\N	#0201
696	696	HS#2G	\N
697	697	HS#250/50-006-0638L	#0638
698	698	HS#6G	\N
699	699	HS#1GR/#40	\N
700	700	HS#1G	#0636
701	701	HS#1MG	#5737
702	702	HS#1MS	\N
703	703	HS#4G	\N
704	704	\N	#0174
705	705	\N	#0634
706	706	\N	720
707	707	\N	#0700
708	708	\N	#0149
709	709	\N	#0156
710	710	\N	#5734
711	711	\N	#1110
712	712	\N	\N
713	713	\N	#0205
714	714	\N	#0182
715	715	\N	#0691
716	716	\N	#0635
717	717	\N	#0183
718	718	HS#1K	#0694
719	719	\N	635
720	720	\N	#0156
721	721	\N	#2152
722	722	\N	#0174
723	723	\N	#0751
724	724	HS#250	638
725	725	HS#1S	636
726	726	\N	#0634
727	727	HS#250/50-006-0638L	#0638
728	728	\N	691
729	729	\N	5734
730	730	\N	#0201
731	731	HS#4MG	\N
732	732	\N	#0156
733	733	\N	#0635
734	734	\N	#0183
735	735	\N	#0156
736	736	\N	#0700
737	737	\N	#0634
738	738	\N	#1110
739	739	\N	#0635
740	740	\N	#0205
741	741	\N	#0182
742	742	\N	#0149
743	743	\N	#5738
744	744	\N	#0174
745	745	HS#1G	#0636
746	746	\N	#0700
747	747	\N	#0156
748	748	HS#1G	#0636
749	749	\N	#0201
750	750	HS#1S	#1260
751	751	\N	#0182
752	752	HS#1S	#1260
753	753	\N	#0149
754	754	\N	#0174
755	755	\N	#0183
756	756	\N	#0205
757	757	\N	#0634
758	758	\N	#0691
759	759	HS#1K	#0694
760	760	\N	#1110
761	761	\N	#2152
762	762	HS#1MG	#5737
763	763	\N	#0635
764	764	\N	#0751
765	765	\N	#0720
766	766	\N	#0720
767	767	HS#1K	#0694
768	768	\N	#0201
769	769	\N	#5734
770	770	\N	#0691
771	771	\N	#0751
772	772	\N	#2152
773	773	HS#1MG	#5737
774	774	\N	#5738
775	775	\N	\N
776	776	\N	2152
777	777	HS#2G	\N
778	778	HS#6G	\N
779	779	\N	#5734
780	780	HS#4G	\N
781	781	HS#4MG	\N
782	782	HS#5MG	\N
783	783	HS#8MG	\N
784	784	HS#1MS	\N
785	785	HS#1CO	\N
786	786	HS#2CO	\N
787	787	HS#1BL	\N
788	788	HS#3CO	\N
789	789	HS#2BL	\N
790	790	HS#3BL	\N
791	791	HS#4BL	\N
792	792	HS#1RE	\N
793	793	HS#1PU	\N
794	794	HS#2PU	\N
795	795	HS#2PI/HS#2PK	\N
796	796	HS#1GR	\N
797	797	\N	5736
798	798	\N	5739
799	799	HS#1MG	#5737
800	800	HS#1G	#0636
801	801	\N	#0156
802	802	\N	#0700
803	803	HS#1S	#1260
804	804	\N	#0149
805	805	\N	#0174
806	806	\N	#0182
807	807	\N	#0183
808	808	\N	#0201
809	809	\N	#0205
810	810	\N	#0634
811	811	\N	#0635
812	812	HS#250/50-006-0638L	#0638
813	813	\N	#0691
814	814	HS#1K	#0694
815	815	\N	#0720
816	816	\N	#0751
817	817	\N	#1110
818	818	\N	#2152
819	819	\N	#5734
820	820	\N	#5738
821	821	\N	#5739
822	822	\N	#0149
823	823	\N	#0183
824	824	\N	#0700
825	825	HS#1G	#0636
826	826	\N	#0634
827	827	\N	#0174
828	828	\N	#0635
829	829	HS#1S	#1260
830	830	\N	#0720
831	831	\N	#0156
832	832	\N	#0182
833	833	\N	#0691
834	834	HS#250/50-006-0638L	#00638
835	835	HS#1K	#0694
836	836	\N	#0205
837	837	\N	#0751
838	838	\N	#1110
839	839	\N	#2152
840	840	HS#1MG	#5737
841	841	\N	#5738
842	842	HS#1G	636
843	843	\N	#0201
844	844	\N	5734
845	845	\N	5736
846	846	\N	#2152
847	847	\N	#0634
848	848	HS#1G	636
849	849	\N	#0720
850	850	\N	#0700
851	851	\N	183
852	852	\N	#0174
853	853	HS#1S	1260
854	854	\N	#0635
855	855	\N	#0205
856	856	\N	#0751
857	857	\N	#0156
858	858	HS#2G	\N
859	859	HS#4G	\N
860	860	HS#6G	\N
861	861	HS#1PU	\N
862	862	HS#1CO	\N
863	863	HS#1RE	\N
864	864	HS#2G	\N
865	865	HS#6G	\N
866	866	HS#4MG	\N
867	867	HS#4G	\N
868	868	HS#1MS	\N
869	869	\N	\N
870	870	\N	\N
871	871	#04-3	\N
872	872	HS#3BL	\N
873	873	HS#1GR	\N
874	874	HS#6MG	\N
875	875	#31-7	\N
876	876	HS#2BL	\N
877	877	#50	\N
878	878	\N	#0205
879	879	\N	#0751
880	880	\N	#0149
881	881	\N	#0201
882	882	\N	\N
883	883	HS#8MG	\N
884	884	HS#3CO	\N
885	885	#55	\N
886	886	\N	700
887	887	\N	183
888	888	HS#4G	\N
889	889	\N	#0183
890	890	\N	700
891	891	\N	149
892	892	HS#1G	636
893	893	\N	5738
894	894	HS#1PU	\N
895	895	HS#250/50-006-0638L	#0638
896	896	HS#1MG	#5737
897	897	\N	\N
898	898	\N	\N
899	899	#48-1	\N
900	900	HS#6MG	\N
901	901	HS#1S	#1260
902	902	HS#1G	#0636
903	903	HS#2G	\N
904	904	HS#8MG	\N
905	905	HS#4MG	\N
906	906	HS#4G	\N
907	907	HS#2CO	\N
908	908	HS#5MG	\N
909	909	HS#6G	\N
910	910	HS#1MS	\N
911	911	HS#1MG	#5737
912	912	HS#3CO	\N
913	913	HS#1BL	\N
914	914	HS#1RE	\N
915	915	HS#2PI/HS#2PK	\N
916	916	HS#1GR	\N
917	917	HS#1K	#0694
918	918	HS#1CO	\N
919	919	HS#3BL	\N
920	920	HS#2BL	\N
921	921	HS#4BL	\N
922	922	\N	\N
923	923	\N	\N
924	924	HS#2CO	\N
925	925	HS#2K	\N
926	926	HS#6MG	\N
927	927	HS#3BL	\N
928	928	HS#2PU	\N
929	929	HS#2RE	\N
930	930	\N	\N
931	931	\N	\N
932	932	\N	\N
933	933	\N	\N
934	934	\N	\N
935	935	\N	\N
936	936	\N	\N
937	937	\N	\N
938	938	\N	\N
939	939	\N	\N
940	940	\N	\N
941	941	\N	\N
942	942	\N	\N
943	943	\N	\N
944	944	\N	\N
945	945	\N	\N
946	946	HS#1G	636
947	947	HS#1G	636
948	948	\N	\N
949	949	\N	\N
950	950	\N	#0205
951	951	HS#1G	\N
952	952	HG#06S	\N
953	953	\N	\N
954	954	\N	\N
955	955	\N	\N
956	956	\N	#0156
957	957	\N	#0694
958	958	\N	\N
959	959	\N	#0634
960	960	\N	#0149
961	961	\N	#0700
962	962	\N	#0205
963	963	\N	#5738
964	964	\N	\N
965	965	\N	\N
966	966	\N	\N
967	967	\N	\N
968	968	\N	\N
969	969	\N	\N
970	970	\N	\N
971	971	\N	\N
972	972	\N	\N
973	973	\N	\N
974	974	\N	\N
975	975	\N	\N
976	976	\N	\N
977	977	\N	\N
978	978	\N	\N
979	979	\N	\N
980	980	\N	\N
981	981	\N	\N
982	982	\N	\N
983	983	\N	\N
984	984	\N	\N
985	985	\N	\N
986	986	\N	\N
987	987	\N	\N
988	988	\N	\N
989	989	\N	\N
990	990	\N	\N
991	991	\N	\N
992	992	\N	\N
993	993	\N	\N
994	994	\N	\N
995	995	\N	\N
996	996	\N	\N
997	997	\N	\N
998	998	\N	\N
999	999	\N	\N
1000	1000	\N	\N
1001	1001	\N	\N
1002	1002	\N	\N
1003	1003	\N	\N
1004	1004	\N	\N
1005	1005	\N	\N
1006	1006	\N	\N
1007	1007	\N	\N
1008	1008	\N	\N
1009	1009	\N	\N
1010	1010	\N	\N
1011	1011	\N	\N
1012	1012	\N	\N
1013	1013	\N	\N
1014	1014	\N	\N
1015	1015	\N	\N
1016	1016	\N	\N
1017	1017	\N	\N
\.


--
-- TOC entry 5018 (class 0 OID 16425)
-- Dependencies: 225
-- Data for Name: matte_lamination; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.matte_lamination (id, project_id, standard_furonghui_22d, pre_coated_hy1206_65, high_tack_pre_coated_hy40, pre_coated_economical_high_wear_resistant_yt008a, pre_coated_high_wear_resistant_tn008, pre_coated_standard_wear_resistant_kvmb_f, soft_touch_matte_laminate_6015a) FROM stdin;
1	1	\N	\N	\N	\N	\N	\N	\N
2	2	\N	\N	\N	\N	\N	\N	\N
3	3	\N	\N	\N	\N	\N	\N	\N
4	4	\N	\N	\N	\N	\N	\N	\N
5	5	\N	\N	\N	\N	\N	\N	\N
6	6	\N	\N	\N	\N	\N	\N	\N
7	7	\N	\N	\N	\N	\N	\N	\N
8	8	\N	\N	\N	\N	\N	\N	\N
9	9	\N	\N	\N	\N	\N	\N	\N
10	10	\N	\N	\N	\N	\N	\N	\N
11	11	\N	\N	\N	\N	\N	\N	\N
12	12	\N	\N	\N	\N	\N	\N	\N
13	13	\N	\N	\N	\N	\N	\N	\N
14	14	\N	\N	\N	\N	\N	\N	\N
15	15	\N	\N	\N	\N	\N	\N	\N
16	16	\N	\N	\N	\N	\N	\N	\N
17	17	\N	\N	\N	\N	\N	\N	\N
18	18	\N	\N	\N	\N	\N	\N	\N
19	19	\N	\N	\N	\N	\N	\N	\N
20	20	\N	\N	\N	\N	\N	\N	\N
21	21	\N	\N	\N	\N	\N	\N	\N
22	22	\N	\N	\N	\N	\N	\N	\N
23	23	\N	\N	\N	\N	\N	\N	\N
24	24	\N	\N	\N	\N	\N	\N	\N
25	25	\N	\N	\N	\N	\N	\N	\N
26	26	\N	\N	\N	\N	\N	\N	\N
27	27	\N	\N	\N	\N	\N	\N	\N
28	28	\N	\N	\N	\N	\N	\N	\N
29	29	\N	\N	\N	\N	\N	\N	\N
30	30	\N	\N	\N	\N	\N	\N	\N
31	31	\N	\N	\N	\N	\N	\N	\N
32	32	\N	\N	\N	\N	\N	\N	\N
33	33	\N	\N	\N	\N	\N	\N	\N
34	34	\N	\N	\N	\N	\N	\N	\N
35	35	\N	\N	\N	\N	\N	\N	\N
36	36	\N	\N	\N	\N	\N	\N	\N
37	37	\N	\N	\N	\N	\N	\N	\N
38	38	\N	\N	\N	\N	\N	\N	\N
39	39	\N	\N	\N	\N	\N	\N	\N
40	40	\N	\N	\N	\N	\N	\N	\N
41	41	\N	\N	\N	\N	\N	\N	\N
42	42	\N	\N	\N	\N	\N	\N	\N
43	43	\N	\N	\N	\N	\N	\N	\N
44	44	\N	\N	\N	\N	\N	\N	\N
45	45	\N	\N	\N	\N	\N	\N	\N
46	46	\N	\N	\N	\N	\N	\N	\N
47	47	\N	\N	\N	\N	\N	\N	\N
48	48	\N	\N	\N	\N	\N	\N	\N
49	49	\N	\N	\N	\N	\N	\N	\N
50	50	\N	\N	\N	\N	\N	\N	\N
51	51	\N	\N	\N	\N	\N	\N	\N
52	52	\N	\N	\N	\N	\N	\N	\N
53	53	\N	\N	\N	\N	\N	\N	\N
54	54	\N	\N	\N	\N	\N	\N	\N
55	55	\N	\N	\N	\N	\N	\N	\N
56	56	\N	\N	\N	\N	\N	\N	\N
57	57	\N	\N	\N	\N	\N	\N	\N
58	58	\N	\N	\N	\N	\N	\N	\N
59	59	\N	\N	\N	\N	\N	\N	\N
60	60	\N	\N	\N	\N	\N	\N	\N
61	61	X	X	X	X	X	X	X
62	62	X	X	X	X	X	X	X
63	63	\N	\N	\N	\N	\N	\N	\N
64	64	\N	\N	\N	\N	\N	\N	\N
65	65	\N	\N	\N	\N	\N	\N	\N
66	66	\N	\N	\N	\N	\N	\N	\N
67	67	V	V	V	X	X	V	V
68	68	V	V	V	X	X	V	V
69	69	V	V	V	X	X	V	V
70	70	V	V	V	X	X	V	V
71	71	V	V	V	X	X	V	V
72	72	V	V	V	X	X	V	V
73	73	V	V	V	X	X	V	V
74	74	V	V	V	X	X	V	V
75	75	V	V	V	X	X	V	V
76	76	V	V	V	X	X	V	V
77	77	V	V	V	X	X	V	V
78	78	V	V	V	X	X	V	V
79	79	V	V	V	X	X	V	V
80	80	V	V	V	X	X	V	V
81	81	V	V	V	X	X	V	V
82	82	V	V	V	X	X	V	V
83	83	V	V	V	X	X	V	V
84	84	V	V	V	X	X	V	V
85	85	V	V	V	X	X	V	V
86	86	V	V	V	X	X	V	V
87	87	V	V	V	X	X	V	V
88	88	V	V	V	X	X	V	V
89	89	V	V	V	X	X	V	V
90	90	V	V	V	X	X	V	V
91	91	V	V	V	X	X	V	V
92	92	V	V	V	X	X	V	V
93	93	V	V	V	X	X	V	V
94	94	V	V	V	X	X	V	V
95	95	V	V	V	X	X	V	V
96	96	V	V	V	X	X	V	V
97	97	V	V	V	X	X	V	V
98	98	V	V	V	X	X	V	V
99	99	V	V	V	X	X	V	V
100	100	V	V	V	X	X	V	V
101	101	V	V	V	X	X	V	V
102	102	V	V	V	X	X	V	V
103	103	V	V	V	X	X	V	V
104	104	V	V	V	X	X	V	V
105	105	X	X	X	X	X	X	X
106	106	X	X	X	X	X	X	X
107	107	X	X	X	X	X	X	X
108	108	X	X	X	X	X	X	X
109	109	X	X	X	X	X	X	X
110	110	X	X	X	X	X	X	X
111	111	X	X	X	X	X	X	X
112	112	X	X	X	X	X	X	X
113	113	X	X	X	X	X	X	X
114	114	X	X	X	X	X	X	X
115	115	X	X	X	X	X	X	X
116	116	X	X	X	X	X	X	X
117	117	X	X	X	X	X	X	X
118	118	X	X	X	X	X	X	X
119	119	X	X	X	X	X	X	X
120	120	X	X	X	X	X	X	X
121	121	X	X	X	X	X	X	X
122	122	X	X	X	X	X	X	X
123	123	X	X	X	X	X	X	X
124	124	\N	\N	\N	\N	\N	\N	\N
125	125	\N	\N	\N	\N	\N	\N	\N
126	126	\N	\N	\N	\N	\N	\N	\N
127	127	\N	\N	\N	\N	\N	\N	\N
128	128	\N	\N	\N	\N	\N	\N	\N
129	129	\N	\N	\N	\N	\N	\N	\N
130	130	\N	\N	\N	\N	\N	\N	\N
131	131	\N	\N	\N	\N	\N	\N	\N
132	132	\N	\N	\N	\N	\N	\N	\N
133	133	\N	\N	\N	\N	\N	\N	\N
134	134	\N	\N	\N	\N	\N	\N	\N
135	135	\N	\N	\N	\N	\N	\N	\N
136	136	\N	\N	\N	\N	\N	\N	\N
137	137	\N	\N	\N	\N	\N	\N	\N
138	138	\N	\N	\N	\N	\N	\N	\N
139	139	\N	\N	\N	\N	\N	\N	\N
140	140	\N	\N	\N	\N	\N	\N	\N
141	141	\N	\N	\N	\N	\N	\N	\N
142	142	\N	\N	\N	\N	\N	\N	\N
143	143	\N	\N	\N	\N	\N	\N	\N
144	144	\N	\N	\N	\N	\N	\N	\N
145	145	\N	\N	\N	\N	\N	\N	\N
146	146	\N	\N	\N	\N	\N	\N	\N
147	147	\N	\N	\N	\N	\N	\N	\N
148	148	\N	\N	\N	\N	\N	\N	\N
149	149	\N	\N	\N	\N	\N	\N	\N
150	150	\N	\N	\N	\N	\N	\N	\N
151	151	\N	\N	\N	\N	\N	\N	\N
152	152	\N	\N	\N	\N	\N	\N	\N
153	153	V	V	V	X	X	V	V
154	154	V	V	V	X	X	V	V
155	155	V	V	V	X	X	V	V
156	156	V	V	V	X	X	V	V
157	157	V	V	V	X	X	V	V
158	158	V	V	V	X	X	V	V
159	159	V	V	V	X	X	V	V
160	160	V	V	V	X	X	V	V
161	161	V	V	V	X	X	V	V
162	162	V	V	V	X	X	V	V
163	163	V	V	V	X	X	V	V
164	164	V	V	V	X	X	V	V
165	165	V	V	V	X	X	V	V
166	166	V	V	V	X	X	V	V
167	167	V	V	V	X	X	V	V
168	168	V	V	V	X	X	V	V
169	169	V	V	V	X	X	V	V
170	170	V	V	V	X	X	V	V
171	171	V	V	V	X	X	V	V
172	172	V	V	V	X	X	V	V
173	173	V	V	V	X	X	V	V
174	174	V	V	V	X	X	V	V
175	175	V	V	V	X	X	V	V
176	176	\N	\N	\N	\N	\N	\N	\N
177	177	\N	\N	\N	\N	\N	\N	\N
178	178	\N	\N	\N	\N	\N	\N	\N
179	179	\N	\N	\N	\N	\N	\N	\N
180	180	V	V	V	X	X	V	V
181	181	V	V	V	X	X	V	V
182	182	V	V	V	X	X	V	V
183	183	V	V	V	X	X	V	V
184	184	V	V	V	X	X	V	V
185	185	V	V	V	X	X	V	V
186	186	V	V	V	X	X	V	V
187	187	V	V	V	X	X	V	V
188	188	V	V	V	X	X	V	V
189	189	V	V	V	X	X	V	V
190	190	V	V	V	X	X	V	V
191	191	V	V	V	X	X	V	V
192	192	V	V	V	X	X	V	V
193	193	V	V	V	X	X	V	V
194	194	V	V	V	X	X	V	V
195	195	V	V	V	X	X	V	V
196	196	V	V	V	X	X	V	V
197	197	V	V	V	X	X	V	V
198	198	V	V	V	X	X	V	V
199	199	V	V	V	X	X	V	V
200	200	V	V	V	X	X	V	V
201	201	V	V	V	X	X	V	V
202	202	V	V	V	X	X	V	V
203	203	V	V	V	X	X	V	V
204	204	V	V	V	X	X	V	V
205	205	V	V	V	X	X	V	V
206	206	V	V	V	X	X	V	V
207	207	V	V	V	X	X	V	V
208	208	V	V	V	X	X	V	V
209	209	V	V	V	X	X	V	V
210	210	X	X	X	X	X	X	X
211	211	X	X	X	X	X	X	X
212	212	X	X	X	X	X	X	X
213	213	X	X	X	X	X	X	X
214	214	X	X	X	X	X	X	X
215	215	X	X	X	X	X	X	X
216	216	X	X	X	X	X	X	X
217	217	X	X	X	X	X	X	X
218	218	X	X	X	X	X	X	X
219	219	X	X	X	X	X	X	X
220	220	X	X	X	X	X	X	X
221	221	X	X	X	X	X	X	X
222	222	X	X	X	X	X	X	X
223	223	X	X	X	X	X	X	X
224	224	X	X	X	X	X	X	X
225	225	\N	\N	\N	\N	\N	\N	\N
226	226	\N	\N	\N	\N	\N	\N	\N
227	227	\N	\N	\N	\N	\N	\N	\N
228	228	\N	\N	\N	\N	\N	\N	\N
229	229	\N	\N	\N	\N	\N	\N	\N
230	230	\N	\N	\N	\N	\N	\N	\N
231	231	\N	\N	\N	\N	\N	\N	\N
232	232	\N	\N	\N	\N	\N	\N	\N
233	233	\N	\N	\N	\N	\N	\N	\N
234	234	\N	\N	\N	\N	\N	\N	\N
235	235	\N	\N	\N	\N	\N	\N	\N
236	236	\N	\N	\N	\N	\N	\N	\N
237	237	\N	\N	\N	\N	\N	\N	\N
238	238	\N	\N	\N	\N	\N	\N	\N
239	239	\N	\N	\N	\N	\N	\N	\N
240	240	\N	\N	\N	\N	\N	\N	\N
241	241	\N	\N	\N	\N	\N	\N	\N
242	242	\N	\N	\N	\N	\N	\N	\N
243	243	\N	\N	\N	\N	\N	\N	\N
244	244	X	X	X	X	X	X	X
245	245	X	X	X	X	X	X	X
246	246	X	X	X	X	X	X	X
247	247	X	X	X	X	X	X	X
248	248	X	X	X	X	X	X	X
249	249	X	X	X	X	X	X	X
250	250	X	X	X	X	X	X	X
251	251	X	X	X	X	X	X	X
252	252	X	X	X	X	X	X	X
253	253	X	X	X	X	X	X	X
254	254	X	X	X	X	X	X	X
255	255	X	X	X	X	X	X	X
256	256	X	X	X	X	X	X	X
257	257	X	X	X	X	X	X	X
258	258	X	X	X	X	X	X	X
259	259	X	X	X	X	X	X	X
260	260	X	X	X	X	X	X	X
261	261	X	X	X	X	X	X	X
262	262	X	X	X	X	X	X	X
263	263	X	X	X	X	X	X	X
264	264	X	X	X	X	X	X	X
265	265	X	X	X	X	X	X	X
266	266	X	X	X	X	X	X	X
267	267	X	X	X	X	X	X	X
268	268	X	X	X	X	X	X	X
269	269	V	V	V	X	X	V	V
270	270	V	V	V	X	X	V	V
271	271	\N	\N	\N	\N	\N	\N	\N
272	272	\N	\N	\N	\N	\N	\N	\N
273	273	\N	\N	\N	\N	\N	\N	\N
274	274	\N	\N	\N	\N	\N	\N	\N
275	275	\N	\N	\N	\N	\N	\N	\N
276	276	\N	\N	\N	\N	\N	\N	\N
277	277	\N	\N	\N	\N	\N	\N	\N
278	278	\N	\N	\N	\N	\N	\N	\N
279	279	\N	\N	\N	\N	\N	\N	\N
280	280	\N	\N	\N	\N	\N	\N	\N
281	281	\N	\N	\N	\N	\N	\N	\N
282	282	\N	\N	\N	\N	\N	\N	\N
283	283	\N	\N	\N	\N	\N	\N	\N
284	284	\N	\N	\N	\N	\N	\N	\N
285	285	V	V	V	X	X	V	V
286	286	V	V	V	X	X	V	V
287	287	V	V	V	X	X	V	V
288	288	V	V	V	X	X	V	V
289	289	V	V	V	X	X	V	V
290	290	V	V	V	X	X	V	V
291	291	V	V	V	X	X	V	V
292	292	V	V	V	X	X	V	V
293	293	V	V	V	X	X	V	V
294	294	V	V	V	X	X	V	V
295	295	V	V	V	X	X	V	V
296	296	V	V	V	X	X	V	V
297	297	V	V	V	X	X	V	V
298	298	V	V	V	X	X	V	V
299	299	V	V	V	X	X	V	V
300	300	V	V	V	X	X	V	V
301	301	V	V	V	X	X	V	V
302	302	V	V	V	X	X	V	V
303	303	V	V	V	X	X	V	V
304	304	V	V	V	X	X	V	V
305	305	V	V	V	X	X	V	V
306	306	V	V	V	X	X	V	V
307	307	V	V	V	X	X	V	V
308	308	V	V	V	X	X	V	V
309	309	V	V	V	X	X	V	V
310	310	V	V	V	X	X	V	V
311	311	V	V	V	X	X	V	V
312	312	V	V	V	X	X	V	V
313	313	V	V	V	X	X	V	V
314	314	V	V	V	X	X	V	V
315	315	V	V	V	X	X	V	V
316	316	V	V	V	X	X	V	V
317	317	V	V	V	X	X	V	V
318	318	V	V	V	X	X	V	V
319	319	V	V	V	X	X	V	V
320	320	V	V	V	X	X	V	V
321	321	V	V	V	X	X	V	V
322	322	V	V	V	X	X	V	V
323	323	V	V	V	X	X	V	V
324	324	V	V	V	X	X	V	V
325	325	V	V	V	X	X	V	V
326	326	V	V	V	X	X	V	V
327	327	V	V	V	X	X	V	V
328	328	V	V	V	X	X	V	V
329	329	V	V	V	X	X	V	V
330	330	V	V	V	X	X	V	V
331	331	V	V	V	X	X	V	V
332	332	V	V	V	X	X	V	V
333	333	V	V	V	X	X	V	V
334	334	V	V	V	X	X	V	V
335	335	V	V	V	X	X	V	V
336	336	V	V	V	X	X	V	V
337	337	V	V	V	X	X	V	V
338	338	V	V	V	X	X	V	V
339	339	V	V	V	X	X	V	V
340	340	V	V	V	X	X	V	V
341	341	V	V	V	X	X	V	V
342	342	V	V	V	X	X	V	V
343	343	V	V	V	X	X	V	V
344	344	V	V	V	X	X	V	V
345	345	V	V	V	X	X	V	V
346	346	V	V	V	X	X	V	V
347	347	V	V	V	X	X	V	V
348	348	V	V	V	X	X	V	V
349	349	V	V	V	X	X	V	V
350	350	V	V	V	X	X	V	V
351	351	V	V	V	X	X	V	V
352	352	V	V	V	X	X	V	V
353	353	V	V	V	X	X	V	V
354	354	V	V	V	X	X	V	V
355	355	V	V	V	X	X	V	V
356	356	V	V	V	X	X	V	V
357	357	V	V	V	X	X	V	V
358	358	V	V	V	X	X	V	V
359	359	V	V	V	X	X	V	V
360	360	V	V	V	X	X	V	V
361	361	V	V	V	X	X	V	V
362	362	V	V	V	X	X	V	V
363	363	V	V	V	X	X	V	V
364	364	V	V	V	X	X	V	V
365	365	V	V	V	X	X	V	V
366	366	V	V	V	X	X	V	V
367	367	V	V	V	X	X	V	V
368	368	V	V	V	X	X	V	V
369	369	V	V	V	X	X	V	V
370	370	V	V	V	X	X	V	V
371	371	V	V	V	X	X	V	V
372	372	V	V	V	X	X	V	V
373	373	V	V	V	X	X	V	V
374	374	V	V	V	X	X	V	V
375	375	V	V	V	X	X	V	V
376	376	V	V	V	X	X	V	V
377	377	V	V	V	X	X	V	V
378	378	V	V	V	X	X	V	V
379	379	V	V	V	X	X	V	V
380	380	V	V	V	X	X	V	V
381	381	V	V	V	X	X	V	V
382	382	V	V	V	X	X	V	V
383	383	V	V	V	X	X	V	V
384	384	V	V	V	X	X	V	V
385	385	V	V	V	X	X	V	V
386	386	V	V	V	X	X	V	V
387	387	V	V	V	X	X	V	V
388	388	V	V	V	X	X	V	V
389	389	V	V	V	X	X	V	V
390	390	V	V	V	X	X	V	V
391	391	V	V	V	X	X	V	V
392	392	V	V	V	X	X	V	V
393	393	V	V	V	X	X	V	V
394	394	V	V	V	X	X	V	V
395	395	V	V	V	X	X	V	V
396	396	V	V	V	X	X	V	V
397	397	V	V	V	X	X	V	V
398	398	V	V	V	X	X	V	V
399	399	V	V	V	X	X	V	V
400	400	V	V	V	X	X	V	V
401	401	\N	\N	\N	\N	\N	\N	\N
402	402	\N	\N	\N	\N	\N	\N	\N
403	403	\N	\N	\N	\N	\N	\N	\N
404	404	\N	\N	\N	\N	\N	\N	\N
405	405	V	V	V	X	X	V	V
406	406	V	V	V	X	X	V	V
407	407	V	V	V	X	X	V	V
408	408	V	V	V	X	X	V	V
409	409	V	V	V	X	X	V	V
410	410	V	V	V	X	X	V	V
411	411	V	V	V	X	X	V	V
412	412	V	V	V	X	X	V	V
413	413	V	V	V	X	X	V	V
414	414	V	V	V	X	X	V	V
415	415	V	V	V	X	X	V	V
416	416	V	V	V	X	X	V	V
417	417	V	V	V	X	X	V	V
418	418	V	V	V	X	X	V	V
419	419	V	V	V	X	X	V	V
420	420	V	V	V	X	X	V	V
421	421	V	V	V	X	X	V	V
422	422	V	V	V	X	X	V	V
423	423	V	V	V	X	X	V	V
424	424	V	V	V	X	X	V	V
425	425	V	V	V	X	X	V	V
426	426	V	V	V	X	X	V	V
427	427	V	V	V	X	X	V	V
428	428	V	V	V	X	X	V	V
429	429	V	V	V	X	X	V	V
430	430	V	V	V	X	X	V	V
431	431	X	X	X	X	X	X	X
432	432	X	X	X	X	X	X	X
433	433	X	X	X	X	X	X	X
434	434	X	X	X	X	X	X	X
435	435	X	X	X	X	X	X	X
436	436	X	X	X	X	X	X	X
437	437	X	X	X	X	X	X	X
438	438	X	X	X	X	X	X	X
439	439	X	X	X	X	X	X	X
440	440	X	X	X	X	X	X	X
441	441	X	X	X	X	X	X	X
442	442	X	X	X	X	X	X	X
443	443	X	X	X	X	X	X	X
444	444	X	X	X	X	X	X	X
445	445	X	X	X	X	X	X	X
446	446	X	X	X	X	X	X	X
447	447	X	X	X	X	X	X	X
448	448	X	X	X	X	X	X	X
449	449	X	X	X	X	X	X	X
450	450	X	X	X	X	X	X	X
451	451	X	X	X	X	X	X	X
452	452	X	X	X	X	X	X	X
453	453	X	X	X	X	X	X	X
454	454	X	X	X	X	X	X	X
455	455	X	X	X	X	X	X	X
456	456	X	X	X	X	X	X	X
457	457	X	X	X	X	X	X	X
458	458	X	X	X	X	X	X	X
459	459	X	X	X	X	X	X	X
460	460	X	X	X	X	X	X	X
461	461	\N	\N	\N	\N	\N	\N	\N
462	462	V	V	V	X	X	V	V
463	463	V	V	V	X	X	V	V
464	464	V	V	V	X	X	V	V
465	465	V	V	V	X	X	V	V
466	466	V	V	V	X	X	V	V
467	467	V	V	V	X	X	V	V
468	468	V	V	V	X	X	V	V
469	469	V	V	V	X	X	V	V
470	470	V	V	V	X	X	V	V
471	471	V	V	V	X	X	V	V
472	472	V	V	V	X	X	V	V
473	473	V	V	V	X	X	V	V
474	474	V	V	V	X	X	V	V
475	475	V	V	V	X	X	V	V
476	476	V	V	V	X	X	V	V
477	477	V	V	V	X	X	V	V
478	478	V	V	V	X	X	V	V
479	479	V	V	V	X	X	V	V
480	480	V	V	V	X	X	V	V
481	481	V	V	V	X	X	V	V
482	482	V	V	V	X	X	V	V
483	483	V	V	V	X	X	V	V
484	484	V	V	V	X	X	V	V
485	485	V	V	V	X	X	V	V
486	486	V	V	V	X	X	V	V
487	487	V	V	V	X	X	V	V
488	488	V	V	V	X	X	V	V
489	489	V	V	V	X	X	V	V
490	490	V	V	V	X	X	V	V
491	491	V	V	V	X	X	V	V
492	492	V	V	V	X	X	V	V
493	493	V	V	V	X	X	V	V
494	494	V	V	V	X	X	V	V
495	495	V	V	V	X	X	V	V
496	496	V	V	V	X	X	V	V
497	497	V	V	V	X	X	V	V
498	498	V	V	V	X	X	V	V
499	499	V	V	V	X	X	V	V
500	500	V	V	V	X	X	V	V
501	501	V	V	V	X	X	V	V
502	502	V	V	V	X	X	V	V
503	503	V	V	V	X	X	V	V
504	504	V	V	V	X	X	V	V
505	505	V	V	V	X	X	V	V
506	506	V	V	V	X	X	V	V
507	507	V	V	V	X	X	V	V
508	508	V	V	V	X	X	V	V
509	509	V	V	V	X	X	V	V
510	510	V	V	V	X	X	V	V
511	511	V	V	V	X	X	V	V
512	512	V	V	V	X	X	V	V
513	513	V	V	V	X	X	V	V
514	514	V	V	V	X	X	V	V
515	515	V	V	V	X	X	V	V
516	516	V	V	V	X	X	V	V
517	517	V	V	V	X	X	V	V
518	518	V	V	V	X	X	V	V
519	519	V	V	V	X	X	V	V
520	520	V	V	V	X	X	V	V
521	521	V	V	V	X	X	V	V
522	522	V	V	V	X	X	V	V
523	523	V	V	V	X	X	V	V
524	524	V	V	V	X	X	V	V
525	525	V	V	V	X	X	V	V
526	526	V	V	V	X	X	V	V
527	527	V	V	V	X	X	V	V
528	528	V	V	V	X	X	V	V
529	529	V	V	V	X	X	V	V
530	530	V	V	V	X	X	V	V
531	531	V	V	V	X	X	V	V
532	532	V	V	V	X	X	V	V
533	533	V	V	V	X	X	V	V
534	534	V	V	V	X	X	V	V
535	535	V	V	V	X	X	V	V
536	536	V	V	V	X	X	V	V
537	537	V	V	V	X	X	V	V
538	538	V	V	V	X	X	V	V
539	539	V	V	V	X	X	V	V
540	540	V	V	V	X	X	V	V
541	541	V	V	V	X	X	V	V
542	542	V	V	V	X	X	V	V
543	543	V	V	V	X	X	V	V
544	544	V	V	V	X	X	V	V
545	545	V	V	V	X	X	V	V
546	546	V	V	V	X	X	V	V
547	547	V	V	V	X	X	V	V
548	548	V	V	V	X	X	V	V
549	549	V	V	V	X	X	V	V
550	550	V	V	V	X	X	V	V
551	551	V	V	V	X	X	V	V
552	552	V	V	V	X	X	V	V
553	553	V	V	V	X	X	V	V
554	554	V	V	V	X	X	V	V
555	555	V	V	V	X	X	V	V
556	556	V	V	V	X	X	V	V
557	557	V	V	V	X	X	V	V
558	558	V	V	V	X	X	V	V
559	559	V	V	V	X	X	V	V
560	560	V	V	V	X	X	V	V
561	561	V	V	V	X	X	V	V
562	562	X	X	X	X	X	X	X
563	563	X	X	X	X	X	X	X
564	564	X	X	X	X	X	X	X
565	565	X	X	X	X	X	X	X
566	566	X	X	X	X	X	X	X
567	567	X	X	X	X	X	X	X
568	568	X	X	X	X	X	X	X
569	569	X	X	X	X	X	X	X
570	570	X	X	X	X	X	X	X
571	571	X	X	X	X	X	X	X
572	572	X	X	X	X	X	X	X
573	573	X	X	X	X	X	X	X
574	574	X	X	X	X	X	X	X
575	575	X	X	X	X	X	X	X
576	576	X	X	X	X	X	X	X
577	577	X	X	X	X	X	X	X
578	578	X	X	X	X	X	X	X
579	579	X	X	X	X	X	X	X
580	580	X	X	X	X	X	X	X
581	581	X	X	X	X	X	X	X
582	582	X	X	X	X	X	X	X
583	583	X	X	X	X	X	X	X
584	584	X	X	X	X	X	X	X
585	585	X	X	X	X	X	X	X
586	586	X	X	X	X	X	X	X
587	587	X	X	X	X	X	X	X
588	588	X	X	X	X	X	X	X
589	589	X	X	X	X	X	X	X
590	590	X	X	X	X	X	X	X
591	591	X	X	X	X	X	X	X
592	592	X	X	X	X	X	X	X
593	593	X	X	X	X	X	X	X
594	594	X	X	X	X	X	X	X
595	595	X	X	X	X	X	X	X
596	596	X	X	X	X	X	X	X
597	597	X	X	X	X	X	X	X
598	598	X	X	X	X	X	X	X
599	599	X	X	X	X	X	X	X
600	600	X	X	X	X	X	X	X
601	601	X	X	X	X	X	X	X
602	602	X	X	X	X	X	X	X
603	603	X	X	X	X	X	X	X
604	604	X	X	X	X	X	X	X
605	605	X	X	X	X	X	X	X
606	606	X	X	X	X	X	X	X
607	607	X	X	X	X	X	X	X
608	608	X	X	X	X	X	X	X
609	609	X	X	X	X	X	X	X
610	610	X	X	X	X	X	X	X
611	611	X	X	X	X	X	X	X
612	612	X	X	X	X	X	X	X
613	613	X	X	X	X	X	X	X
614	614	X	X	X	X	X	X	X
615	615	X	X	X	X	X	X	X
616	616	X	X	X	X	X	X	X
617	617	X	X	X	X	X	X	X
618	618	X	X	X	X	X	X	X
619	619	X	X	X	X	X	X	X
620	620	X	X	X	X	X	X	X
621	621	X	X	X	X	X	X	X
622	622	X	X	X	X	X	X	X
623	623	X	X	X	X	X	X	X
624	624	X	X	X	X	X	X	X
625	625	X	X	X	X	X	X	X
626	626	X	X	X	X	X	X	X
627	627	X	X	X	X	X	X	X
628	628	X	X	X	X	X	X	X
629	629	X	X	X	X	X	X	X
630	630	X	X	X	X	X	X	X
631	631	X	X	X	X	X	X	X
632	632	X	X	X	X	X	X	X
633	633	X	X	X	X	X	X	X
634	634	X	X	X	X	X	X	X
635	635	X	X	X	X	X	X	X
636	636	X	X	X	X	X	X	X
637	637	X	X	X	X	X	X	X
638	638	X	X	X	X	X	X	X
639	639	X	X	X	X	X	X	X
640	640	X	X	X	X	X	X	X
641	641	X	X	X	X	X	X	X
642	642	X	X	X	X	X	X	X
643	643	X	X	X	X	X	X	X
644	644	X	X	X	X	X	X	X
645	645	X	X	X	X	X	X	X
646	646	X	X	X	X	X	X	X
647	647	\N	\N	\N	\N	\N	\N	\N
648	648	\N	\N	\N	\N	\N	\N	\N
649	649	\N	\N	\N	\N	\N	\N	\N
650	650	\N	\N	\N	\N	\N	\N	\N
651	651	\N	\N	\N	\N	\N	\N	\N
652	652	\N	\N	\N	\N	\N	\N	\N
653	653	\N	\N	\N	\N	\N	\N	\N
654	654	\N	\N	\N	\N	\N	\N	\N
655	655	\N	\N	\N	\N	\N	\N	\N
656	656	\N	\N	\N	\N	\N	\N	\N
657	657	\N	\N	\N	\N	\N	\N	\N
658	658	\N	\N	\N	\N	\N	\N	\N
659	659	\N	\N	\N	\N	\N	\N	\N
660	660	\N	\N	\N	\N	\N	\N	\N
661	661	V	V	V	X	X	V	V
662	662	V	V	V	X	X	V	V
663	663	V	V	V	X	X	V	V
664	664	V	V	V	X	X	V	V
665	665	V	V	V	X	X	V	V
666	666	V	V	V	X	X	V	V
667	667	V	V	V	X	X	V	V
668	668	V	V	V	X	X	V	V
669	669	V	V	V	X	X	V	V
670	670	V	V	V	X	X	V	V
671	671	V	V	V	X	X	V	V
672	672	V	V	V	X	X	V	V
673	673	V	V	V	X	X	V	V
674	674	V	V	V	X	X	V	V
675	675	V	V	V	X	X	V	V
676	676	V	V	V	X	X	V	V
677	677	V	V	V	X	X	V	V
678	678	V	V	V	X	X	V	V
679	679	V	V	V	X	X	V	V
680	680	V	V	V	X	X	V	V
681	681	V	V	V	X	X	V	V
682	682	V	V	V	X	X	V	V
683	683	V	V	V	X	X	V	V
684	684	V	V	V	X	X	V	V
685	685	V	V	V	X	X	V	V
686	686	V	V	V	X	X	V	V
687	687	V	V	V	X	X	V	V
688	688	V	V	V	X	X	V	V
689	689	V	V	V	X	X	V	V
690	690	V	V	V	X	X	V	V
691	691	V	V	V	X	X	V	V
692	692	V	V	V	X	X	V	V
693	693	V	V	V	X	X	V	V
694	694	V	V	V	X	X	V	V
695	695	V	V	V	X	X	V	V
696	696	V	V	V	X	X	V	V
697	697	V	V	V	X	X	V	V
698	698	V	V	V	X	X	V	V
699	699	V	V	V	X	X	V	V
700	700	V	V	V	X	X	V	V
701	701	V	V	V	X	X	V	V
702	702	V	V	V	X	X	V	V
703	703	V	V	V	X	X	V	V
704	704	V	V	V	X	X	V	V
705	705	V	V	V	X	X	V	V
706	706	V	V	V	X	X	V	V
707	707	V	V	V	X	X	V	V
708	708	V	V	V	X	X	V	V
709	709	V	V	V	X	X	V	V
710	710	V	V	V	X	X	V	V
711	711	V	V	V	X	X	V	V
712	712	V	V	V	X	X	V	V
713	713	V	V	V	X	X	V	V
714	714	V	V	V	X	X	V	V
715	715	V	V	V	X	X	V	V
716	716	V	V	V	X	X	V	V
717	717	V	V	V	X	X	V	V
718	718	V	V	V	X	X	V	V
719	719	V	V	V	X	X	V	V
720	720	V	V	V	X	X	V	V
721	721	V	V	V	X	X	V	V
722	722	V	V	V	X	X	V	V
723	723	V	V	V	X	X	V	V
724	724	V	V	V	X	X	V	V
725	725	V	V	V	X	X	V	V
726	726	V	V	V	X	X	V	V
727	727	V	V	V	X	X	V	V
728	728	V	V	V	X	X	V	V
729	729	V	V	V	X	X	V	V
730	730	V	V	V	X	X	V	V
731	731	V	V	V	X	X	V	V
732	732	V	V	V	X	X	V	V
733	733	V	V	V	X	X	V	V
734	734	V	V	V	X	X	V	V
735	735	V	V	V	X	X	V	V
736	736	V	V	V	X	X	V	V
737	737	V	V	V	X	X	V	V
738	738	V	V	V	X	X	V	V
739	739	V	V	V	X	X	V	V
740	740	V	V	V	X	X	V	V
741	741	V	V	V	X	X	V	V
742	742	V	V	V	X	X	V	V
743	743	V	V	V	X	X	V	V
744	744	V	V	V	X	X	V	V
745	745	V	V	V	X	X	V	V
746	746	V	V	V	X	X	V	V
747	747	V	V	V	X	X	V	V
748	748	V	V	V	X	X	V	V
749	749	V	V	V	X	X	V	V
750	750	V	V	V	X	X	V	V
751	751	V	V	V	X	X	V	V
752	752	V	V	V	X	X	V	V
753	753	V	V	V	X	X	V	V
754	754	V	V	V	X	X	V	V
755	755	V	V	V	X	X	V	V
756	756	V	V	V	X	X	V	V
757	757	V	V	V	X	X	V	V
758	758	V	V	V	X	X	V	V
759	759	V	V	V	X	X	V	V
760	760	V	V	V	X	X	V	V
761	761	V	V	V	X	X	V	V
762	762	V	V	V	X	X	V	V
763	763	V	V	V	X	X	V	V
764	764	V	V	V	X	X	V	V
765	765	V	V	V	X	X	V	V
766	766	V	V	V	X	X	V	V
767	767	V	V	V	X	X	V	V
768	768	V	V	V	X	X	V	V
769	769	V	V	V	X	X	V	V
770	770	V	V	V	X	X	V	V
771	771	V	V	V	X	X	V	V
772	772	V	V	V	X	X	V	V
773	773	V	V	V	X	X	V	V
774	774	V	V	V	X	X	V	V
775	775	V	V	V	X	X	V	V
776	776	V	V	V	X	X	V	V
777	777	V	V	V	X	X	V	V
778	778	V	V	V	X	X	V	V
779	779	V	V	V	X	X	V	V
780	780	V	V	V	X	X	V	V
781	781	V	V	V	X	X	V	V
782	782	V	V	V	X	X	V	V
783	783	V	V	V	X	X	V	V
784	784	V	V	V	X	X	V	V
785	785	V	V	V	X	X	V	V
786	786	V	V	V	X	X	V	V
787	787	V	V	V	X	X	V	V
788	788	V	V	V	X	X	V	V
789	789	V	V	V	X	X	V	V
790	790	V	V	V	X	X	V	V
791	791	V	V	V	X	X	V	V
792	792	V	V	V	X	X	V	V
793	793	V	V	V	X	X	V	V
794	794	V	V	V	X	X	V	V
795	795	V	V	V	X	X	V	V
796	796	V	V	V	X	X	V	V
797	797	V	V	V	X	X	V	V
798	798	V	V	V	X	X	V	V
799	799	X	X	X	X	X	X	X
800	800	X	X	X	X	X	X	X
801	801	X	X	X	X	X	X	X
802	802	X	X	X	X	X	X	X
803	803	X	X	X	X	X	X	X
804	804	X	X	X	X	X	X	X
805	805	X	X	X	X	X	X	X
806	806	X	X	X	X	X	X	X
807	807	X	X	X	X	X	X	X
808	808	X	X	X	X	X	X	X
809	809	X	X	X	X	X	X	X
810	810	X	X	X	X	X	X	X
811	811	X	X	X	X	X	X	X
812	812	X	X	X	X	X	X	X
813	813	X	X	X	X	X	X	X
814	814	X	X	X	X	X	X	X
815	815	X	X	X	X	X	X	X
816	816	X	X	X	X	X	X	X
817	817	X	X	X	X	X	X	X
818	818	X	X	X	X	X	X	X
819	819	X	X	X	X	X	X	X
820	820	X	X	X	X	X	X	X
821	821	X	X	X	X	X	X	X
822	822	X	X	X	X	X	X	X
823	823	X	X	X	X	X	X	X
824	824	X	X	X	X	X	X	X
825	825	X	X	X	X	X	X	X
826	826	X	X	X	X	X	X	X
827	827	X	X	X	X	X	X	X
828	828	X	X	X	X	X	X	X
829	829	X	X	X	X	X	X	X
830	830	X	X	X	X	X	X	X
831	831	X	X	X	X	X	X	X
832	832	X	X	X	X	X	X	X
833	833	X	X	X	X	X	X	X
834	834	X	X	X	X	X	X	X
835	835	X	X	X	X	X	X	X
836	836	X	X	X	X	X	X	X
837	837	X	X	X	X	X	X	X
838	838	X	X	X	X	X	X	X
839	839	X	X	X	X	X	X	X
840	840	X	X	X	X	X	X	X
841	841	X	X	X	X	X	X	X
842	842	X	X	X	X	X	X	X
843	843	X	X	X	X	X	X	X
844	844	X	X	X	X	X	X	X
845	845	X	X	X	X	X	X	X
846	846	X	X	X	X	X	X	X
847	847	X	X	X	X	X	X	X
848	848	X	X	X	X	X	X	X
849	849	X	X	X	X	X	X	X
850	850	X	X	X	X	X	X	X
851	851	X	X	X	X	X	X	X
852	852	X	X	X	X	X	X	X
853	853	X	X	X	X	X	X	X
854	854	X	X	X	X	X	X	X
855	855	X	X	X	X	X	X	X
856	856	X	X	X	X	X	X	X
857	857	X	X	X	X	X	X	X
858	858	X	X	X	X	X	X	X
859	859	X	X	X	X	X	X	X
860	860	V	V	V	X	X	V	V
861	861	V	V	V	X	X	V	V
862	862	V	V	V	X	X	V	V
863	863	V	V	V	X	X	V	V
864	864	V	V	V	X	X	V	V
865	865	V	V	V	X	X	V	V
866	866	V	V	V	X	X	V	V
867	867	V	V	V	X	X	V	V
868	868	V	V	V	X	X	V	V
869	869	V	V	V	X	X	V	V
870	870	V	V	V	X	X	V	V
871	871	V	V	V	X	X	V	V
872	872	V	V	V	X	X	V	V
873	873	V	V	V	X	X	V	V
874	874	V	V	V	X	X	V	V
875	875	V	V	V	X	X	V	V
876	876	V	V	V	X	X	V	V
877	877	V	V	V	X	X	V	V
878	878	V	V	V	X	X	V	V
879	879	V	V	V	X	X	V	V
880	880	V	V	V	X	X	V	V
881	881	V	V	V	X	X	V	V
882	882	V	V	V	X	X	V	V
883	883	V	V	V	X	X	V	V
884	884	V	V	V	X	X	V	V
885	885	V	V	V	X	X	V	V
886	886	V	V	V	X	X	V	V
887	887	V	V	V	X	X	V	V
888	888	V	V	V	X	X	V	V
889	889	V	V	V	X	X	V	V
890	890	V	V	V	X	X	V	V
891	891	V	V	V	X	X	V	V
892	892	V	V	V	X	X	V	V
893	893	V	V	V	X	X	V	V
894	894	V	V	V	X	X	V	V
895	895	V	V	V	X	X	V	V
896	896	V	V	V	X	X	V	V
897	897	V	V	V	X	X	V	V
898	898	V	V	V	X	X	V	V
899	899	V	V	V	X	X	V	V
900	900	V	V	V	X	X	V	V
901	901	V	V	V	X	X	V	V
902	902	V	V	V	X	X	V	V
903	903	V	V	V	X	X	V	V
904	904	V	V	V	X	X	V	V
905	905	V	V	V	X	X	V	V
906	906	V	V	V	X	X	V	V
907	907	V	V	V	X	X	V	V
908	908	V	V	V	X	X	V	V
909	909	V	V	V	X	X	V	V
910	910	V	V	V	X	X	V	V
911	911	V	V	V	X	X	V	V
912	912	V	V	V	X	X	V	V
913	913	V	V	V	X	X	V	V
914	914	V	V	V	X	X	V	V
915	915	V	V	V	X	X	V	V
916	916	V	V	V	X	X	V	V
917	917	V	V	V	X	X	V	V
918	918	V	V	V	X	X	V	V
919	919	V	V	V	X	X	V	V
920	920	V	V	V	X	X	V	V
921	921	V	V	V	X	X	V	V
922	922	V	V	V	X	X	V	V
923	923	V	V	V	X	X	V	V
924	924	V	V	V	X	X	V	V
925	925	V	V	V	X	X	V	V
926	926	V	V	V	X	X	V	V
927	927	V	V	V	X	X	V	V
928	928	V	V	V	X	X	V	V
929	929	V	V	V	X	X	V	V
930	930	V	V	V	X	X	V	V
931	931	V	V	V	X	X	V	V
932	932	\N	\N	\N	\N	\N	\N	\N
933	933	\N	\N	\N	\N	\N	\N	\N
934	934	\N	\N	\N	\N	\N	\N	\N
935	935	\N	\N	\N	\N	\N	\N	\N
936	936	\N	\N	\N	\N	\N	\N	\N
937	937	\N	\N	\N	\N	\N	\N	\N
938	938	\N	\N	\N	\N	\N	\N	\N
939	939	\N	\N	\N	\N	\N	\N	\N
940	940	\N	\N	\N	\N	\N	\N	\N
941	941	\N	\N	\N	\N	\N	\N	\N
942	942	\N	\N	\N	\N	\N	\N	\N
943	943	\N	\N	\N	\N	\N	\N	\N
944	944	\N	\N	\N	\N	\N	\N	\N
945	945	\N	\N	\N	\N	\N	\N	\N
946	946	\N	\N	\N	\N	\N	\N	\N
947	947	\N	\N	\N	\N	\N	\N	\N
948	948	\N	\N	\N	\N	\N	\N	\N
949	949	\N	\N	\N	\N	\N	\N	\N
950	950	\N	\N	\N	\N	\N	\N	\N
951	951	\N	\N	\N	\N	\N	\N	\N
952	952	\N	\N	\N	\N	\N	\N	\N
953	953	V	V	V	V	V	V	V
954	954	V	V	V	V	V	V	V
955	955	V	V	V	V	V	V	V
956	956	V	V	V	V	V	V	V
957	957	V	V	V	V	V	V	V
958	958	V	V	V	V	V	V	V
959	959	V	V	V	V	V	V	V
960	960	V	V	V	V	V	V	V
961	961	V	V	V	V	V	V	V
962	962	V	V	V	V	V	V	V
963	963	V	V	V	V	V	V	V
964	964	V	V	V	V	V	V	V
965	965	V	V	V	V	V	V	V
966	966	V	V	V	V	V	V	V
967	967	V	V	V	V	V	V	V
968	968	V	V	V	V	V	V	V
969	969	V	V	V	V	V	V	V
970	970	V	V	V	V	V	V	V
971	971	V	V	V	X	X	V	V
972	972	V	V	V	X	X	V	V
973	973	V	V	V	X	X	V	V
974	974	V	V	V	X	X	V	V
975	975	V	V	V	X	X	V	V
976	976	\N	\N	\N	\N	\N	\N	\N
977	977	\N	\N	\N	\N	\N	\N	\N
978	978	\N	\N	\N	\N	\N	\N	\N
979	979	V	V	V	V	V	V	V
980	980	V	V	V	V	V	V	V
981	981	V	V	V	V	V	V	V
982	982	V	V	V	V	V	V	V
983	983	V	V	V	V	V	V	V
984	984	V	V	V	V	V	V	V
985	985	V	V	V	V	V	V	V
986	986	V	V	V	V	V	V	V
987	987	V	V	V	V	V	V	V
988	988	V	V	V	V	V	V	V
989	989	V	V	V	V	V	V	V
990	990	V	V	V	V	V	V	V
991	991	V	V	V	V	V	V	V
992	992	V	V	V	V	V	V	V
993	993	V	V	V	V	V	V	V
994	994	V	V	V	V	V	V	V
995	995	V	V	V	V	V	V	V
996	996	V	V	V	V	V	V	V
997	997	V	V	V	V	V	V	V
998	998	V	V	V	V	V	V	V
999	999	V	V	V	V	V	V	V
1000	1000	V	V	V	V	V	V	V
1001	1001	V	V	V	V	V	V	V
1002	1002	V	V	V	V	V	V	V
1003	1003	V	V	V	V	V	V	V
1004	1004	V	V	V	V	V	V	V
1005	1005	V	V	V	V	V	V	V
1006	1006	V	V	V	V	V	V	V
1007	1007	V	V	V	V	V	V	V
1008	1008	V	V	V	V	V	V	V
1009	1009	V	V	V	V	V	V	V
1010	1010	V	V	V	V	V	V	V
1011	1011	V	V	V	V	V	V	V
1012	1012	V	V	V	V	V	V	V
1013	1013	V	V	V	V	V	V	V
1014	1014	V	V	V	V	V	V	V
1015	1015	V	V	V	V	V	V	V
1016	1016	V	V	V	V	V	V	V
1017	1017	V	V	V	V	V	V	V
\.


--
-- TOC entry 5020 (class 0 OID 16429)
-- Dependencies: 227
-- Data for Name: oil_and_uv_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oil_and_uv_types (id, project_id, oil_6812_glossy_3301_3440_dumb, gv_led_uv_gloss_matte_oil, oil_based_gloss_matte_on_powder_paper, oil_based_gloss_matte_non_powder_paper) FROM stdin;
1	1	\N	\N	\N	\N
2	2	\N	\N	\N	\N
3	3	\N	\N	\N	\N
4	4	\N	\N	\N	\N
5	5	\N	\N	\N	\N
6	6	\N	\N	\N	\N
7	7	\N	\N	\N	\N
8	8	\N	\N	\N	\N
9	9	\N	\N	\N	\N
10	10	\N	\N	\N	\N
11	11	\N	\N	\N	\N
12	12	\N	\N	\N	\N
13	13	\N	\N	\N	\N
14	14	\N	\N	\N	\N
15	15	\N	\N	\N	\N
16	16	\N	\N	\N	\N
17	17	\N	\N	\N	\N
18	18	\N	\N	\N	\N
19	19	\N	\N	\N	\N
20	20	\N	\N	\N	\N
21	21	\N	\N	\N	\N
22	22	\N	\N	\N	\N
23	23	\N	\N	\N	\N
24	24	\N	\N	\N	\N
25	25	\N	\N	\N	\N
26	26	\N	\N	\N	\N
27	27	\N	\N	\N	\N
28	28	\N	\N	\N	\N
29	29	\N	\N	\N	\N
30	30	\N	\N	\N	\N
31	31	\N	\N	\N	\N
32	32	\N	\N	\N	\N
33	33	\N	\N	\N	\N
34	34	\N	\N	\N	\N
35	35	\N	\N	\N	\N
36	36	\N	\N	\N	\N
37	37	\N	\N	\N	\N
38	38	\N	\N	\N	\N
39	39	\N	\N	\N	\N
40	40	\N	\N	\N	\N
41	41	\N	\N	\N	\N
42	42	\N	\N	\N	\N
43	43	\N	\N	\N	\N
44	44	\N	\N	\N	\N
45	45	\N	\N	\N	\N
46	46	\N	\N	\N	\N
47	47	\N	\N	\N	\N
48	48	\N	\N	\N	\N
49	49	\N	\N	\N	\N
50	50	\N	\N	\N	\N
51	51	\N	\N	\N	\N
52	52	\N	\N	\N	\N
53	53	\N	\N	\N	\N
54	54	\N	\N	\N	\N
55	55	\N	\N	\N	\N
56	56	\N	\N	\N	\N
57	57	\N	\N	\N	\N
58	58	\N	\N	\N	\N
59	59	\N	\N	\N	\N
60	60	\N	\N	\N	\N
61	61	V	V	X	V
62	62	V	V	X	V
63	63	\N	\N	\N	\N
64	64	\N	\N	\N	\N
65	65	\N	\N	\N	\N
66	66	\N	\N	\N	\N
67	67	V	V	X	V
68	68	V	V	X	V
69	69	V	V	X	V
70	70	V	V	X	V
71	71	V	V	X	V
72	72	V	V	X	V
73	73	V	V	X	V
74	74	V	V	X	V
75	75	V	V	X	V
76	76	V	V	X	V
77	77	V	V	X	V
78	78	V	V	X	V
79	79	V	V	X	V
80	80	V	V	X	V
81	81	V	V	X	V
82	82	V	V	X	V
83	83	V	V	X	V
84	84	V	V	X	V
85	85	V	V	X	V
86	86	V	V	X	V
87	87	V	V	X	V
88	88	V	V	X	V
89	89	V	V	X	V
90	90	V	V	X	V
91	91	V	V	X	V
92	92	V	V	X	V
93	93	V	V	X	V
94	94	V	V	X	V
95	95	V	V	X	V
96	96	V	V	X	V
97	97	V	V	X	V
98	98	V	V	X	V
99	99	V	V	X	V
100	100	V	V	X	V
101	101	V	V	X	V
102	102	V	V	X	V
103	103	V	V	X	V
104	104	V	V	X	V
105	105	V	V	X	V
106	106	V	V	X	V
107	107	V	V	X	V
108	108	V	V	X	V
109	109	V	V	X	V
110	110	V	V	X	V
111	111	V	V	X	V
112	112	V	V	X	V
113	113	V	V	X	V
114	114	V	V	X	V
115	115	V	V	X	V
116	116	V	V	X	V
117	117	V	V	X	V
118	118	V	V	X	V
119	119	V	V	X	V
120	120	V	V	X	V
121	121	V	V	X	V
122	122	V	V	X	V
123	123	V	V	X	V
124	124	\N	\N	\N	\N
125	125	\N	\N	\N	\N
126	126	\N	\N	\N	\N
127	127	\N	\N	\N	\N
128	128	\N	\N	\N	\N
129	129	\N	\N	\N	\N
130	130	\N	\N	\N	\N
131	131	\N	\N	\N	\N
132	132	\N	\N	\N	\N
133	133	\N	\N	\N	\N
134	134	\N	\N	\N	\N
135	135	\N	\N	\N	\N
136	136	\N	\N	\N	\N
137	137	\N	\N	\N	\N
138	138	\N	\N	\N	\N
139	139	\N	\N	\N	\N
140	140	\N	\N	\N	\N
141	141	\N	\N	\N	\N
142	142	\N	\N	\N	\N
143	143	\N	\N	\N	\N
144	144	\N	\N	\N	\N
145	145	\N	\N	\N	\N
146	146	\N	\N	\N	\N
147	147	\N	\N	\N	\N
148	148	\N	\N	\N	\N
149	149	\N	\N	\N	\N
150	150	\N	\N	\N	\N
151	151	\N	\N	\N	\N
152	152	\N	\N	\N	\N
153	153	V	V	X	V
154	154	V	V	X	V
155	155	V	V	X	V
156	156	V	V	X	V
157	157	V	V	X	V
158	158	V	V	X	V
159	159	V	V	X	V
160	160	V	V	X	V
161	161	V	V	X	V
162	162	V	V	X	V
163	163	V	V	X	V
164	164	V	V	X	V
165	165	V	V	X	V
166	166	V	V	X	V
167	167	V	V	X	V
168	168	V	V	X	V
169	169	V	V	X	V
170	170	V	V	X	V
171	171	V	V	X	V
172	172	V	V	X	V
173	173	V	V	X	V
174	174	V	V	X	V
175	175	V	V	X	V
176	176	\N	\N	\N	\N
177	177	\N	\N	\N	\N
178	178	\N	\N	\N	\N
179	179	\N	\N	\N	\N
180	180	V	V	X	V
181	181	V	V	X	V
182	182	V	V	X	V
183	183	V	V	X	V
184	184	V	V	X	V
185	185	V	V	X	V
186	186	V	V	X	V
187	187	V	V	X	V
188	188	V	V	X	V
189	189	V	V	X	V
190	190	V	V	X	V
191	191	V	V	X	V
192	192	V	V	X	V
193	193	V	V	X	V
194	194	V	V	X	V
195	195	V	V	X	V
196	196	V	V	X	V
197	197	V	V	X	V
198	198	V	V	X	V
199	199	V	V	X	V
200	200	V	V	X	V
201	201	V	V	X	V
202	202	V	V	X	V
203	203	V	V	X	V
204	204	V	V	X	V
205	205	V	V	X	V
206	206	V	V	X	V
207	207	V	V	X	V
208	208	V	V	X	V
209	209	V	V	X	V
210	210	V	V	X	V
211	211	V	V	X	V
212	212	V	V	X	V
213	213	V	V	X	V
214	214	V	V	X	V
215	215	V	V	X	V
216	216	V	V	X	V
217	217	V	V	X	V
218	218	V	V	X	V
219	219	V	V	X	V
220	220	V	V	X	V
221	221	V	V	X	V
222	222	V	V	X	V
223	223	V	V	X	V
224	224	V	V	X	V
225	225	\N	\N	\N	\N
226	226	\N	\N	\N	\N
227	227	\N	\N	\N	\N
228	228	\N	\N	\N	\N
229	229	\N	\N	\N	\N
230	230	\N	\N	\N	\N
231	231	\N	\N	\N	\N
232	232	\N	\N	\N	\N
233	233	\N	\N	\N	\N
234	234	\N	\N	\N	\N
235	235	\N	\N	\N	\N
236	236	\N	\N	\N	\N
237	237	\N	\N	\N	\N
238	238	\N	\N	\N	\N
239	239	\N	\N	\N	\N
240	240	\N	\N	\N	\N
241	241	\N	\N	\N	\N
242	242	\N	\N	\N	\N
243	243	\N	\N	\N	\N
244	244	V	V	X	V
245	245	V	V	X	V
246	246	V	V	X	V
247	247	V	V	X	V
248	248	V	V	X	V
249	249	V	V	X	V
250	250	V	V	X	V
251	251	V	V	X	V
252	252	V	V	X	V
253	253	V	V	X	V
254	254	V	V	X	V
255	255	V	V	X	V
256	256	V	V	X	V
257	257	V	V	X	V
258	258	V	V	X	V
259	259	V	V	X	V
260	260	V	V	X	V
261	261	V	V	X	V
262	262	V	V	X	V
263	263	V	V	X	V
264	264	V	V	X	V
265	265	V	V	X	V
266	266	V	V	X	V
267	267	V	V	X	V
268	268	V	V	X	V
269	269	V	V	X	V
270	270	V	V	X	V
271	271	\N	\N	\N	\N
272	272	\N	\N	\N	\N
273	273	\N	\N	\N	\N
274	274	\N	\N	\N	\N
275	275	\N	\N	\N	\N
276	276	\N	\N	\N	\N
277	277	\N	\N	\N	\N
278	278	\N	\N	\N	\N
279	279	\N	\N	\N	\N
280	280	\N	\N	\N	\N
281	281	\N	\N	\N	\N
282	282	\N	\N	\N	\N
283	283	\N	\N	\N	\N
284	284	\N	\N	\N	\N
285	285	V	V	X	V
286	286	V	V	X	V
287	287	V	V	X	V
288	288	V	V	X	V
289	289	V	V	X	V
290	290	V	V	X	V
291	291	V	V	X	V
292	292	V	V	X	V
293	293	V	V	X	V
294	294	V	V	X	V
295	295	V	V	X	V
296	296	V	V	X	V
297	297	V	V	X	V
298	298	V	V	X	V
299	299	V	V	X	V
300	300	V	V	X	V
301	301	V	V	X	V
302	302	V	V	X	V
303	303	V	V	X	V
304	304	V	V	X	V
305	305	V	V	X	V
306	306	V	V	X	V
307	307	V	V	X	V
308	308	V	V	X	V
309	309	V	V	X	V
310	310	V	V	X	V
311	311	V	V	X	V
312	312	V	V	X	V
313	313	V	V	X	V
314	314	V	V	X	V
315	315	V	V	X	V
316	316	V	V	X	V
317	317	V	V	X	V
318	318	V	V	X	V
319	319	V	V	X	V
320	320	V	V	X	V
321	321	V	V	X	V
322	322	V	V	X	V
323	323	V	V	X	V
324	324	V	V	X	V
325	325	V	V	X	V
326	326	V	V	X	V
327	327	V	V	X	V
328	328	V	V	X	V
329	329	V	V	X	V
330	330	V	V	X	V
331	331	V	V	X	V
332	332	V	V	X	V
333	333	V	V	X	V
334	334	V	V	X	V
335	335	V	V	X	V
336	336	V	V	X	V
337	337	V	V	X	V
338	338	V	V	X	V
339	339	V	V	X	V
340	340	V	V	X	V
341	341	V	V	X	V
342	342	V	V	X	V
343	343	V	V	X	V
344	344	V	V	X	V
345	345	V	V	X	V
346	346	V	V	X	V
347	347	V	V	X	V
348	348	V	V	X	V
349	349	V	V	X	V
350	350	V	V	X	V
351	351	V	V	X	V
352	352	V	V	X	V
353	353	V	V	X	V
354	354	V	V	X	V
355	355	V	V	X	V
356	356	V	V	X	V
357	357	V	V	X	V
358	358	V	V	X	V
359	359	V	V	X	V
360	360	V	V	X	V
361	361	V	V	X	V
362	362	V	V	X	V
363	363	V	V	X	V
364	364	V	V	X	V
365	365	V	V	X	V
366	366	V	V	X	V
367	367	V	V	X	V
368	368	V	V	X	V
369	369	V	V	X	V
370	370	V	V	X	V
371	371	V	V	X	V
372	372	V	V	X	V
373	373	V	V	X	V
374	374	V	V	X	V
375	375	V	V	X	V
376	376	V	V	X	V
377	377	V	V	X	V
378	378	V	V	X	V
379	379	V	V	X	V
380	380	V	V	X	V
381	381	V	V	X	V
382	382	V	V	X	V
383	383	V	V	X	V
384	384	V	V	X	V
385	385	V	V	X	V
386	386	V	V	X	V
387	387	V	V	X	V
388	388	V	V	X	V
389	389	V	V	X	V
390	390	V	V	X	V
391	391	V	V	X	V
392	392	V	V	X	V
393	393	V	V	X	V
394	394	V	V	X	V
395	395	V	V	X	V
396	396	V	V	X	V
397	397	V	V	X	V
398	398	V	V	X	V
399	399	V	V	X	V
400	400	V	V	X	V
401	401	\N	\N	\N	\N
402	402	\N	\N	\N	\N
403	403	\N	\N	\N	\N
404	404	\N	\N	\N	\N
405	405	V	V	X	V
406	406	V	V	X	V
407	407	V	V	X	V
408	408	V	V	X	V
409	409	V	V	X	V
410	410	V	V	X	V
411	411	V	V	X	V
412	412	V	V	X	V
413	413	V	V	X	V
414	414	V	V	X	V
415	415	V	V	X	V
416	416	V	V	X	V
417	417	V	V	X	V
418	418	V	V	X	V
419	419	V	V	X	V
420	420	V	V	X	V
421	421	V	V	X	V
422	422	V	V	X	V
423	423	V	V	X	V
424	424	V	V	X	V
425	425	V	V	X	V
426	426	V	V	X	V
427	427	V	V	X	V
428	428	V	V	X	V
429	429	V	V	X	V
430	430	V	V	X	V
431	431	V	V	X	V
432	432	V	V	X	V
433	433	V	V	X	V
434	434	V	V	X	V
435	435	V	V	X	V
436	436	V	V	X	V
437	437	V	V	X	V
438	438	V	V	X	V
439	439	V	V	X	V
440	440	V	V	X	V
441	441	V	V	X	V
442	442	V	V	X	V
443	443	V	V	X	V
444	444	V	V	X	V
445	445	V	V	X	V
446	446	V	V	X	V
447	447	V	V	X	V
448	448	V	V	X	V
449	449	V	V	X	V
450	450	V	V	X	V
451	451	V	V	X	V
452	452	V	V	X	V
453	453	V	V	X	V
454	454	V	V	X	V
455	455	V	V	X	V
456	456	V	V	X	V
457	457	V	V	X	V
458	458	V	V	X	V
459	459	V	V	X	V
460	460	V	V	X	V
461	461	\N	\N	\N	\N
462	462	V	V	X	V
463	463	V	V	X	V
464	464	V	V	X	V
465	465	V	V	X	V
466	466	V	V	X	V
467	467	V	V	X	V
468	468	V	V	X	V
469	469	V	V	X	V
470	470	V	V	X	V
471	471	V	V	X	V
472	472	V	V	X	V
473	473	V	V	X	V
474	474	V	V	X	V
475	475	V	V	X	V
476	476	V	V	X	V
477	477	V	V	X	V
478	478	V	V	X	V
479	479	V	V	X	V
480	480	V	V	X	V
481	481	V	V	X	V
482	482	V	V	X	V
483	483	V	V	X	V
484	484	V	V	X	V
485	485	V	V	X	V
486	486	V	V	X	V
487	487	V	V	X	V
488	488	V	V	X	V
489	489	V	V	X	V
490	490	V	V	X	V
491	491	V	V	X	V
492	492	V	V	X	V
493	493	V	V	X	V
494	494	V	V	X	V
495	495	V	V	X	V
496	496	V	V	X	V
497	497	V	V	X	V
498	498	V	V	X	V
499	499	V	V	X	V
500	500	V	V	X	V
501	501	V	V	X	V
502	502	V	V	X	V
503	503	V	V	X	V
504	504	V	V	X	V
505	505	V	V	X	V
506	506	V	V	X	V
507	507	V	V	X	V
508	508	V	V	X	V
509	509	V	V	X	V
510	510	V	V	X	V
511	511	V	V	X	V
512	512	V	V	X	V
513	513	V	V	X	V
514	514	V	V	X	V
515	515	V	V	X	V
516	516	V	V	X	V
517	517	V	V	X	V
518	518	V	V	X	V
519	519	V	V	X	V
520	520	V	V	X	V
521	521	V	V	X	V
522	522	V	V	X	V
523	523	V	V	X	V
524	524	V	V	X	V
525	525	V	V	X	V
526	526	V	V	X	V
527	527	V	V	X	V
528	528	V	V	X	V
529	529	V	V	X	V
530	530	V	V	X	V
531	531	V	V	X	V
532	532	V	V	X	V
533	533	V	V	X	V
534	534	V	V	X	V
535	535	V	V	X	V
536	536	V	V	X	V
537	537	V	V	X	V
538	538	V	V	X	V
539	539	V	V	X	V
540	540	V	V	X	V
541	541	V	V	X	V
542	542	V	V	X	V
543	543	V	V	X	V
544	544	V	V	X	V
545	545	V	V	X	V
546	546	V	V	X	V
547	547	V	V	X	V
548	548	V	V	X	V
549	549	V	V	X	V
550	550	V	V	X	V
551	551	V	V	X	V
552	552	V	V	X	V
553	553	V	V	X	V
554	554	V	V	X	V
555	555	V	V	X	V
556	556	V	V	X	V
557	557	V	V	X	V
558	558	V	V	X	V
559	559	V	V	X	V
560	560	V	V	X	V
561	561	V	V	X	V
562	562	V	V	X	V
563	563	V	V	X	V
564	564	V	V	X	V
565	565	V	V	X	V
566	566	V	V	X	V
567	567	V	V	X	V
568	568	V	V	X	V
569	569	V	V	X	V
570	570	V	V	X	V
571	571	V	V	X	V
572	572	V	V	X	V
573	573	V	V	X	V
574	574	V	V	X	V
575	575	V	V	X	V
576	576	V	V	X	V
577	577	V	V	X	V
578	578	V	V	X	V
579	579	V	V	X	V
580	580	V	V	X	V
581	581	V	V	X	V
582	582	V	V	X	V
583	583	V	V	X	V
584	584	V	V	X	V
585	585	V	V	X	V
586	586	V	V	X	V
587	587	V	V	X	V
588	588	V	V	X	V
589	589	V	V	X	V
590	590	V	V	X	V
591	591	V	V	X	V
592	592	V	V	X	V
593	593	V	V	X	V
594	594	V	V	X	V
595	595	V	V	X	V
596	596	V	V	X	V
597	597	V	V	X	V
598	598	V	V	X	V
599	599	V	V	X	V
600	600	V	V	X	V
601	601	V	V	X	V
602	602	V	V	X	V
603	603	V	V	X	V
604	604	V	V	X	V
605	605	V	V	X	V
606	606	V	V	X	V
607	607	V	V	X	V
608	608	V	V	X	V
609	609	V	V	X	V
610	610	V	V	X	V
611	611	V	V	X	V
612	612	V	V	X	V
613	613	V	V	X	V
614	614	V	V	X	V
615	615	V	V	X	V
616	616	V	V	X	V
617	617	V	V	X	V
618	618	V	V	X	V
619	619	V	V	X	V
620	620	V	V	X	V
621	621	V	V	X	V
622	622	V	V	X	V
623	623	V	V	X	V
624	624	V	V	X	V
625	625	V	V	X	V
626	626	V	V	X	V
627	627	V	V	X	V
628	628	V	V	X	V
629	629	V	V	X	V
630	630	V	V	X	V
631	631	V	V	X	V
632	632	V	V	X	V
633	633	V	V	X	V
634	634	V	V	X	V
635	635	V	V	X	V
636	636	V	V	X	V
637	637	V	V	X	V
638	638	V	V	X	V
639	639	V	V	X	V
640	640	V	V	X	V
641	641	V	V	X	V
642	642	V	V	X	V
643	643	V	V	X	V
644	644	V	V	X	V
645	645	V	V	X	V
646	646	V	V	X	V
647	647	\N	\N	\N	\N
648	648	\N	\N	\N	\N
649	649	\N	\N	\N	\N
650	650	\N	\N	\N	\N
651	651	\N	\N	\N	\N
652	652	\N	\N	\N	\N
653	653	\N	\N	\N	\N
654	654	\N	\N	\N	\N
655	655	\N	\N	\N	\N
656	656	\N	\N	\N	\N
657	657	\N	\N	\N	\N
658	658	\N	\N	\N	\N
659	659	\N	\N	\N	\N
660	660	\N	\N	\N	\N
661	661	V	V	X	V
662	662	V	V	X	V
663	663	V	V	X	V
664	664	V	V	X	V
665	665	V	V	X	V
666	666	V	V	X	V
667	667	V	V	X	V
668	668	V	V	X	V
669	669	V	V	X	V
670	670	V	V	X	V
671	671	V	V	X	V
672	672	V	V	X	V
673	673	V	V	X	V
674	674	V	V	X	V
675	675	V	V	X	V
676	676	V	V	X	V
677	677	V	V	X	V
678	678	V	V	X	V
679	679	V	V	X	V
680	680	V	V	X	V
681	681	V	V	X	V
682	682	V	V	X	V
683	683	V	V	X	V
684	684	V	V	X	V
685	685	V	V	X	V
686	686	V	V	X	V
687	687	V	V	X	V
688	688	V	V	X	V
689	689	V	V	X	V
690	690	V	V	X	V
691	691	V	V	X	V
692	692	V	V	X	V
693	693	V	V	X	V
694	694	V	V	X	V
695	695	V	V	X	V
696	696	V	V	X	V
697	697	V	V	X	V
698	698	V	V	X	V
699	699	V	V	X	V
700	700	V	V	X	V
701	701	V	V	X	V
702	702	V	V	X	V
703	703	V	V	X	V
704	704	V	V	X	V
705	705	V	V	X	V
706	706	V	V	X	V
707	707	V	V	X	V
708	708	V	V	X	V
709	709	V	V	X	V
710	710	V	V	X	V
711	711	V	V	X	V
712	712	V	V	X	V
713	713	V	V	X	V
714	714	V	V	X	V
715	715	V	V	X	V
716	716	V	V	X	V
717	717	V	V	X	V
718	718	V	V	X	V
719	719	V	V	X	V
720	720	V	V	X	V
721	721	V	V	X	V
722	722	V	V	X	V
723	723	V	V	X	V
724	724	V	V	X	V
725	725	V	V	X	V
726	726	V	V	X	V
727	727	V	V	X	V
728	728	V	V	X	V
729	729	V	V	X	V
730	730	V	V	X	V
731	731	V	V	X	V
732	732	V	V	X	V
733	733	V	V	X	V
734	734	V	V	X	V
735	735	V	V	X	V
736	736	V	V	X	V
737	737	V	V	X	V
738	738	V	V	X	V
739	739	V	V	X	V
740	740	V	V	X	V
741	741	V	V	X	V
742	742	V	V	X	V
743	743	V	V	X	V
744	744	V	V	X	V
745	745	V	V	X	V
746	746	V	V	X	V
747	747	V	V	X	V
748	748	V	V	X	V
749	749	V	V	X	V
750	750	V	V	X	V
751	751	V	V	X	V
752	752	V	V	X	V
753	753	V	V	X	V
754	754	V	V	X	V
755	755	V	V	X	V
756	756	V	V	X	V
757	757	V	V	X	V
758	758	V	V	X	V
759	759	V	V	X	V
760	760	V	V	X	V
761	761	V	V	X	V
762	762	V	V	X	V
763	763	V	V	X	V
764	764	V	V	X	V
765	765	V	V	X	V
766	766	V	V	X	V
767	767	V	V	X	V
768	768	V	V	X	V
769	769	V	V	X	V
770	770	V	V	X	V
771	771	V	V	X	V
772	772	V	V	X	V
773	773	V	V	X	V
774	774	V	V	X	V
775	775	V	V	X	V
776	776	V	V	X	V
777	777	V	V	X	V
778	778	V	V	X	V
779	779	V	V	X	V
780	780	V	V	X	V
781	781	V	V	X	V
782	782	V	V	X	V
783	783	V	V	X	V
784	784	V	V	X	V
785	785	V	V	X	V
786	786	V	V	X	V
787	787	V	V	X	V
788	788	V	V	X	V
789	789	V	V	X	V
790	790	V	V	X	V
791	791	V	V	X	V
792	792	V	V	X	V
793	793	V	V	X	V
794	794	V	V	X	V
795	795	V	V	X	V
796	796	V	V	X	V
797	797	V	V	X	V
798	798	V	V	X	V
799	799	V	V	X	V
800	800	V	V	X	V
801	801	V	V	X	V
802	802	V	V	X	V
803	803	V	V	X	V
804	804	V	V	X	V
805	805	V	V	X	V
806	806	V	V	X	V
807	807	V	V	X	V
808	808	V	V	X	V
809	809	V	V	X	V
810	810	V	V	X	V
811	811	V	V	X	V
812	812	V	V	X	V
813	813	V	V	X	V
814	814	V	V	X	V
815	815	V	V	X	V
816	816	V	V	X	V
817	817	V	V	X	V
818	818	V	V	X	V
819	819	V	V	X	V
820	820	V	V	X	V
821	821	V	V	X	V
822	822	V	V	X	V
823	823	V	V	X	V
824	824	V	V	X	V
825	825	V	V	X	V
826	826	V	V	X	V
827	827	V	V	X	V
828	828	V	V	X	V
829	829	V	V	X	V
830	830	V	V	X	V
831	831	V	V	X	V
832	832	V	V	X	V
833	833	V	V	X	V
834	834	V	V	X	V
835	835	V	V	X	V
836	836	V	V	X	V
837	837	V	V	X	V
838	838	V	V	X	V
839	839	V	V	X	V
840	840	V	V	X	V
841	841	V	V	X	V
842	842	V	V	X	V
843	843	V	V	X	V
844	844	V	V	X	V
845	845	V	V	X	V
846	846	V	V	X	V
847	847	V	V	X	V
848	848	V	V	X	V
849	849	V	V	X	V
850	850	V	V	X	V
851	851	V	V	X	V
852	852	V	V	X	V
853	853	V	V	X	V
854	854	V	V	X	V
855	855	V	V	X	V
856	856	V	V	X	V
857	857	V	V	X	V
858	858	V	V	X	V
859	859	V	V	X	V
860	860	V	V	X	V
861	861	V	V	X	V
862	862	V	V	X	V
863	863	V	V	X	V
864	864	V	V	X	V
865	865	V	V	X	V
866	866	V	V	X	V
867	867	V	V	X	V
868	868	V	V	X	V
869	869	V	V	X	V
870	870	V	V	X	V
871	871	V	V	X	V
872	872	V	V	X	V
873	873	V	V	X	V
874	874	V	V	X	V
875	875	V	V	X	V
876	876	V	V	X	V
877	877	V	V	X	V
878	878	V	V	X	V
879	879	V	V	X	V
880	880	V	V	X	V
881	881	V	V	X	V
882	882	V	V	X	V
883	883	V	V	X	V
884	884	V	V	X	V
885	885	V	V	X	V
886	886	V	V	X	V
887	887	V	V	X	V
888	888	V	V	X	V
889	889	V	V	X	V
890	890	V	V	X	V
891	891	V	V	X	V
892	892	V	V	X	V
893	893	V	V	X	V
894	894	V	V	X	V
895	895	V	V	X	V
896	896	V	V	X	V
897	897	V	V	X	V
898	898	V	V	X	V
899	899	V	V	X	V
900	900	V	V	X	V
901	901	V	V	X	V
902	902	V	V	X	V
903	903	V	V	X	V
904	904	V	V	X	V
905	905	V	V	X	V
906	906	V	V	X	V
907	907	V	V	X	V
908	908	V	V	X	V
909	909	V	V	X	V
910	910	V	V	X	V
911	911	V	V	X	V
912	912	V	V	X	V
913	913	V	V	X	V
914	914	V	V	X	V
915	915	V	V	X	V
916	916	V	V	X	V
917	917	V	V	X	V
918	918	V	V	X	V
919	919	V	V	X	V
920	920	V	V	X	V
921	921	V	V	X	V
922	922	V	V	X	V
923	923	V	V	X	V
924	924	V	V	X	V
925	925	V	V	X	V
926	926	V	V	X	V
927	927	V	V	X	V
928	928	V	V	X	V
929	929	V	V	X	V
930	930	V	V	X	V
931	931	V	V	X	V
932	932	\N	\N	\N	\N
933	933	\N	\N	\N	\N
934	934	\N	\N	\N	\N
935	935	\N	\N	\N	\N
936	936	\N	\N	\N	\N
937	937	\N	\N	\N	\N
938	938	\N	\N	\N	\N
939	939	\N	\N	\N	\N
940	940	\N	\N	\N	\N
941	941	\N	\N	\N	\N
942	942	\N	\N	\N	\N
943	943	\N	\N	\N	\N
944	944	\N	\N	\N	\N
945	945	\N	\N	\N	\N
946	946	\N	\N	\N	\N
947	947	\N	\N	\N	\N
948	948	\N	\N	\N	\N
949	949	\N	\N	\N	\N
950	950	\N	\N	\N	\N
951	951	\N	\N	\N	\N
952	952	\N	\N	\N	\N
953	953	V	V	X	V
954	954	V	V	X	V
955	955	V	V	X	V
956	956	V	V	X	V
957	957	V	V	X	V
958	958	V	V	X	V
959	959	V	V	X	V
960	960	V	V	X	V
961	961	V	V	X	V
962	962	V	V	X	V
963	963	V	V	X	V
964	964	V	V	X	V
965	965	V	V	X	V
966	966	V	V	X	V
967	967	V	V	X	V
968	968	V	V	X	V
969	969	V	V	X	V
970	970	V	V	X	V
971	971	V	V	X	V
972	972	V	V	X	V
973	973	V	V	X	V
974	974	V	V	X	V
975	975	V	V	X	V
976	976	\N	\N	\N	\N
977	977	\N	\N	\N	\N
978	978	\N	\N	\N	\N
979	979	V	V	X	V
980	980	V	V	X	V
981	981	V	V	X	V
982	982	V	V	X	V
983	983	V	V	X	V
984	984	V	V	X	V
985	985	V	V	X	V
986	986	V	V	X	V
987	987	V	V	X	V
988	988	V	V	X	V
989	989	V	V	X	V
990	990	V	V	X	V
991	991	V	V	X	V
992	992	V	V	X	V
993	993	V	V	X	V
994	994	V	V	X	V
995	995	V	V	X	V
996	996	V	V	X	V
997	997	V	V	X	V
998	998	V	V	X	V
999	999	V	V	X	V
1000	1000	V	V	X	V
1001	1001	V	V	X	V
1002	1002	V	V	X	V
1003	1003	V	V	X	V
1004	1004	V	V	X	V
1005	1005	V	V	X	V
1006	1006	V	V	X	V
1007	1007	V	V	X	V
1008	1008	V	V	X	V
1009	1009	V	V	X	V
1010	1010	V	V	X	V
1011	1011	V	V	X	V
1012	1012	V	V	X	V
1013	1013	V	V	X	V
1014	1014	V	V	X	V
1015	1015	V	V	X	V
1016	1016	V	V	X	V
1017	1017	V	V	X	V
\.


--
-- TOC entry 5022 (class 0 OID 16433)
-- Dependencies: 229
-- Data for Name: pattern; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pattern (id, project_id, gradient_halftone, thin_lines_letters, medium_small_thin_lines_letters, medium_large_area_fine_lines_letters, extra_large_area) FROM stdin;
1	1	\N	\N	\N	\N	\N
2	2	\N	\N	\N	\N	\N
3	3	\N	\N	\N	\N	\N
4	4	\N	\N	\N	\N	\N
5	5	\N	\N	\N	\N	\N
6	6	\N	\N	\N	\N	\N
7	7	\N	\N	\N	\N	\N
8	8	\N	\N	\N	\N	\N
9	9	\N	\N	\N	\N	\N
10	10	\N	\N	\N	\N	\N
11	11	\N	\N	\N	\N	\N
12	12	\N	\N	\N	\N	\N
13	13	\N	\N	\N	\N	\N
14	14	\N	\N	\N	\N	\N
15	15	\N	\N	\N	\N	\N
16	16	\N	\N	\N	\N	\N
17	17	\N	\N	\N	\N	\N
18	18	\N	\N	\N	\N	\N
19	19	\N	\N	\N	\N	\N
20	20	\N	\N	\N	\N	\N
21	21	\N	\N	\N	\N	\N
22	22	\N	\N	\N	\N	\N
23	23	\N	\N	\N	\N	\N
24	24	\N	\N	\N	\N	\N
25	25	\N	\N	\N	\N	\N
26	26	\N	\N	\N	\N	\N
27	27	\N	\N	\N	\N	\N
28	28	\N	\N	\N	\N	\N
29	29	\N	\N	\N	\N	\N
30	30	\N	\N	\N	\N	\N
31	31	\N	\N	\N	\N	\N
32	32	\N	\N	\N	\N	\N
33	33	\N	\N	\N	\N	\N
34	34	\N	\N	\N	\N	\N
35	35	\N	\N	\N	\N	\N
36	36	\N	\N	\N	\N	\N
37	37	\N	\N	\N	\N	\N
38	38	\N	\N	\N	\N	\N
39	39	\N	\N	\N	\N	\N
40	40	\N	\N	\N	\N	\N
41	41	\N	\N	\N	\N	\N
42	42	\N	\N	\N	\N	\N
43	43	\N	\N	\N	\N	\N
44	44	\N	\N	\N	\N	\N
45	45	\N	\N	\N	\N	\N
46	46	\N	\N	\N	\N	\N
47	47	\N	\N	\N	\N	\N
48	48	\N	\N	\N	\N	\N
49	49	\N	\N	\N	\N	\N
50	50	\N	\N	\N	\N	\N
51	51	\N	\N	\N	\N	\N
52	52	\N	\N	\N	\N	\N
53	53	\N	\N	\N	\N	\N
54	54	\N	\N	\N	\N	\N
55	55	\N	\N	\N	\N	\N
56	56	\N	\N	\N	\N	\N
57	57	\N	\N	\N	\N	\N
58	58	\N	\N	\N	\N	\N
59	59	\N	\N	\N	\N	\N
60	60	\N	\N	\N	\N	\N
61	61	X	X	V	V	X
62	62	X	X	V	V	X
63	63	\N	\N	\N	\N	\N
64	64	\N	\N	\N	\N	\N
65	65	\N	\N	\N	\N	\N
66	66	\N	\N	\N	\N	\N
67	67	X	X	V	X	X
68	68	X	X	V	X	X
69	69	X	X	V	X	X
70	70	X	X	V	X	X
71	71	X	X	V	X	X
72	72	X	X	V	X	X
73	73	X	X	V	X	X
74	74	X	X	V	X	X
75	75	X	X	V	X	X
76	76	X	X	V	X	X
77	77	X	X	V	X	X
78	78	X	X	V	X	X
79	79	X	X	V	X	X
80	80	X	X	V	X	X
81	81	X	X	V	X	X
82	82	X	X	V	X	X
83	83	X	X	V	X	X
84	84	X	X	V	X	X
85	85	X	X	V	X	X
86	86	X	X	V	X	X
87	87	X	X	V	X	X
88	88	X	X	V	X	X
89	89	X	X	V	X	X
90	90	X	X	V	X	X
91	91	X	X	V	X	X
92	92	X	X	V	X	X
93	93	X	X	V	X	X
94	94	X	X	V	X	X
95	95	X	X	V	X	X
96	96	X	X	V	X	X
97	97	X	X	V	X	X
98	98	X	X	V	X	X
99	99	X	X	V	X	X
100	100	X	X	V	X	X
101	101	X	X	V	X	X
102	102	X	X	V	X	X
103	103	X	X	V	X	X
104	104	X	X	V	X	X
105	105	X	NA_Enum	V	NA_Enum	NA_Enum
106	106	X	NA_Enum	V	NA_Enum	NA_Enum
107	107	X	NA_Enum	V	NA_Enum	NA_Enum
108	108	X	NA_Enum	V	NA_Enum	NA_Enum
109	109	X	NA_Enum	V	NA_Enum	NA_Enum
110	110	X	NA_Enum	V	NA_Enum	NA_Enum
111	111	X	NA_Enum	V	NA_Enum	NA_Enum
112	112	X	NA_Enum	V	NA_Enum	NA_Enum
113	113	X	NA_Enum	V	NA_Enum	NA_Enum
114	114	X	NA_Enum	V	NA_Enum	NA_Enum
115	115	X	NA_Enum	V	NA_Enum	NA_Enum
116	116	X	NA_Enum	V	NA_Enum	NA_Enum
117	117	X	NA_Enum	V	NA_Enum	NA_Enum
118	118	X	NA_Enum	V	NA_Enum	NA_Enum
119	119	X	NA_Enum	V	NA_Enum	NA_Enum
120	120	X	NA_Enum	V	NA_Enum	NA_Enum
121	121	X	NA_Enum	V	NA_Enum	NA_Enum
122	122	X	NA_Enum	V	NA_Enum	NA_Enum
123	123	X	NA_Enum	V	NA_Enum	NA_Enum
124	124	\N	\N	\N	\N	\N
125	125	\N	\N	\N	\N	\N
126	126	\N	\N	\N	\N	\N
127	127	\N	\N	\N	\N	\N
128	128	\N	\N	\N	\N	\N
129	129	\N	\N	\N	\N	\N
130	130	\N	\N	\N	\N	\N
131	131	\N	\N	\N	\N	\N
132	132	\N	\N	\N	\N	\N
133	133	\N	\N	\N	\N	\N
134	134	\N	\N	\N	\N	\N
135	135	\N	\N	\N	\N	\N
136	136	\N	\N	\N	\N	\N
137	137	\N	\N	\N	\N	\N
138	138	\N	\N	\N	\N	\N
139	139	\N	\N	\N	\N	\N
140	140	\N	\N	\N	\N	\N
141	141	\N	\N	\N	\N	\N
142	142	\N	\N	\N	\N	\N
143	143	\N	\N	\N	\N	\N
144	144	\N	\N	\N	\N	\N
145	145	\N	\N	\N	\N	\N
146	146	\N	\N	\N	\N	\N
147	147	\N	\N	\N	\N	\N
148	148	\N	\N	\N	\N	\N
149	149	\N	\N	\N	\N	\N
150	150	\N	\N	\N	\N	\N
151	151	\N	\N	\N	\N	\N
152	152	\N	\N	\N	\N	\N
153	153	X	X	V	X	X
154	154	X	X	V	X	X
155	155	X	X	V	X	X
156	156	X	X	V	X	X
157	157	X	X	V	X	X
158	158	X	X	V	X	X
159	159	X	X	V	X	X
160	160	X	X	V	X	X
161	161	X	X	V	X	X
162	162	X	X	V	X	X
163	163	X	X	V	X	X
164	164	X	X	V	X	X
165	165	X	X	V	X	X
166	166	X	X	V	X	X
167	167	X	X	V	X	X
168	168	X	X	X	V	X
169	169	X	X	X	V	X
170	170	X	X	X	V	X
171	171	X	X	X	V	X
172	172	X	X	X	V	X
173	173	X	X	X	V	X
174	174	X	X	X	V	X
175	175	X	X	X	V	X
176	176	\N	\N	\N	\N	\N
177	177	\N	\N	\N	\N	\N
178	178	\N	\N	\N	\N	\N
179	179	\N	\N	\N	\N	\N
180	180	X	X	V	X	X
181	181	X	X	V	X	X
182	182	X	X	V	X	X
183	183	X	X	V	X	X
184	184	X	X	V	X	X
185	185	X	X	V	X	X
186	186	X	X	V	X	X
187	187	X	X	V	X	X
188	188	X	X	V	X	X
189	189	X	X	V	X	X
190	190	X	X	V	X	X
191	191	X	X	V	X	X
192	192	X	X	V	X	X
193	193	X	X	V	X	X
194	194	X	X	V	X	X
195	195	X	X	V	X	X
196	196	X	X	V	X	X
197	197	X	X	V	X	X
198	198	X	X	V	X	X
199	199	X	X	V	X	X
200	200	X	X	V	X	X
201	201	X	X	V	X	X
202	202	X	X	V	X	X
203	203	X	X	V	X	X
204	204	X	X	V	X	X
205	205	X	X	V	X	X
206	206	X	X	V	X	X
207	207	X	X	V	X	X
208	208	X	X	V	X	X
209	209	X	X	V	X	X
210	210	X	X	V	X	X
211	211	X	X	V	X	X
212	212	X	X	V	X	X
213	213	X	X	V	X	X
214	214	X	X	V	X	X
215	215	X	X	V	X	X
216	216	X	NA_Enum	V	NA_Enum	NA_Enum
217	217	X	NA_Enum	V	NA_Enum	NA_Enum
218	218	X	NA_Enum	V	NA_Enum	NA_Enum
219	219	X	NA_Enum	V	NA_Enum	NA_Enum
220	220	X	NA_Enum	V	NA_Enum	NA_Enum
221	221	X	NA_Enum	V	NA_Enum	NA_Enum
222	222	X	NA_Enum	V	NA_Enum	NA_Enum
223	223	X	NA_Enum	V	NA_Enum	NA_Enum
224	224	X	NA_Enum	V	NA_Enum	NA_Enum
225	225	\N	\N	\N	\N	\N
226	226	\N	\N	\N	\N	\N
227	227	\N	\N	\N	\N	\N
228	228	\N	\N	\N	\N	\N
229	229	\N	\N	\N	\N	\N
230	230	\N	\N	\N	\N	\N
231	231	\N	\N	\N	\N	\N
232	232	\N	\N	\N	\N	\N
233	233	\N	\N	\N	\N	\N
234	234	\N	\N	\N	\N	\N
235	235	\N	\N	\N	\N	\N
236	236	\N	\N	\N	\N	\N
237	237	\N	\N	\N	\N	\N
238	238	\N	\N	\N	\N	\N
239	239	\N	\N	\N	\N	\N
240	240	\N	\N	\N	\N	\N
241	241	\N	\N	\N	\N	\N
242	242	\N	\N	\N	\N	\N
243	243	\N	\N	\N	\N	\N
244	244	X	X	V	V	X
245	245	X	X	V	V	X
246	246	X	X	V	V	X
247	247	X	X	V	V	X
248	248	X	X	V	V	X
249	249	X	X	V	V	X
250	250	X	X	V	V	X
251	251	X	X	V	V	X
252	252	X	X	V	V	X
253	253	X	X	V	V	X
254	254	X	X	V	V	X
255	255	X	X	V	V	X
256	256	X	NA_Enum	V	NA_Enum	NA_Enum
257	257	X	NA_Enum	V	NA_Enum	NA_Enum
258	258	X	NA_Enum	V	NA_Enum	NA_Enum
259	259	X	NA_Enum	V	NA_Enum	NA_Enum
260	260	X	NA_Enum	V	NA_Enum	NA_Enum
261	261	X	NA_Enum	V	NA_Enum	NA_Enum
262	262	X	NA_Enum	V	NA_Enum	NA_Enum
263	263	X	NA_Enum	V	NA_Enum	NA_Enum
264	264	X	NA_Enum	V	NA_Enum	NA_Enum
265	265	X	NA_Enum	V	NA_Enum	NA_Enum
266	266	X	NA_Enum	V	NA_Enum	NA_Enum
267	267	X	NA_Enum	V	NA_Enum	NA_Enum
268	268	X	NA_Enum	V	NA_Enum	NA_Enum
269	269	X	NA_Enum	V	NA_Enum	NA_Enum
270	270	X	NA_Enum	V	NA_Enum	NA_Enum
271	271	\N	\N	\N	\N	\N
272	272	\N	\N	\N	\N	\N
273	273	\N	\N	\N	\N	\N
274	274	\N	\N	\N	\N	\N
275	275	\N	\N	\N	\N	\N
276	276	\N	\N	\N	\N	\N
277	277	\N	\N	\N	\N	\N
278	278	\N	\N	\N	\N	\N
279	279	\N	\N	\N	\N	\N
280	280	\N	\N	\N	\N	\N
281	281	\N	\N	\N	\N	\N
282	282	\N	\N	\N	\N	\N
283	283	\N	\N	\N	\N	\N
284	284	\N	\N	\N	\N	\N
285	285	X	NA_Enum	V	NA_Enum	NA_Enum
286	286	X	NA_Enum	V	NA_Enum	NA_Enum
287	287	X	NA_Enum	V	NA_Enum	NA_Enum
288	288	X	NA_Enum	V	NA_Enum	NA_Enum
289	289	X	NA_Enum	V	NA_Enum	NA_Enum
290	290	X	NA_Enum	V	NA_Enum	NA_Enum
291	291	X	NA_Enum	V	NA_Enum	NA_Enum
292	292	X	NA_Enum	V	NA_Enum	NA_Enum
293	293	X	NA_Enum	V	NA_Enum	NA_Enum
294	294	X	NA_Enum	V	NA_Enum	NA_Enum
295	295	X	NA_Enum	V	NA_Enum	NA_Enum
296	296	X	NA_Enum	V	NA_Enum	NA_Enum
297	297	X	NA_Enum	V	NA_Enum	NA_Enum
298	298	X	NA_Enum	V	NA_Enum	NA_Enum
299	299	X	NA_Enum	V	NA_Enum	NA_Enum
300	300	X	NA_Enum	V	NA_Enum	NA_Enum
301	301	X	NA_Enum	V	NA_Enum	NA_Enum
302	302	X	NA_Enum	V	NA_Enum	NA_Enum
303	303	X	NA_Enum	V	NA_Enum	NA_Enum
304	304	X	NA_Enum	V	NA_Enum	NA_Enum
305	305	X	NA_Enum	V	NA_Enum	NA_Enum
306	306	X	NA_Enum	V	NA_Enum	NA_Enum
307	307	X	NA_Enum	V	NA_Enum	NA_Enum
308	308	X	NA_Enum	V	NA_Enum	NA_Enum
309	309	X	NA_Enum	V	NA_Enum	NA_Enum
310	310	X	NA_Enum	V	NA_Enum	NA_Enum
311	311	X	NA_Enum	V	NA_Enum	NA_Enum
312	312	X	NA_Enum	V	NA_Enum	NA_Enum
313	313	X	NA_Enum	V	NA_Enum	NA_Enum
314	314	X	NA_Enum	V	NA_Enum	NA_Enum
315	315	X	NA_Enum	V	NA_Enum	NA_Enum
316	316	X	NA_Enum	V	NA_Enum	NA_Enum
317	317	X	NA_Enum	V	NA_Enum	NA_Enum
318	318	X	NA_Enum	V	NA_Enum	NA_Enum
319	319	X	NA_Enum	V	NA_Enum	NA_Enum
320	320	X	NA_Enum	V	NA_Enum	NA_Enum
321	321	X	NA_Enum	V	NA_Enum	NA_Enum
322	322	X	NA_Enum	V	NA_Enum	NA_Enum
323	323	X	NA_Enum	V	NA_Enum	NA_Enum
324	324	X	NA_Enum	V	NA_Enum	NA_Enum
325	325	X	NA_Enum	V	NA_Enum	NA_Enum
326	326	X	NA_Enum	V	NA_Enum	NA_Enum
327	327	X	NA_Enum	V	NA_Enum	NA_Enum
328	328	X	NA_Enum	V	NA_Enum	NA_Enum
329	329	X	NA_Enum	V	NA_Enum	NA_Enum
330	330	X	NA_Enum	V	NA_Enum	NA_Enum
331	331	X	NA_Enum	V	NA_Enum	NA_Enum
332	332	X	NA_Enum	V	NA_Enum	NA_Enum
333	333	X	NA_Enum	V	NA_Enum	NA_Enum
334	334	X	NA_Enum	V	NA_Enum	NA_Enum
335	335	X	NA_Enum	V	NA_Enum	NA_Enum
336	336	X	NA_Enum	V	NA_Enum	NA_Enum
337	337	X	NA_Enum	V	NA_Enum	NA_Enum
338	338	X	NA_Enum	V	NA_Enum	NA_Enum
339	339	X	NA_Enum	V	NA_Enum	NA_Enum
340	340	X	NA_Enum	V	NA_Enum	NA_Enum
341	341	X	NA_Enum	V	NA_Enum	NA_Enum
342	342	X	NA_Enum	V	NA_Enum	NA_Enum
343	343	X	NA_Enum	V	NA_Enum	NA_Enum
344	344	X	NA_Enum	V	NA_Enum	NA_Enum
345	345	X	NA_Enum	V	NA_Enum	NA_Enum
346	346	X	NA_Enum	V	NA_Enum	NA_Enum
347	347	X	NA_Enum	V	NA_Enum	NA_Enum
348	348	X	NA_Enum	V	NA_Enum	NA_Enum
349	349	X	NA_Enum	V	NA_Enum	NA_Enum
350	350	X	NA_Enum	V	NA_Enum	NA_Enum
351	351	X	NA_Enum	V	NA_Enum	NA_Enum
352	352	X	NA_Enum	V	NA_Enum	NA_Enum
353	353	X	NA_Enum	V	NA_Enum	NA_Enum
354	354	X	NA_Enum	V	NA_Enum	NA_Enum
355	355	X	NA_Enum	V	NA_Enum	NA_Enum
356	356	X	NA_Enum	V	NA_Enum	NA_Enum
357	357	X	NA_Enum	V	NA_Enum	NA_Enum
358	358	X	NA_Enum	V	NA_Enum	NA_Enum
359	359	X	NA_Enum	V	NA_Enum	NA_Enum
360	360	X	NA_Enum	V	NA_Enum	NA_Enum
361	361	X	NA_Enum	V	NA_Enum	NA_Enum
362	362	X	NA_Enum	V	NA_Enum	NA_Enum
363	363	X	NA_Enum	V	NA_Enum	NA_Enum
364	364	X	NA_Enum	V	NA_Enum	NA_Enum
365	365	X	NA_Enum	V	NA_Enum	NA_Enum
366	366	X	NA_Enum	V	NA_Enum	NA_Enum
367	367	X	NA_Enum	V	NA_Enum	NA_Enum
368	368	X	NA_Enum	V	NA_Enum	NA_Enum
369	369	X	NA_Enum	V	NA_Enum	NA_Enum
370	370	X	NA_Enum	V	NA_Enum	NA_Enum
371	371	X	NA_Enum	V	NA_Enum	NA_Enum
372	372	X	NA_Enum	V	NA_Enum	NA_Enum
373	373	X	NA_Enum	V	NA_Enum	NA_Enum
374	374	X	NA_Enum	V	NA_Enum	NA_Enum
375	375	X	NA_Enum	V	NA_Enum	NA_Enum
376	376	X	NA_Enum	V	NA_Enum	NA_Enum
377	377	X	NA_Enum	V	NA_Enum	NA_Enum
378	378	X	NA_Enum	V	NA_Enum	NA_Enum
379	379	X	NA_Enum	V	NA_Enum	NA_Enum
380	380	X	NA_Enum	V	NA_Enum	NA_Enum
381	381	X	NA_Enum	V	NA_Enum	NA_Enum
382	382	X	NA_Enum	V	NA_Enum	NA_Enum
383	383	X	NA_Enum	V	NA_Enum	NA_Enum
384	384	X	NA_Enum	V	NA_Enum	NA_Enum
385	385	X	NA_Enum	V	NA_Enum	NA_Enum
386	386	X	NA_Enum	V	NA_Enum	NA_Enum
387	387	X	NA_Enum	V	NA_Enum	NA_Enum
388	388	X	NA_Enum	V	NA_Enum	NA_Enum
389	389	X	NA_Enum	V	NA_Enum	NA_Enum
390	390	X	NA_Enum	V	NA_Enum	NA_Enum
391	391	X	NA_Enum	V	NA_Enum	NA_Enum
392	392	X	NA_Enum	V	NA_Enum	NA_Enum
393	393	X	NA_Enum	V	NA_Enum	NA_Enum
394	394	X	NA_Enum	V	NA_Enum	NA_Enum
395	395	X	NA_Enum	V	NA_Enum	NA_Enum
396	396	X	NA_Enum	V	NA_Enum	NA_Enum
397	397	X	NA_Enum	V	NA_Enum	NA_Enum
398	398	X	NA_Enum	V	NA_Enum	NA_Enum
399	399	X	NA_Enum	V	NA_Enum	NA_Enum
400	400	X	NA_Enum	V	NA_Enum	NA_Enum
401	401	\N	\N	\N	\N	\N
402	402	\N	\N	\N	\N	\N
403	403	\N	\N	\N	\N	\N
404	404	\N	\N	\N	\N	\N
405	405	X	V	V	X	X
406	406	X	V	V	X	X
407	407	X	V	V	X	X
408	408	X	V	V	X	X
409	409	X	V	V	X	X
410	410	X	V	V	X	X
411	411	X	V	V	X	X
412	412	X	V	V	X	X
413	413	X	V	V	X	X
414	414	X	V	V	X	X
415	415	X	V	V	X	X
416	416	X	V	V	X	X
417	417	X	V	V	X	X
418	418	X	V	V	X	X
419	419	X	V	V	X	X
420	420	X	V	V	X	X
421	421	X	V	V	X	X
422	422	X	V	V	X	X
423	423	X	V	V	X	X
424	424	X	V	V	X	X
425	425	X	V	V	X	X
426	426	X	V	V	X	X
427	427	X	V	V	X	X
428	428	X	V	V	X	X
429	429	X	V	V	X	X
430	430	X	V	V	X	X
431	431	X	V	V	X	X
432	432	X	V	V	X	X
433	433	X	V	V	X	X
434	434	X	V	V	X	X
435	435	X	V	V	X	X
436	436	X	V	V	X	X
437	437	X	V	V	X	X
438	438	X	V	V	X	X
439	439	X	V	V	X	X
440	440	X	V	V	X	X
441	441	X	V	V	X	X
442	442	X	V	V	X	X
443	443	X	V	V	X	X
444	444	X	V	V	X	X
445	445	X	V	V	X	X
446	446	X	V	V	X	X
447	447	X	V	V	X	X
448	448	X	V	V	X	X
449	449	X	V	V	X	X
450	450	X	V	V	X	X
451	451	X	V	V	X	X
452	452	X	V	V	X	X
453	453	X	V	V	X	X
454	454	X	V	V	X	X
455	455	X	V	V	X	X
456	456	X	V	V	X	X
457	457	X	V	V	X	X
458	458	X	V	V	X	X
459	459	X	V	V	X	X
460	460	X	V	V	X	X
461	461	\N	\N	\N	\N	\N
462	462	X	X	V	X	X
463	463	X	X	V	X	X
464	464	X	X	V	X	X
465	465	X	X	V	X	X
466	466	X	X	V	X	X
467	467	X	X	V	X	X
468	468	X	X	V	X	X
469	469	X	X	V	X	X
470	470	X	X	V	X	X
471	471	X	X	V	X	X
472	472	X	X	V	X	X
473	473	X	X	V	X	X
474	474	X	X	V	X	X
475	475	X	X	V	X	X
476	476	X	X	V	X	X
477	477	X	X	V	X	X
478	478	X	X	V	X	X
479	479	X	X	V	X	X
480	480	X	X	V	X	X
481	481	X	X	V	X	X
482	482	X	X	V	X	X
483	483	X	X	V	X	X
484	484	X	X	V	X	X
485	485	X	X	V	X	X
486	486	X	X	V	X	X
487	487	X	X	V	X	X
488	488	X	X	V	X	X
489	489	X	X	V	X	X
490	490	X	X	V	X	X
491	491	X	X	V	X	X
492	492	X	X	V	X	X
493	493	X	X	V	X	X
494	494	X	X	V	X	X
495	495	X	X	V	X	X
496	496	X	X	V	X	X
497	497	X	X	V	X	X
498	498	X	X	V	X	X
499	499	X	X	V	X	X
500	500	X	X	V	X	X
501	501	X	X	V	X	X
502	502	X	X	V	X	X
503	503	X	X	V	X	X
504	504	X	X	V	X	X
505	505	X	X	V	X	X
506	506	X	X	V	X	X
507	507	X	X	V	X	X
508	508	X	X	V	X	X
509	509	X	X	V	X	X
510	510	X	X	V	X	X
511	511	X	X	V	X	X
512	512	X	X	V	X	X
513	513	X	X	V	X	X
514	514	X	X	V	X	X
515	515	X	X	V	X	X
516	516	X	X	V	X	X
517	517	X	X	V	X	X
518	518	X	X	V	X	X
519	519	X	X	V	X	X
520	520	X	X	V	X	X
521	521	X	X	V	X	X
522	522	X	X	V	X	X
523	523	X	X	V	X	X
524	524	X	X	V	X	X
525	525	X	X	V	X	X
526	526	X	X	V	X	X
527	527	X	X	V	X	X
528	528	X	X	V	X	X
529	529	X	X	V	X	X
530	530	X	X	V	X	X
531	531	X	X	V	X	X
532	532	X	X	V	X	X
533	533	X	X	V	X	X
534	534	X	X	V	X	X
535	535	X	X	V	X	X
536	536	X	X	V	X	X
537	537	X	X	V	X	X
538	538	X	X	V	X	X
539	539	X	X	V	X	X
540	540	X	X	V	X	X
541	541	X	X	V	X	X
542	542	X	X	V	X	X
543	543	X	X	V	X	X
544	544	X	X	V	X	X
545	545	X	X	V	X	X
546	546	X	X	V	X	X
547	547	X	X	V	X	X
548	548	X	X	V	X	X
549	549	X	X	V	X	X
550	550	X	X	V	X	X
551	551	X	X	V	X	X
552	552	X	X	V	X	X
553	553	X	X	V	X	X
554	554	X	X	V	X	X
555	555	X	X	V	X	X
556	556	X	X	V	X	X
557	557	X	X	V	X	X
558	558	X	X	V	X	X
559	559	X	X	V	X	X
560	560	X	X	V	X	X
561	561	X	X	V	X	X
562	562	X	X	V	X	X
563	563	X	X	V	X	X
564	564	X	X	V	X	X
565	565	X	X	V	X	X
566	566	X	X	V	X	X
567	567	X	X	V	X	X
568	568	X	X	V	X	X
569	569	X	X	V	X	X
570	570	X	X	V	X	X
571	571	X	X	V	X	X
572	572	X	X	V	X	X
573	573	X	X	V	X	X
574	574	X	X	V	X	X
575	575	X	X	V	X	X
576	576	X	X	V	X	X
577	577	X	X	V	X	X
578	578	X	X	V	X	X
579	579	X	X	V	X	X
580	580	X	X	V	X	X
581	581	X	X	V	X	X
582	582	X	X	V	X	X
583	583	X	X	V	X	X
584	584	X	X	V	X	X
585	585	X	X	V	X	X
586	586	X	X	V	X	X
587	587	X	X	V	X	X
588	588	X	X	V	X	X
589	589	X	X	V	X	X
590	590	X	X	V	X	X
591	591	X	X	V	X	X
592	592	X	X	V	X	X
593	593	X	X	V	X	X
594	594	X	X	V	X	X
595	595	X	X	V	X	X
596	596	X	X	V	X	X
597	597	X	X	V	X	X
598	598	X	X	V	X	X
599	599	X	X	V	X	X
600	600	X	X	V	X	X
601	601	X	X	V	X	X
602	602	X	X	V	X	X
603	603	X	X	V	X	X
604	604	X	X	V	X	X
605	605	X	X	V	X	X
606	606	X	X	V	X	X
607	607	X	X	V	X	X
608	608	X	X	V	X	X
609	609	X	X	V	X	X
610	610	X	X	V	X	X
611	611	X	X	V	X	X
612	612	X	X	V	X	X
613	613	X	X	V	X	X
614	614	X	X	V	X	X
615	615	X	X	V	X	X
616	616	X	X	V	X	X
617	617	X	X	V	X	X
618	618	X	X	V	X	X
619	619	X	X	V	X	X
620	620	X	X	V	X	X
621	621	X	X	V	X	X
622	622	X	X	V	X	X
623	623	X	X	V	X	X
624	624	X	X	V	X	X
625	625	X	X	V	X	X
626	626	X	X	V	X	X
627	627	X	X	V	X	X
628	628	X	X	V	X	X
629	629	X	X	V	X	X
630	630	X	X	V	X	X
631	631	X	X	V	X	X
632	632	X	X	V	X	X
633	633	X	X	V	X	X
634	634	X	X	V	X	X
635	635	X	X	V	X	X
636	636	X	X	V	X	X
637	637	X	X	V	X	X
638	638	X	X	V	X	X
639	639	X	X	V	X	X
640	640	X	X	V	X	X
641	641	X	X	V	X	X
642	642	X	X	V	X	X
643	643	X	X	V	X	X
644	644	X	X	V	X	X
645	645	X	X	V	X	X
646	646	X	X	V	X	X
647	647	\N	\N	\N	\N	\N
648	648	\N	\N	\N	\N	\N
649	649	\N	\N	\N	\N	\N
650	650	\N	\N	\N	\N	\N
651	651	\N	\N	\N	\N	\N
652	652	\N	\N	\N	\N	\N
653	653	\N	\N	\N	\N	\N
654	654	\N	\N	\N	\N	\N
655	655	\N	\N	\N	\N	\N
656	656	\N	\N	\N	\N	\N
657	657	\N	\N	\N	\N	\N
658	658	\N	\N	\N	\N	\N
659	659	\N	\N	\N	\N	\N
660	660	\N	\N	\N	\N	\N
661	661	X	X	X	X	V
662	662	X	X	X	X	V
663	663	X	X	X	X	V
664	664	X	X	X	X	V
665	665	X	X	X	X	V
666	666	X	X	X	X	V
667	667	X	X	X	X	V
668	668	X	X	X	X	V
669	669	X	X	X	X	V
670	670	X	X	X	X	V
671	671	X	X	X	X	V
672	672	X	X	X	X	V
673	673	X	X	X	X	V
674	674	X	X	X	X	V
675	675	X	X	X	X	V
676	676	X	X	X	X	V
677	677	X	X	X	X	V
678	678	X	X	X	X	V
679	679	X	X	X	X	V
680	680	X	X	X	X	V
681	681	X	X	X	X	V
682	682	X	X	X	X	V
683	683	X	X	X	X	V
684	684	X	X	X	X	V
685	685	X	X	X	X	V
686	686	X	X	X	X	V
687	687	X	X	X	X	V
688	688	X	X	X	X	V
689	689	X	X	X	X	V
690	690	X	X	X	X	V
691	691	X	X	X	X	V
692	692	X	X	X	X	V
693	693	X	X	X	X	V
694	694	X	X	X	X	V
695	695	X	X	X	X	V
696	696	X	X	X	X	V
697	697	X	X	X	X	V
698	698	X	X	X	X	V
699	699	X	X	X	X	V
700	700	X	X	X	X	V
701	701	X	X	X	X	V
702	702	X	X	X	X	V
703	703	X	X	X	X	V
704	704	X	X	X	X	V
705	705	X	X	X	X	V
706	706	X	X	X	X	V
707	707	X	X	X	X	V
708	708	X	X	X	X	V
709	709	X	X	X	X	V
710	710	X	X	X	X	V
711	711	X	X	X	X	V
712	712	X	X	X	X	V
713	713	X	X	X	X	V
714	714	X	X	X	X	V
715	715	X	X	X	X	V
716	716	X	X	X	X	V
717	717	X	X	X	X	V
718	718	X	X	X	X	V
719	719	X	X	X	X	V
720	720	X	X	X	X	V
721	721	X	X	X	X	V
722	722	X	X	X	X	V
723	723	X	X	X	X	V
724	724	X	X	X	X	V
725	725	X	X	X	X	V
726	726	X	X	X	X	V
727	727	X	X	X	X	V
728	728	X	X	X	X	V
729	729	X	X	X	X	V
730	730	X	X	X	X	V
731	731	X	X	X	X	V
732	732	X	X	X	X	V
733	733	X	X	X	X	V
734	734	X	X	V	X	X
735	735	X	X	V	X	X
736	736	X	X	V	X	X
737	737	X	X	V	X	X
738	738	X	X	V	X	X
739	739	X	X	V	X	X
740	740	X	X	V	X	X
741	741	X	X	V	X	X
742	742	X	X	V	X	X
743	743	X	X	V	X	X
744	744	X	X	V	X	X
745	745	X	X	V	X	X
746	746	X	X	V	X	X
747	747	X	X	V	X	X
748	748	X	X	V	X	X
749	749	X	X	V	X	X
750	750	X	X	V	X	X
751	751	X	X	V	X	X
752	752	X	X	V	X	X
753	753	X	X	V	X	X
754	754	X	X	V	X	X
755	755	X	X	V	X	X
756	756	X	X	V	X	X
757	757	X	X	V	X	X
758	758	X	X	V	X	X
759	759	X	X	V	X	X
760	760	X	X	V	X	X
761	761	X	X	V	X	X
762	762	X	X	V	X	X
763	763	X	X	V	X	X
764	764	X	X	V	X	X
765	765	X	X	V	X	X
766	766	X	X	V	X	X
767	767	X	X	V	X	X
768	768	X	X	V	X	X
769	769	X	X	V	X	X
770	770	X	X	V	X	X
771	771	X	X	V	X	X
772	772	X	X	V	X	X
773	773	X	X	V	X	X
774	774	X	X	V	X	X
775	775	X	X	V	X	X
776	776	X	X	V	X	X
777	777	X	X	V	X	X
778	778	X	X	V	X	X
779	779	X	X	V	X	X
780	780	X	X	V	X	X
781	781	X	X	V	X	X
782	782	X	X	V	X	X
783	783	X	X	V	X	X
784	784	X	X	V	X	X
785	785	X	X	V	X	X
786	786	X	X	V	X	X
787	787	X	X	V	X	X
788	788	X	X	V	X	X
789	789	X	X	V	X	X
790	790	X	X	V	X	X
791	791	X	X	V	X	X
792	792	X	X	V	X	X
793	793	X	X	V	X	X
794	794	X	X	V	X	X
795	795	X	X	V	X	X
796	796	X	X	V	X	X
797	797	X	X	V	X	X
798	798	X	X	V	X	X
799	799	X	X	X	V	X
800	800	X	X	X	V	X
801	801	X	X	X	V	X
802	802	X	X	X	V	X
803	803	X	X	X	V	X
804	804	X	X	X	V	X
805	805	X	X	X	V	X
806	806	X	X	X	V	X
807	807	X	X	X	V	X
808	808	X	X	X	V	X
809	809	X	X	X	V	X
810	810	X	X	X	V	X
811	811	X	X	X	V	X
812	812	X	X	X	V	X
813	813	X	X	X	V	X
814	814	X	X	X	V	X
815	815	X	X	X	V	X
816	816	X	X	X	V	X
817	817	X	X	X	V	X
818	818	X	X	X	V	X
819	819	X	X	X	V	X
820	820	X	X	X	V	X
821	821	X	X	X	V	X
822	822	X	X	X	V	X
823	823	X	X	X	V	X
824	824	X	X	X	V	X
825	825	X	X	X	V	X
826	826	X	X	X	V	X
827	827	X	X	X	V	X
828	828	X	X	X	V	X
829	829	X	X	X	V	X
830	830	X	X	X	V	X
831	831	X	X	X	V	X
832	832	X	X	X	V	X
833	833	X	X	X	V	X
834	834	X	X	X	V	X
835	835	X	X	X	V	X
836	836	X	X	X	V	X
837	837	X	X	X	V	X
838	838	X	X	X	V	X
839	839	X	X	X	V	X
840	840	X	X	X	V	X
841	841	X	X	X	V	X
842	842	X	X	X	V	X
843	843	X	X	X	V	X
844	844	X	X	X	V	X
845	845	X	X	X	V	X
846	846	V	X	X	X	X
847	847	V	X	X	X	X
848	848	V	X	X	X	X
849	849	V	X	X	X	X
850	850	V	X	X	X	X
851	851	V	X	X	X	X
852	852	V	X	X	X	X
853	853	V	X	X	X	X
854	854	V	X	X	X	X
855	855	V	X	X	X	X
856	856	V	X	X	X	X
857	857	V	X	X	X	X
858	858	V	X	X	X	X
859	859	V	X	X	X	X
860	860	X	X	V	X	X
861	861	X	X	V	X	X
862	862	X	X	V	X	X
863	863	X	X	V	X	X
864	864	X	X	V	X	X
865	865	X	X	V	X	X
866	866	X	X	V	X	X
867	867	X	X	V	X	X
868	868	X	X	V	X	X
869	869	X	X	V	X	X
870	870	X	X	V	X	X
871	871	X	X	V	X	X
872	872	X	X	V	X	X
873	873	X	X	V	X	X
874	874	X	X	V	X	X
875	875	X	X	V	X	X
876	876	X	X	V	X	X
877	877	X	X	V	X	X
878	878	X	X	V	X	X
879	879	X	X	V	X	X
880	880	X	X	V	X	X
881	881	X	X	V	X	X
882	882	X	X	V	X	X
883	883	X	X	V	X	X
884	884	X	X	V	X	X
885	885	X	X	V	X	X
886	886	X	X	V	X	X
887	887	X	X	V	X	X
888	888	X	X	V	X	X
889	889	X	X	V	X	X
890	890	X	X	V	X	X
891	891	X	X	V	X	X
892	892	X	X	V	X	X
893	893	X	X	V	X	X
894	894	X	X	V	X	X
895	895	X	X	V	X	X
896	896	X	X	V	X	X
897	897	X	X	V	X	X
898	898	X	X	V	X	X
899	899	X	X	V	X	X
900	900	X	X	V	X	X
901	901	X	V	X	X	X
902	902	X	V	X	X	X
903	903	X	V	X	X	X
904	904	X	V	X	X	X
905	905	X	V	X	X	X
906	906	X	V	X	X	X
907	907	X	V	X	X	X
908	908	X	V	X	X	X
909	909	X	V	X	X	X
910	910	X	V	X	X	X
911	911	X	V	X	X	X
912	912	X	V	X	X	X
913	913	X	V	X	X	X
914	914	X	V	X	X	X
915	915	X	V	X	X	X
916	916	X	V	X	X	X
917	917	X	V	X	X	X
918	918	X	V	X	X	X
919	919	X	V	X	X	X
920	920	X	V	X	X	X
921	921	X	V	X	X	X
922	922	X	V	X	X	X
923	923	X	V	X	X	X
924	924	X	V	X	X	X
925	925	X	V	X	X	X
926	926	X	V	X	X	X
927	927	X	V	X	X	X
928	928	X	V	X	X	X
929	929	X	V	X	X	X
930	930	X	V	X	X	X
931	931	X	V	X	X	X
932	932	\N	\N	\N	\N	\N
933	933	\N	\N	\N	\N	\N
934	934	\N	\N	\N	\N	\N
935	935	\N	\N	\N	\N	\N
936	936	\N	\N	\N	\N	\N
937	937	\N	\N	\N	\N	\N
938	938	\N	\N	\N	\N	\N
939	939	\N	\N	\N	\N	\N
940	940	\N	\N	\N	\N	\N
941	941	\N	\N	\N	\N	\N
942	942	\N	\N	\N	\N	\N
943	943	\N	\N	\N	\N	\N
944	944	\N	\N	\N	\N	\N
945	945	\N	\N	\N	\N	\N
946	946	\N	\N	\N	\N	\N
947	947	\N	\N	\N	\N	\N
948	948	\N	\N	\N	\N	\N
949	949	\N	\N	\N	\N	\N
950	950	\N	\N	\N	\N	\N
951	951	\N	\N	\N	\N	\N
952	952	\N	\N	\N	\N	\N
953	953	X	V	V	X	X
954	954	X	V	V	X	X
955	955	X	V	V	X	X
956	956	X	V	V	X	X
957	957	X	V	V	X	X
958	958	X	V	V	X	X
959	959	X	V	V	X	X
960	960	X	V	V	X	X
961	961	X	V	V	X	X
962	962	X	V	V	X	X
963	963	X	V	V	X	X
964	964	X	V	V	X	X
965	965	X	V	V	X	X
966	966	X	V	V	X	X
967	967	X	V	V	X	X
968	968	X	V	V	X	X
969	969	X	V	V	X	X
970	970	X	V	V	X	X
971	971	X	NA_Enum	V	NA_Enum	NA_Enum
972	972	X	NA_Enum	V	NA_Enum	NA_Enum
973	973	X	NA_Enum	V	NA_Enum	NA_Enum
974	974	X	NA_Enum	V	NA_Enum	NA_Enum
975	975	X	NA_Enum	V	NA_Enum	NA_Enum
976	976	\N	\N	\N	\N	\N
977	977	\N	\N	\N	\N	\N
978	978	\N	\N	\N	\N	\N
979	979	X	NA_Enum	V	NA_Enum	NA_Enum
980	980	X	NA_Enum	V	NA_Enum	NA_Enum
981	981	X	NA_Enum	V	NA_Enum	NA_Enum
982	982	X	NA_Enum	V	NA_Enum	NA_Enum
983	983	X	NA_Enum	V	NA_Enum	NA_Enum
984	984	X	NA_Enum	V	NA_Enum	NA_Enum
985	985	X	NA_Enum	V	NA_Enum	NA_Enum
986	986	X	NA_Enum	V	NA_Enum	NA_Enum
987	987	X	NA_Enum	V	NA_Enum	NA_Enum
988	988	X	NA_Enum	V	NA_Enum	NA_Enum
989	989	X	NA_Enum	V	NA_Enum	NA_Enum
990	990	X	NA_Enum	V	NA_Enum	NA_Enum
991	991	X	NA_Enum	V	NA_Enum	NA_Enum
992	992	X	NA_Enum	V	NA_Enum	NA_Enum
993	993	X	NA_Enum	V	NA_Enum	NA_Enum
994	994	X	NA_Enum	V	NA_Enum	NA_Enum
995	995	X	NA_Enum	V	NA_Enum	NA_Enum
996	996	X	NA_Enum	V	NA_Enum	NA_Enum
997	997	X	NA_Enum	V	NA_Enum	NA_Enum
998	998	X	NA_Enum	V	NA_Enum	NA_Enum
999	999	X	NA_Enum	V	NA_Enum	NA_Enum
1000	1000	X	NA_Enum	V	NA_Enum	NA_Enum
1001	1001	X	NA_Enum	V	NA_Enum	NA_Enum
1002	1002	X	NA_Enum	V	NA_Enum	NA_Enum
1003	1003	X	NA_Enum	V	NA_Enum	NA_Enum
1004	1004	X	NA_Enum	V	NA_Enum	NA_Enum
1005	1005	X	NA_Enum	V	NA_Enum	NA_Enum
1006	1006	X	NA_Enum	V	NA_Enum	NA_Enum
1007	1007	X	NA_Enum	V	NA_Enum	NA_Enum
1008	1008	X	NA_Enum	V	NA_Enum	NA_Enum
1009	1009	X	NA_Enum	V	NA_Enum	NA_Enum
1010	1010	X	NA_Enum	V	NA_Enum	NA_Enum
1011	1011	X	NA_Enum	V	NA_Enum	NA_Enum
1012	1012	X	NA_Enum	V	NA_Enum	NA_Enum
1013	1013	X	NA_Enum	V	NA_Enum	NA_Enum
1014	1014	X	NA_Enum	V	NA_Enum	NA_Enum
1015	1015	X	NA_Enum	V	NA_Enum	NA_Enum
1016	1016	X	NA_Enum	V	NA_Enum	NA_Enum
1017	1017	X	NA_Enum	V	NA_Enum	NA_Enum
\.


--
-- TOC entry 5024 (class 0 OID 16437)
-- Dependencies: 231
-- Data for Name: pricing; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pricing (id, project_id, price) FROM stdin;
1	1	\N
2	2	\N
3	3	\N
4	4	\N
5	5	\N
6	6	\N
7	7	\N
8	8	\N
9	9	\N
10	10	\N
11	11	\N
12	12	\N
13	13	\N
14	14	\N
15	15	\N
16	16	\N
17	17	\N
18	18	\N
19	19	\N
20	20	\N
21	21	\N
22	22	\N
23	23	\N
24	24	\N
25	25	\N
26	26	\N
27	27	\N
28	28	\N
29	29	\N
30	30	\N
31	31	\N
32	32	\N
33	33	\N
34	34	\N
35	35	\N
36	36	\N
37	37	\N
38	38	\N
39	39	\N
40	40	\N
41	41	\N
42	42	\N
43	43	\N
44	44	\N
45	45	\N
46	46	\N
47	47	\N
48	48	\N
49	49	\N
50	50	\N
51	51	\N
52	52	\N
53	53	\N
54	54	\N
55	55	\N
56	56	\N
57	57	\N
58	58	\N
59	59	\N
60	60	\N
61	61	5.0
62	62	5.0
63	63	\N
64	64	\N
65	65	\N
66	66	\N
67	67	2.0
68	68	2.0
69	69	2.0
70	70	2.0
71	71	2.0
72	72	2.0
73	73	2.0
74	74	2.0
75	75	2.0
76	76	2.0
77	77	2.0
78	78	2.0
79	79	2.0
80	80	2.0
81	81	2.0
82	82	2.0
83	83	2.0
84	84	2.0
85	85	2.0
86	86	2.0
87	87	2.0
88	88	2.0
89	89	2.0
90	90	2.0
91	91	2.0
92	92	2.0
93	93	2.0
94	94	2.0
95	95	2.0
96	96	2.0
97	97	2.0
98	98	2.0
99	99	2.0
100	100	2.0
101	101	2.0
102	102	2.0
103	103	2.0
104	104	2.0
105	105	2.0
106	106	2.0
107	107	2.0
108	108	2.0
109	109	2.0
110	110	2.0
111	111	2.0
112	112	2.0
113	113	2.0
114	114	2.0
115	115	2.0
116	116	2.0
117	117	2.0
118	118	2.0
119	119	2.0
120	120	2.0
121	121	2.0
122	122	2.0
123	123	2.0
124	124	\N
125	125	\N
126	126	\N
127	127	\N
128	128	\N
129	129	\N
130	130	\N
131	131	\N
132	132	\N
133	133	\N
134	134	\N
135	135	\N
136	136	\N
137	137	\N
138	138	\N
139	139	\N
140	140	\N
141	141	\N
142	142	\N
143	143	\N
144	144	\N
145	145	\N
146	146	\N
147	147	\N
148	148	\N
149	149	\N
150	150	\N
151	151	\N
152	152	\N
153	153	2.0
154	154	2.0
155	155	2.0
156	156	2.0
157	157	2.0
158	158	2.0
159	159	2.0
160	160	2.0
161	161	2.0
162	162	2.0
163	163	2.0
164	164	2.0
165	165	2.0
166	166	2.0
167	167	2.0
168	168	2.0
169	169	2.0
170	170	2.0
171	171	2.0
172	172	2.0
173	173	2.0
174	174	2.0
175	175	2.0
176	176	\N
177	177	\N
178	178	\N
179	179	\N
180	180	6.0
181	181	6.0
182	182	6.0
183	183	6.0
184	184	6.0
185	185	6.0
186	186	6.0
187	187	6.0
188	188	6.0
189	189	6.0
190	190	6.0
191	191	6.0
192	192	6.0
193	193	6.0
194	194	6.0
195	195	6.0
196	196	6.0
197	197	6.0
198	198	6.0
199	199	6.0
200	200	6.0
201	201	6.0
202	202	6.0
203	203	6.0
204	204	6.0
205	205	6.0
206	206	6.0
207	207	6.0
208	208	6.0
209	209	6.0
210	210	7.0
211	211	7.0
212	212	7.0
213	213	7.0
214	214	7.0
215	215	7.0
216	216	2.0
217	217	2.0
218	218	2.0
219	219	2.0
220	220	2.0
221	221	2.0
222	222	2.0
223	223	2.0
224	224	2.0
225	225	\N
226	226	\N
227	227	\N
228	228	\N
229	229	\N
230	230	\N
231	231	\N
232	232	\N
233	233	\N
234	234	\N
235	235	\N
236	236	\N
237	237	\N
238	238	\N
239	239	\N
240	240	\N
241	241	\N
242	242	\N
243	243	\N
244	244	8.0
245	245	8.0
246	246	8.0
247	247	8.0
248	248	8.0
249	249	8.0
250	250	8.0
251	251	8.0
252	252	8.0
253	253	8.0
254	254	8.0
255	255	8.0
256	256	1.0
257	257	1.0
258	258	1.0
259	259	1.0
260	260	1.0
261	261	1.0
262	262	1.0
263	263	1.0
264	264	1.0
265	265	1.0
266	266	1.0
267	267	1.0
268	268	1.0
269	269	3.0
270	270	3.0
271	271	\N
272	272	\N
273	273	\N
274	274	\N
275	275	\N
276	276	\N
277	277	\N
278	278	\N
279	279	\N
280	280	\N
281	281	\N
282	282	\N
283	283	\N
284	284	\N
285	285	1.0
286	286	1.0
287	287	1.0
288	288	1.0
289	289	1.0
290	290	1.0
291	291	1.0
292	292	1.0
293	293	1.0
294	294	1.0
295	295	1.0
296	296	1.0
297	297	1.0
298	298	1.0
299	299	1.0
300	300	1.0
301	301	1.0
302	302	1.0
303	303	1.0
304	304	1.0
305	305	1.0
306	306	1.0
307	307	1.0
308	308	1.0
309	309	1.0
310	310	1.0
311	311	1.0
312	312	1.0
313	313	1.0
314	314	1.0
315	315	1.0
316	316	1.0
317	317	1.0
318	318	1.0
319	319	1.0
320	320	1.0
321	321	1.0
322	322	1.0
323	323	1.0
324	324	1.0
325	325	1.0
326	326	1.0
327	327	1.0
328	328	1.0
329	329	1.0
330	330	1.0
331	331	1.0
332	332	1.0
333	333	1.0
334	334	1.0
335	335	1.0
336	336	1.0
337	337	1.0
338	338	1.0
339	339	1.0
340	340	1.0
341	341	1.0
342	342	1.0
343	343	1.0
344	344	1.0
345	345	1.0
346	346	1.0
347	347	1.0
348	348	1.0
349	349	1.0
350	350	1.0
351	351	1.0
352	352	1.0
353	353	1.0
354	354	1.0
355	355	1.0
356	356	1.0
357	357	1.0
358	358	1.0
359	359	1.0
360	360	1.0
361	361	1.0
362	362	1.0
363	363	1.0
364	364	1.0
365	365	1.0
366	366	1.0
367	367	1.0
368	368	1.0
369	369	1.0
370	370	1.0
371	371	1.0
372	372	1.0
373	373	1.0
374	374	1.0
375	375	1.0
376	376	1.0
377	377	1.0
378	378	1.0
379	379	1.0
380	380	1.0
381	381	1.0
382	382	1.0
383	383	1.0
384	384	1.0
385	385	1.0
386	386	1.0
387	387	1.0
388	388	1.0
389	389	1.0
390	390	1.0
391	391	1.0
392	392	1.0
393	393	1.0
394	394	1.0
395	395	1.0
396	396	1.0
397	397	1.0
398	398	1.0
399	399	1.0
400	400	1.0
401	401	\N
402	402	\N
403	403	\N
404	404	\N
405	405	9.0
406	406	9.0
407	407	9.0
408	408	9.0
409	409	9.0
410	410	9.0
411	411	9.0
412	412	9.0
413	413	9.0
414	414	9.0
415	415	9.0
416	416	9.0
417	417	9.0
418	418	9.0
419	419	9.0
420	420	9.0
421	421	9.0
422	422	9.0
423	423	9.0
424	424	9.0
425	425	9.0
426	426	9.0
427	427	9.0
428	428	9.0
429	429	9.0
430	430	9.0
431	431	10.0
432	432	10.0
433	433	10.0
434	434	10.0
435	435	10.0
436	436	10.0
437	437	10.0
438	438	10.0
439	439	10.0
440	440	10.0
441	441	10.0
442	442	10.0
443	443	10.0
444	444	10.0
445	445	10.0
446	446	10.0
447	447	10.0
448	448	10.0
449	449	10.0
450	450	10.0
451	451	10.0
452	452	10.0
453	453	10.0
454	454	10.0
455	455	10.0
456	456	10.0
457	457	10.0
458	458	10.0
459	459	10.0
460	460	10.0
461	461	\N
462	462	3.0
463	463	3.0
464	464	3.0
465	465	3.0
466	466	3.0
467	467	3.0
468	468	3.0
469	469	3.0
470	470	3.0
471	471	3.0
472	472	3.0
473	473	3.0
474	474	3.0
475	475	3.0
476	476	3.0
477	477	3.0
478	478	3.0
479	479	3.0
480	480	3.0
481	481	3.0
482	482	3.0
483	483	3.0
484	484	3.0
485	485	3.0
486	486	3.0
487	487	3.0
488	488	3.0
489	489	3.0
490	490	3.0
491	491	3.0
492	492	3.0
493	493	3.0
494	494	3.0
495	495	3.0
496	496	3.0
497	497	3.0
498	498	3.0
499	499	3.0
500	500	3.0
501	501	3.0
502	502	3.0
503	503	3.0
504	504	3.0
505	505	3.0
506	506	3.0
507	507	3.0
508	508	3.0
509	509	3.0
510	510	3.0
511	511	3.0
512	512	3.0
513	513	3.0
514	514	3.0
515	515	3.0
516	516	3.0
517	517	3.0
518	518	3.0
519	519	3.0
520	520	3.0
521	521	3.0
522	522	3.0
523	523	3.0
524	524	3.0
525	525	3.0
526	526	3.0
527	527	3.0
528	528	3.0
529	529	3.0
530	530	3.0
531	531	3.0
532	532	3.0
533	533	3.0
534	534	3.0
535	535	3.0
536	536	3.0
537	537	3.0
538	538	3.0
539	539	3.0
540	540	3.0
541	541	3.0
542	542	3.0
543	543	3.0
544	544	3.0
545	545	3.0
546	546	3.0
547	547	3.0
548	548	3.0
549	549	3.0
550	550	3.0
551	551	3.0
552	552	3.0
553	553	3.0
554	554	3.0
555	555	3.0
556	556	3.0
557	557	3.0
558	558	3.0
559	559	3.0
560	560	3.0
561	561	3.0
562	562	4.0
563	563	4.0
564	564	4.0
565	565	4.0
566	566	4.0
567	567	4.0
568	568	4.0
569	569	4.0
570	570	4.0
571	571	4.0
572	572	4.0
573	573	4.0
574	574	4.0
575	575	4.0
576	576	4.0
577	577	4.0
578	578	4.0
579	579	4.0
580	580	4.0
581	581	4.0
582	582	4.0
583	583	4.0
584	584	4.0
585	585	4.0
586	586	4.0
587	587	4.0
588	588	4.0
589	589	4.0
590	590	4.0
591	591	4.0
592	592	4.0
593	593	4.0
594	594	4.0
595	595	4.0
596	596	4.0
597	597	4.0
598	598	4.0
599	599	4.0
600	600	4.0
601	601	4.0
602	602	4.0
603	603	4.0
604	604	4.0
605	605	4.0
606	606	4.0
607	607	4.0
608	608	4.0
609	609	4.0
610	610	4.0
611	611	4.0
612	612	4.0
613	613	4.0
614	614	4.0
615	615	4.0
616	616	4.0
617	617	4.0
618	618	4.0
619	619	4.0
620	620	4.0
621	621	4.0
622	622	4.0
623	623	4.0
624	624	4.0
625	625	4.0
626	626	4.0
627	627	4.0
628	628	4.0
629	629	4.0
630	630	4.0
631	631	4.0
632	632	4.0
633	633	4.0
634	634	4.0
635	635	4.0
636	636	4.0
637	637	4.0
638	638	4.0
639	639	4.0
640	640	4.0
641	641	4.0
642	642	4.0
643	643	4.0
644	644	4.0
645	645	4.0
646	646	4.0
647	647	\N
648	648	\N
649	649	\N
650	650	\N
651	651	\N
652	652	\N
653	653	\N
654	654	\N
655	655	\N
656	656	\N
657	657	\N
658	658	\N
659	659	\N
660	660	\N
661	661	1.0
662	662	1.0
663	663	1.0
664	664	1.0
665	665	1.0
666	666	1.0
667	667	1.0
668	668	1.0
669	669	1.0
670	670	1.0
671	671	1.0
672	672	1.0
673	673	1.0
674	674	1.0
675	675	1.0
676	676	1.0
677	677	1.0
678	678	1.0
679	679	1.0
680	680	1.0
681	681	1.0
682	682	1.0
683	683	1.0
684	684	1.0
685	685	1.0
686	686	1.0
687	687	1.0
688	688	1.0
689	689	1.0
690	690	1.0
691	691	1.0
692	692	1.0
693	693	1.0
694	694	1.0
695	695	1.0
696	696	1.0
697	697	1.0
698	698	1.0
699	699	1.0
700	700	1.0
701	701	1.0
702	702	1.0
703	703	1.0
704	704	1.0
705	705	1.0
706	706	1.0
707	707	1.0
708	708	1.0
709	709	1.0
710	710	1.0
711	711	1.0
712	712	1.0
713	713	1.0
714	714	1.0
715	715	1.0
716	716	1.0
717	717	1.0
718	718	1.0
719	719	1.0
720	720	1.0
721	721	1.0
722	722	1.0
723	723	1.0
724	724	1.0
725	725	1.0
726	726	1.0
727	727	1.0
728	728	1.0
729	729	1.0
730	730	1.0
731	731	1.0
732	732	1.0
733	733	1.0
734	734	1.0
735	735	1.0
736	736	1.0
737	737	1.0
738	738	1.0
739	739	1.0
740	740	1.0
741	741	1.0
742	742	1.0
743	743	1.0
744	744	1.0
745	745	1.0
746	746	1.0
747	747	1.0
748	748	1.0
749	749	1.0
750	750	1.0
751	751	1.0
752	752	1.0
753	753	1.0
754	754	1.0
755	755	1.0
756	756	1.0
757	757	1.0
758	758	1.0
759	759	1.0
760	760	1.0
761	761	1.0
762	762	1.0
763	763	1.0
764	764	1.0
765	765	1.0
766	766	1.0
767	767	1.0
768	768	1.0
769	769	1.0
770	770	1.0
771	771	1.0
772	772	1.0
773	773	1.0
774	774	1.0
775	775	1.0
776	776	1.0
777	777	1.0
778	778	1.0
779	779	1.0
780	780	1.0
781	781	1.0
782	782	1.0
783	783	1.0
784	784	1.0
785	785	1.0
786	786	1.0
787	787	1.0
788	788	1.0
789	789	1.0
790	790	1.0
791	791	1.0
792	792	1.0
793	793	1.0
794	794	1.0
795	795	1.0
796	796	1.0
797	797	1.0
798	798	1.0
799	799	1.0
800	800	1.0
801	801	1.0
802	802	1.0
803	803	1.0
804	804	1.0
805	805	1.0
806	806	1.0
807	807	1.0
808	808	1.0
809	809	1.0
810	810	1.0
811	811	1.0
812	812	1.0
813	813	1.0
814	814	1.0
815	815	1.0
816	816	1.0
817	817	1.0
818	818	1.0
819	819	1.0
820	820	1.0
821	821	1.0
822	822	1.0
823	823	1.0
824	824	1.0
825	825	1.0
826	826	1.0
827	827	1.0
828	828	1.0
829	829	1.0
830	830	1.0
831	831	1.0
832	832	1.0
833	833	1.0
834	834	1.0
835	835	1.0
836	836	1.0
837	837	1.0
838	838	1.0
839	839	1.0
840	840	1.0
841	841	1.0
842	842	1.0
843	843	1.0
844	844	1.0
845	845	1.0
846	846	1.0
847	847	1.0
848	848	1.0
849	849	1.0
850	850	1.0
851	851	1.0
852	852	1.0
853	853	1.0
854	854	1.0
855	855	1.0
856	856	1.0
857	857	1.0
858	858	1.0
859	859	1.0
860	860	1.0
861	861	1.0
862	862	1.0
863	863	1.0
864	864	1.0
865	865	1.0
866	866	1.0
867	867	1.0
868	868	1.0
869	869	1.0
870	870	1.0
871	871	1.0
872	872	1.0
873	873	1.0
874	874	1.0
875	875	1.0
876	876	1.0
877	877	1.0
878	878	1.0
879	879	1.0
880	880	1.0
881	881	1.0
882	882	1.0
883	883	1.0
884	884	1.0
885	885	1.0
886	886	1.0
887	887	1.0
888	888	1.0
889	889	1.0
890	890	1.0
891	891	1.0
892	892	1.0
893	893	1.0
894	894	1.0
895	895	1.0
896	896	1.0
897	897	1.0
898	898	1.0
899	899	1.0
900	900	1.0
901	901	1.0
902	902	1.0
903	903	1.0
904	904	1.0
905	905	1.0
906	906	1.0
907	907	1.0
908	908	1.0
909	909	1.0
910	910	1.0
911	911	1.0
912	912	1.0
913	913	1.0
914	914	1.0
915	915	1.0
916	916	1.0
917	917	1.0
918	918	1.0
919	919	1.0
920	920	1.0
921	921	1.0
922	922	1.0
923	923	1.0
924	924	1.0
925	925	1.0
926	926	1.0
927	927	1.0
928	928	1.0
929	929	1.0
930	930	1.0
931	931	1.0
932	932	\N
933	933	\N
934	934	\N
935	935	\N
936	936	\N
937	937	\N
938	938	\N
939	939	\N
940	940	\N
941	941	\N
942	942	\N
943	943	\N
944	944	\N
945	945	\N
946	946	\N
947	947	\N
948	948	\N
949	949	\N
950	950	\N
951	951	\N
952	952	\N
953	953	1.0
954	954	1.0
955	955	1.0
956	956	1.0
957	957	1.0
958	958	1.0
959	959	1.0
960	960	1.0
961	961	1.0
962	962	1.0
963	963	1.0
964	964	1.0
965	965	1.0
966	966	1.0
967	967	1.0
968	968	1.0
969	969	1.0
970	970	1.0
971	971	2.0
972	972	2.0
973	973	2.0
974	974	2.0
975	975	2.0
976	976	\N
977	977	\N
978	978	\N
979	979	1.0
980	980	1.0
981	981	1.0
982	982	1.0
983	983	1.0
984	984	1.0
985	985	1.0
986	986	1.0
987	987	1.0
988	988	1.0
989	989	1.0
990	990	1.0
991	991	1.0
992	992	1.0
993	993	1.0
994	994	1.0
995	995	1.0
996	996	1.0
997	997	1.0
998	998	1.0
999	999	1.0
1000	1000	1.0
1001	1001	1.0
1002	1002	1.0
1003	1003	1.0
1004	1004	1.0
1005	1005	1.0
1006	1006	1.0
1007	1007	1.0
1008	1008	1.0
1009	1009	1.0
1010	1010	1.0
1011	1011	1.0
1012	1012	1.0
1013	1013	1.0
1014	1014	1.0
1015	1015	1.0
1016	1016	1.0
1017	1017	1.0
\.


--
-- TOC entry 5026 (class 0 OID 16443)
-- Dependencies: 233
-- Data for Name: processing_after_ironing; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.processing_after_ironing (id, project_id, lamination, uv_printing, silk_screen_led_uv_sparkle_powder, printing) FROM stdin;
1	1	\N	\N	\N	\N
2	2	\N	\N	\N	\N
3	3	\N	\N	\N	\N
4	4	\N	\N	\N	\N
5	5	\N	\N	\N	\N
6	6	\N	\N	\N	\N
7	7	\N	V	V	V
8	8	\N	V	V	V
9	9	\N	\N	V	\N
10	10	\N	V	V	V
11	11	\N	\N	V	\N
12	12	\N	V	V	\N
13	13	\N	\N	V	\N
14	14	\N	\N	V	\N
15	15	\N	V	V	V
16	16	\N	\N	V	\N
17	17	\N	\N	V	\N
18	18	\N	\N	V	\N
19	19	\N	\N	V	\N
20	20	\N	V	V	\N
21	21	\N	\N	V	\N
22	22	\N	\N	V	\N
23	23	\N	\N	\N	\N
24	24	\N	\N	\N	\N
25	25	\N	\N	\N	\N
26	26	\N	\N	\N	\N
27	27	\N	\N	\N	\N
28	28	\N	\N	\N	\N
29	29	\N	\N	\N	\N
30	30	\N	\N	\N	\N
31	31	\N	\N	\N	\N
32	32	\N	\N	\N	\N
33	33	\N	\N	\N	\N
34	34	\N	\N	\N	\N
35	35	\N	\N	\N	\N
36	36	\N	\N	\N	\N
37	37	\N	\N	\N	\N
38	38	\N	\N	\N	\N
39	39	\N	\N	\N	\N
40	40	\N	\N	\N	\N
41	41	\N	\N	\N	\N
42	42	\N	\N	\N	\N
43	43	\N	\N	\N	\N
44	44	\N	\N	\N	\N
45	45	\N	\N	\N	\N
46	46	\N	\N	\N	\N
47	47	\N	\N	\N	\N
48	48	\N	\N	\N	\N
49	49	\N	\N	\N	\N
50	50	\N	\N	\N	\N
51	51	\N	\N	\N	\N
52	52	\N	\N	\N	\N
53	53	\N	\N	\N	\N
54	54	\N	\N	\N	\N
55	55	\N	\N	\N	\N
56	56	\N	\N	\N	\N
57	57	\N	\N	\N	\N
58	58	\N	\N	\N	\N
59	59	\N	\N	\N	\N
60	60	\N	\N	\N	\N
61	61	\N	\N	\N	V
62	62	\N	\N	\N	V
63	63	\N	\N	\N	\N
64	64	\N	\N	\N	\N
65	65	\N	\N	\N	\N
66	66	\N	\N	\N	\N
67	67	\N	\N	V	V
68	68	\N	V	V	V
69	69	\N	V	V	V
70	70	\N	\N	V	V
71	71	\N	\N	V	V
72	72	\N	V	V	V
73	73	\N	V	V	V
74	74	\N	V	V	V
75	75	\N	\N	V	V
76	76	\N	\N	V	V
77	77	\N	\N	V	V
78	78	\N	\N	V	V
79	79	\N	\N	V	V
80	80	\N	\N	V	V
81	81	\N	\N	V	V
82	82	\N	\N	V	V
83	83	\N	V	V	V
84	84	\N	\N	V	V
85	85	\N	\N	V	V
86	86	\N	\N	V	V
87	87	\N	\N	V	V
88	88	\N	\N	V	V
89	89	\N	\N	V	V
90	90	\N	\N	V	V
91	91	\N	V	V	V
92	92	\N	\N	V	V
93	93	\N	\N	V	V
94	94	\N	\N	V	V
95	95	\N	V	V	V
96	96	\N	\N	V	V
97	97	\N	\N	V	V
98	98	\N	\N	V	V
99	99	\N	\N	V	V
100	100	\N	\N	V	V
101	101	\N	\N	V	V
102	102	\N	\N	V	V
103	103	\N	\N	V	V
104	104	\N	\N	V	V
105	105	\N	\N	\N	\N
106	106	\N	\N	\N	\N
107	107	\N	\N	\N	\N
108	108	\N	\N	\N	\N
109	109	\N	\N	\N	\N
110	110	\N	\N	\N	\N
111	111	\N	\N	\N	\N
112	112	\N	\N	\N	\N
113	113	\N	\N	\N	\N
114	114	\N	\N	\N	\N
115	115	\N	\N	\N	\N
116	116	\N	\N	\N	\N
117	117	\N	\N	\N	\N
118	118	\N	\N	\N	\N
119	119	\N	\N	\N	\N
120	120	\N	\N	\N	\N
121	121	\N	\N	\N	\N
122	122	\N	\N	\N	\N
123	123	\N	\N	\N	\N
124	124	\N	\N	\N	\N
125	125	\N	\N	\N	\N
126	126	\N	\N	\N	\N
127	127	\N	\N	\N	\N
128	128	\N	\N	\N	\N
129	129	\N	\N	\N	\N
130	130	\N	\N	\N	\N
131	131	\N	\N	\N	\N
132	132	\N	\N	\N	\N
133	133	\N	\N	\N	\N
134	134	\N	\N	\N	\N
135	135	\N	\N	\N	\N
136	136	\N	\N	\N	\N
137	137	\N	\N	\N	\N
138	138	\N	\N	\N	\N
139	139	\N	\N	\N	\N
140	140	\N	\N	\N	\N
141	141	\N	\N	\N	\N
142	142	\N	\N	\N	\N
143	143	\N	\N	\N	\N
144	144	\N	\N	\N	\N
145	145	\N	\N	\N	\N
146	146	\N	\N	\N	\N
147	147	\N	\N	\N	\N
148	148	\N	\N	\N	\N
149	149	\N	\N	\N	\N
150	150	\N	\N	\N	\N
151	151	\N	\N	\N	\N
152	152	\N	\N	\N	\N
153	153	\N	\N	V	\N
154	154	\N	\N	V	\N
155	155	\N	\N	V	\N
156	156	\N	\N	V	\N
157	157	\N	\N	V	\N
158	158	\N	V	V	\N
159	159	\N	\N	V	\N
160	160	\N	\N	V	\N
161	161	\N	\N	V	\N
162	162	\N	\N	V	\N
163	163	\N	\N	V	\N
164	164	\N	\N	V	\N
165	165	\N	\N	V	\N
166	166	\N	\N	V	\N
167	167	\N	\N	V	\N
168	168	\N	\N	\N	\N
169	169	\N	\N	\N	\N
170	170	\N	\N	\N	\N
171	171	\N	\N	\N	\N
172	172	\N	\N	\N	\N
173	173	\N	\N	\N	\N
174	174	\N	\N	\N	\N
175	175	\N	\N	\N	\N
176	176	\N	\N	\N	\N
177	177	\N	\N	\N	\N
178	178	\N	\N	\N	\N
179	179	\N	\N	\N	\N
180	180	\N	\N	\N	\N
181	181	\N	\N	\N	\N
182	182	\N	X	\N	\N
183	183	\N	\N	\N	\N
184	184	\N	\N	\N	\N
185	185	\N	\N	\N	\N
186	186	\N	\N	\N	\N
187	187	\N	\N	\N	\N
188	188	\N	\N	\N	\N
189	189	\N	\N	\N	\N
190	190	\N	\N	\N	\N
191	191	\N	\N	\N	\N
192	192	\N	\N	\N	\N
193	193	\N	\N	\N	\N
194	194	\N	\N	\N	\N
195	195	\N	\N	\N	\N
196	196	\N	\N	\N	\N
197	197	\N	\N	\N	\N
198	198	\N	\N	\N	\N
199	199	\N	\N	\N	\N
200	200	\N	\N	\N	\N
201	201	\N	\N	\N	\N
202	202	\N	V	\N	\N
203	203	\N	\N	\N	\N
204	204	\N	\N	\N	\N
205	205	\N	\N	\N	\N
206	206	\N	\N	\N	\N
207	207	\N	\N	\N	\N
208	208	\N	\N	\N	\N
209	209	\N	X	\N	\N
210	210	\N	\N	\N	\N
211	211	\N	\N	\N	\N
212	212	\N	\N	\N	\N
213	213	\N	\N	\N	\N
214	214	\N	\N	\N	\N
215	215	\N	\N	\N	\N
216	216	\N	\N	\N	\N
217	217	\N	\N	\N	\N
218	218	\N	\N	\N	\N
219	219	\N	\N	\N	\N
220	220	\N	\N	\N	\N
221	221	\N	\N	\N	\N
222	222	\N	\N	\N	\N
223	223	\N	\N	\N	\N
224	224	\N	\N	\N	\N
225	225	\N	\N	\N	\N
226	226	\N	\N	\N	\N
227	227	\N	\N	\N	\N
228	228	\N	\N	\N	\N
229	229	\N	\N	\N	\N
230	230	\N	\N	\N	\N
231	231	\N	\N	\N	\N
232	232	\N	\N	\N	\N
233	233	\N	\N	\N	\N
234	234	\N	\N	\N	\N
235	235	\N	V	\N	\N
236	236	\N	\N	\N	\N
237	237	\N	\N	\N	\N
238	238	\N	\N	\N	\N
239	239	\N	\N	\N	\N
240	240	\N	\N	\N	\N
241	241	\N	\N	\N	\N
242	242	\N	\N	\N	\N
243	243	\N	\N	\N	\N
244	244	\N	V	V	V
245	245	\N	\N	V	V
246	246	\N	\N	V	V
247	247	\N	\N	V	\N
248	248	\N	\N	V	V
249	249	\N	\N	V	\N
250	250	\N	V	V	V
251	251	\N	\N	V	V
252	252	\N	X	V	\N
253	253	\N	\N	V	\N
254	254	\N	\N	V	V
255	255	\N	\N	V	V
256	256	\N	\N	\N	V
257	257	\N	V	\N	V
258	258	\N	V	\N	V
259	259	\N	V	\N	V
260	260	\N	X	\N	V
261	261	\N	V	\N	V
262	262	\N	\N	\N	V
263	263	\N	\N	\N	V
264	264	\N	\N	\N	V
265	265	\N	\N	\N	V
266	266	\N	\N	\N	V
267	267	\N	V	\N	V
268	268	\N	\N	\N	V
269	269	\N	\N	\N	\N
270	270	\N	\N	\N	\N
271	271	\N	\N	\N	\N
272	272	\N	V	\N	\N
273	273	\N	V	\N	\N
274	274	\N	V	\N	\N
275	275	\N	V	\N	\N
276	276	\N	\N	\N	\N
277	277	\N	\N	\N	\N
278	278	\N	V	\N	\N
279	279	\N	V	\N	\N
280	280	\N	\N	\N	\N
281	281	\N	\N	\N	\N
282	282	\N	V	\N	\N
283	283	\N	\N	\N	\N
284	284	\N	\N	\N	\N
285	285	\N	V	\N	\N
286	286	\N	\N	\N	\N
287	287	\N	\N	\N	\N
288	288	\N	\N	\N	\N
289	289	\N	\N	\N	\N
290	290	\N	\N	\N	\N
291	291	\N	\N	\N	\N
292	292	\N	\N	\N	\N
293	293	\N	\N	\N	\N
294	294	\N	V	\N	\N
295	295	\N	\N	\N	\N
296	296	\N	\N	\N	\N
297	297	\N	\N	\N	\N
298	298	\N	\N	\N	\N
299	299	\N	\N	\N	\N
300	300	\N	\N	\N	\N
301	301	\N	\N	\N	\N
302	302	\N	\N	\N	\N
303	303	\N	\N	\N	\N
304	304	\N	V	\N	\N
305	305	\N	X	\N	\N
306	306	\N	\N	\N	\N
307	307	\N	V	\N	\N
308	308	\N	\N	\N	\N
309	309	\N	\N	\N	\N
310	310	\N	\N	\N	\N
311	311	\N	\N	\N	\N
312	312	\N	\N	\N	\N
313	313	\N	V	\N	\N
314	314	\N	\N	\N	\N
315	315	\N	X	\N	\N
316	316	\N	V	\N	\N
317	317	\N	V	\N	\N
318	318	\N	\N	\N	\N
319	319	\N	\N	\N	\N
320	320	\N	V	\N	\N
321	321	\N	\N	\N	\N
322	322	\N	\N	\N	\N
323	323	\N	\N	\N	\N
324	324	\N	\N	\N	\N
325	325	\N	\N	\N	\N
326	326	\N	\N	\N	\N
327	327	\N	\N	\N	\N
328	328	\N	\N	\N	\N
329	329	\N	V	\N	\N
330	330	\N	X	\N	\N
331	331	\N	\N	\N	\N
332	332	\N	\N	\N	\N
333	333	\N	\N	\N	\N
334	334	\N	\N	\N	\N
335	335	\N	X	\N	\N
336	336	\N	V	\N	\N
337	337	\N	V	\N	\N
338	338	\N	X	\N	\N
339	339	\N	\N	\N	\N
340	340	\N	\N	\N	\N
341	341	\N	V	\N	\N
342	342	\N	\N	\N	\N
343	343	\N	\N	\N	\N
344	344	\N	\N	\N	\N
345	345	\N	\N	\N	\N
346	346	\N	V	\N	\N
347	347	\N	V	\N	\N
348	348	\N	\N	\N	\N
349	349	\N	\N	\N	\N
350	350	\N	\N	\N	\N
351	351	\N	\N	\N	\N
352	352	\N	X	\N	\N
353	353	\N	\N	\N	\N
354	354	\N	\N	\N	\N
355	355	\N	\N	\N	\N
356	356	\N	\N	\N	\N
357	357	\N	\N	\N	\N
358	358	\N	\N	\N	\N
359	359	\N	\N	\N	\N
360	360	\N	\N	\N	\N
361	361	\N	\N	\N	\N
362	362	\N	X	\N	\N
363	363	\N	\N	\N	\N
364	364	\N	\N	\N	\N
365	365	\N	\N	\N	\N
366	366	\N	\N	\N	\N
367	367	\N	V	\N	\N
368	368	\N	\N	\N	\N
369	369	\N	\N	\N	\N
370	370	\N	\N	\N	\N
371	371	\N	V	\N	\N
372	372	\N	\N	\N	\N
373	373	\N	\N	\N	\N
374	374	\N	\N	\N	\N
375	375	\N	V	\N	\N
376	376	\N	\N	\N	\N
377	377	\N	\N	\N	\N
378	378	\N	\N	\N	\N
379	379	\N	\N	\N	\N
380	380	\N	\N	\N	\N
381	381	\N	\N	\N	\N
382	382	\N	\N	\N	\N
383	383	\N	\N	\N	\N
384	384	\N	\N	\N	\N
385	385	\N	\N	\N	\N
386	386	\N	\N	\N	\N
387	387	\N	\N	\N	\N
388	388	\N	\N	\N	\N
389	389	\N	\N	\N	\N
390	390	\N	\N	\N	\N
391	391	\N	V	\N	\N
392	392	\N	\N	\N	\N
393	393	\N	\N	\N	\N
394	394	\N	\N	\N	\N
395	395	\N	\N	\N	\N
396	396	\N	\N	\N	\N
397	397	\N	V	\N	\N
398	398	\N	V	\N	\N
399	399	\N	V	\N	\N
400	400	\N	\N	\N	\N
401	401	\N	\N	\N	\N
402	402	\N	\N	\N	\N
403	403	\N	\N	\N	\N
404	404	\N	\N	\N	\N
405	405	\N	\N	\N	\N
406	406	\N	\N	\N	\N
407	407	\N	\N	\N	\N
408	408	\N	\N	\N	\N
409	409	\N	\N	\N	\N
410	410	\N	\N	\N	\N
411	411	\N	\N	\N	\N
412	412	\N	\N	\N	\N
413	413	\N	\N	\N	\N
414	414	\N	\N	\N	\N
415	415	\N	\N	\N	\N
416	416	\N	\N	\N	\N
417	417	\N	\N	\N	\N
418	418	\N	\N	\N	\N
419	419	\N	\N	\N	\N
420	420	\N	\N	\N	\N
421	421	\N	\N	\N	\N
422	422	\N	\N	\N	\N
423	423	\N	\N	\N	\N
424	424	\N	\N	\N	\N
425	425	\N	\N	\N	\N
426	426	\N	\N	\N	\N
427	427	\N	\N	\N	\N
428	428	\N	\N	\N	\N
429	429	\N	\N	\N	\N
430	430	\N	\N	\N	\N
431	431	\N	\N	\N	\N
432	432	\N	V	\N	\N
433	433	\N	\N	\N	\N
434	434	\N	\N	\N	\N
435	435	\N	\N	\N	\N
436	436	\N	\N	\N	\N
437	437	\N	\N	\N	\N
438	438	\N	\N	\N	\N
439	439	\N	\N	\N	V
440	440	\N	V	\N	\N
441	441	\N	\N	\N	\N
442	442	\N	V	\N	\N
443	443	\N	V	\N	\N
444	444	\N	\N	\N	\N
445	445	\N	\N	\N	\N
446	446	\N	V	\N	\N
447	447	\N	\N	\N	\N
448	448	\N	\N	\N	\N
449	449	\N	V	\N	\N
450	450	\N	\N	\N	\N
451	451	\N	\N	\N	V
452	452	\N	\N	\N	\N
453	453	\N	\N	\N	V
454	454	\N	\N	\N	\N
455	455	\N	\N	\N	\N
456	456	\N	\N	\N	\N
457	457	\N	\N	\N	\N
458	458	\N	\N	\N	V
459	459	\N	\N	\N	\N
460	460	\N	\N	\N	\N
461	461	\N	\N	\N	\N
462	462	\N	\N	\N	\N
463	463	\N	\N	\N	\N
464	464	\N	\N	\N	\N
465	465	\N	\N	\N	\N
466	466	\N	\N	\N	\N
467	467	\N	\N	\N	\N
468	468	\N	\N	\N	\N
469	469	\N	X	\N	\N
470	470	\N	\N	\N	\N
471	471	\N	\N	\N	\N
472	472	\N	\N	\N	\N
473	473	\N	\N	\N	\N
474	474	\N	\N	\N	\N
475	475	\N	\N	\N	\N
476	476	\N	\N	\N	\N
477	477	\N	\N	\N	\N
478	478	\N	\N	\N	\N
479	479	\N	\N	\N	\N
480	480	\N	\N	\N	\N
481	481	\N	\N	\N	\N
482	482	\N	\N	\N	\N
483	483	\N	\N	\N	\N
484	484	\N	\N	\N	\N
485	485	\N	\N	\N	\N
486	486	\N	\N	\N	\N
487	487	\N	\N	\N	\N
488	488	\N	\N	\N	\N
489	489	\N	\N	\N	\N
490	490	\N	\N	\N	\N
491	491	\N	\N	\N	\N
492	492	\N	\N	\N	\N
493	493	\N	\N	\N	\N
494	494	\N	\N	\N	\N
495	495	\N	\N	\N	\N
496	496	\N	\N	\N	\N
497	497	\N	\N	\N	\N
498	498	\N	\N	\N	\N
499	499	\N	\N	\N	\N
500	500	\N	\N	\N	\N
501	501	\N	\N	\N	\N
502	502	\N	\N	\N	\N
503	503	\N	\N	\N	\N
504	504	\N	\N	\N	\N
505	505	\N	\N	\N	\N
506	506	\N	\N	\N	\N
507	507	\N	\N	\N	\N
508	508	\N	\N	\N	\N
509	509	\N	\N	\N	\N
510	510	\N	\N	\N	\N
511	511	\N	\N	\N	\N
512	512	\N	\N	\N	\N
513	513	\N	\N	\N	\N
514	514	\N	\N	\N	\N
515	515	\N	\N	\N	\N
516	516	\N	\N	\N	\N
517	517	\N	\N	\N	\N
518	518	\N	\N	\N	\N
519	519	\N	\N	\N	\N
520	520	\N	\N	\N	\N
521	521	\N	\N	\N	\N
522	522	\N	\N	\N	\N
523	523	\N	\N	\N	\N
524	524	\N	\N	\N	\N
525	525	\N	\N	\N	\N
526	526	\N	\N	\N	\N
527	527	\N	\N	\N	\N
528	528	\N	\N	\N	\N
529	529	\N	\N	\N	\N
530	530	\N	\N	\N	\N
531	531	\N	\N	\N	\N
532	532	\N	\N	\N	\N
533	533	\N	\N	\N	\N
534	534	\N	\N	\N	\N
535	535	\N	\N	\N	\N
536	536	\N	\N	\N	\N
537	537	\N	\N	\N	\N
538	538	\N	\N	\N	\N
539	539	\N	\N	\N	\N
540	540	\N	\N	\N	\N
541	541	\N	\N	\N	\N
542	542	\N	\N	\N	\N
543	543	\N	\N	\N	\N
544	544	\N	\N	\N	\N
545	545	\N	\N	\N	\N
546	546	\N	\N	\N	\N
547	547	\N	\N	\N	\N
548	548	\N	\N	\N	\N
549	549	\N	\N	\N	\N
550	550	\N	\N	\N	\N
551	551	\N	\N	\N	\N
552	552	\N	\N	\N	\N
553	553	\N	\N	\N	\N
554	554	\N	\N	\N	\N
555	555	\N	\N	\N	\N
556	556	\N	\N	\N	\N
557	557	\N	\N	\N	\N
558	558	\N	\N	\N	\N
559	559	\N	\N	\N	\N
560	560	\N	\N	\N	\N
561	561	\N	\N	\N	\N
562	562	\N	\N	\N	\N
563	563	\N	\N	\N	\N
564	564	\N	\N	\N	\N
565	565	\N	\N	\N	\N
566	566	\N	\N	\N	\N
567	567	\N	\N	\N	\N
568	568	\N	\N	\N	\N
569	569	\N	\N	\N	\N
570	570	\N	\N	\N	\N
571	571	\N	\N	\N	\N
572	572	\N	\N	\N	\N
573	573	\N	\N	\N	\N
574	574	\N	\N	\N	\N
575	575	\N	\N	\N	\N
576	576	\N	\N	\N	\N
577	577	\N	\N	\N	\N
578	578	\N	\N	\N	\N
579	579	\N	\N	\N	\N
580	580	\N	\N	\N	\N
581	581	\N	\N	\N	\N
582	582	\N	\N	\N	\N
583	583	\N	\N	\N	\N
584	584	\N	\N	\N	\N
585	585	\N	\N	\N	\N
586	586	\N	\N	\N	\N
587	587	\N	\N	\N	\N
588	588	\N	\N	\N	\N
589	589	\N	\N	\N	\N
590	590	\N	\N	\N	\N
591	591	\N	\N	\N	\N
592	592	\N	\N	\N	\N
593	593	\N	\N	\N	\N
594	594	\N	\N	\N	\N
595	595	\N	\N	\N	\N
596	596	\N	\N	\N	\N
597	597	\N	\N	\N	\N
598	598	\N	\N	\N	\N
599	599	\N	\N	\N	\N
600	600	\N	\N	\N	\N
601	601	\N	\N	\N	\N
602	602	\N	\N	\N	\N
603	603	\N	\N	\N	\N
604	604	\N	\N	\N	\N
605	605	\N	\N	\N	\N
606	606	\N	\N	\N	\N
607	607	\N	\N	\N	\N
608	608	\N	\N	\N	\N
609	609	\N	\N	\N	\N
610	610	\N	\N	\N	\N
611	611	\N	\N	\N	\N
612	612	\N	\N	\N	\N
613	613	\N	\N	\N	\N
614	614	\N	\N	\N	\N
615	615	\N	\N	\N	\N
616	616	\N	\N	\N	\N
617	617	\N	\N	\N	\N
618	618	\N	\N	\N	\N
619	619	\N	\N	\N	\N
620	620	\N	\N	\N	\N
621	621	\N	\N	\N	\N
622	622	\N	\N	\N	\N
623	623	\N	\N	\N	\N
624	624	\N	\N	\N	\N
625	625	\N	\N	\N	\N
626	626	\N	\N	\N	\N
627	627	\N	\N	\N	\N
628	628	\N	\N	\N	\N
629	629	\N	\N	\N	\N
630	630	\N	\N	\N	\N
631	631	\N	\N	\N	\N
632	632	\N	\N	\N	\N
633	633	\N	\N	\N	\N
634	634	\N	\N	\N	\N
635	635	\N	\N	\N	\N
636	636	\N	\N	\N	\N
637	637	\N	\N	\N	\N
638	638	\N	\N	\N	\N
639	639	\N	\N	\N	\N
640	640	\N	\N	\N	\N
641	641	\N	\N	\N	\N
642	642	\N	\N	\N	\N
643	643	\N	\N	\N	\N
644	644	\N	\N	\N	\N
645	645	\N	\N	\N	\N
646	646	\N	\N	\N	\N
647	647	\N	\N	\N	\N
648	648	\N	\N	\N	\N
649	649	\N	\N	\N	\N
650	650	\N	\N	\N	\N
651	651	\N	\N	\N	\N
652	652	\N	\N	\N	\N
653	653	\N	\N	\N	\N
654	654	\N	\N	\N	\N
655	655	\N	\N	\N	\N
656	656	\N	\N	\N	\N
657	657	\N	\N	\N	\N
658	658	\N	\N	\N	\N
659	659	\N	\N	\N	\N
660	660	\N	\N	\N	\N
661	661	\N	\N	\N	\N
662	662	\N	\N	\N	\N
663	663	\N	\N	\N	\N
664	664	\N	\N	\N	\N
665	665	\N	\N	\N	\N
666	666	\N	\N	\N	\N
667	667	\N	\N	\N	\N
668	668	\N	\N	\N	\N
669	669	\N	\N	\N	\N
670	670	\N	\N	\N	\N
671	671	\N	\N	\N	\N
672	672	\N	\N	\N	\N
673	673	\N	\N	\N	\N
674	674	\N	\N	\N	\N
675	675	\N	\N	\N	\N
676	676	\N	\N	\N	\N
677	677	\N	\N	\N	\N
678	678	\N	\N	\N	\N
679	679	\N	\N	\N	\N
680	680	\N	\N	\N	\N
681	681	\N	\N	\N	\N
682	682	\N	\N	\N	\N
683	683	\N	\N	\N	\N
684	684	\N	\N	\N	\N
685	685	\N	\N	\N	\N
686	686	\N	\N	\N	\N
687	687	\N	\N	\N	\N
688	688	\N	\N	\N	\N
689	689	\N	\N	\N	\N
690	690	\N	\N	\N	\N
691	691	\N	\N	\N	\N
692	692	\N	\N	\N	\N
693	693	\N	\N	\N	\N
694	694	\N	\N	\N	\N
695	695	\N	\N	\N	\N
696	696	\N	\N	\N	\N
697	697	\N	\N	\N	\N
698	698	\N	\N	\N	\N
699	699	\N	\N	\N	\N
700	700	\N	\N	\N	\N
701	701	\N	\N	\N	\N
702	702	\N	\N	\N	\N
703	703	\N	\N	\N	\N
704	704	\N	\N	\N	\N
705	705	\N	\N	\N	\N
706	706	\N	\N	\N	\N
707	707	\N	\N	\N	\N
708	708	\N	\N	\N	\N
709	709	\N	\N	\N	\N
710	710	\N	\N	\N	\N
711	711	\N	\N	\N	\N
712	712	\N	\N	\N	\N
713	713	\N	\N	\N	\N
714	714	\N	\N	\N	\N
715	715	\N	\N	\N	\N
716	716	\N	\N	\N	\N
717	717	\N	\N	\N	\N
718	718	\N	\N	\N	\N
719	719	\N	\N	\N	\N
720	720	\N	\N	\N	\N
721	721	\N	\N	\N	\N
722	722	\N	\N	\N	\N
723	723	\N	\N	\N	\N
724	724	\N	\N	\N	\N
725	725	\N	\N	\N	\N
726	726	\N	\N	\N	\N
727	727	\N	\N	\N	\N
728	728	\N	\N	\N	\N
729	729	\N	\N	\N	\N
730	730	\N	\N	\N	\N
731	731	\N	\N	\N	\N
732	732	\N	\N	\N	\N
733	733	\N	\N	\N	\N
734	734	\N	\N	V	\N
735	735	\N	\N	V	\N
736	736	\N	\N	V	\N
737	737	\N	\N	V	\N
738	738	\N	\N	V	\N
739	739	\N	\N	V	\N
740	740	\N	\N	V	\N
741	741	\N	\N	V	\N
742	742	\N	\N	V	\N
743	743	\N	\N	V	\N
744	744	\N	\N	V	\N
745	745	\N	V	V	\N
746	746	\N	\N	V	\N
747	747	\N	\N	V	\N
748	748	\N	V	V	\N
749	749	\N	\N	V	\N
750	750	\N	\N	V	\N
751	751	\N	\N	V	\N
752	752	\N	\N	V	\N
753	753	\N	\N	V	\N
754	754	\N	\N	V	\N
755	755	\N	\N	V	\N
756	756	\N	\N	V	\N
757	757	\N	\N	V	\N
758	758	\N	\N	V	\N
759	759	\N	\N	V	\N
760	760	\N	\N	V	\N
761	761	\N	\N	V	\N
762	762	\N	\N	V	\N
763	763	\N	\N	V	\N
764	764	\N	\N	V	\N
765	765	\N	\N	V	\N
766	766	\N	\N	V	\N
767	767	\N	\N	V	\N
768	768	\N	\N	V	\N
769	769	\N	\N	V	\N
770	770	\N	\N	V	\N
771	771	\N	\N	V	\N
772	772	\N	\N	V	\N
773	773	\N	\N	V	\N
774	774	\N	\N	V	\N
775	775	\N	\N	V	\N
776	776	\N	\N	V	\N
777	777	\N	\N	V	\N
778	778	\N	\N	V	\N
779	779	\N	\N	V	\N
780	780	\N	\N	V	\N
781	781	\N	\N	V	\N
782	782	\N	\N	V	\N
783	783	\N	\N	V	\N
784	784	\N	\N	V	\N
785	785	\N	\N	V	\N
786	786	\N	\N	V	\N
787	787	\N	\N	V	\N
788	788	\N	\N	V	\N
789	789	\N	\N	V	\N
790	790	\N	\N	V	\N
791	791	\N	\N	V	\N
792	792	\N	\N	V	\N
793	793	\N	\N	V	\N
794	794	\N	\N	V	\N
795	795	\N	\N	V	\N
796	796	\N	\N	V	\N
797	797	\N	\N	V	\N
798	798	\N	\N	V	\N
799	799	\N	\N	\N	\N
800	800	\N	\N	\N	\N
801	801	\N	\N	\N	\N
802	802	\N	\N	\N	\N
803	803	\N	\N	\N	\N
804	804	\N	\N	\N	\N
805	805	\N	\N	\N	\N
806	806	\N	\N	\N	\N
807	807	\N	\N	\N	\N
808	808	\N	\N	\N	\N
809	809	\N	\N	\N	\N
810	810	\N	\N	\N	\N
811	811	\N	\N	\N	\N
812	812	\N	\N	\N	\N
813	813	\N	\N	\N	\N
814	814	\N	\N	\N	\N
815	815	\N	\N	\N	\N
816	816	\N	\N	\N	\N
817	817	\N	\N	\N	\N
818	818	\N	\N	\N	\N
819	819	\N	\N	\N	\N
820	820	\N	\N	\N	\N
821	821	\N	\N	\N	\N
822	822	\N	\N	\N	\N
823	823	\N	\N	\N	\N
824	824	\N	\N	\N	\N
825	825	\N	\N	\N	\N
826	826	\N	\N	\N	\N
827	827	\N	\N	\N	\N
828	828	\N	\N	\N	\N
829	829	\N	\N	\N	\N
830	830	\N	\N	\N	\N
831	831	\N	\N	\N	\N
832	832	\N	\N	\N	\N
833	833	\N	\N	\N	\N
834	834	\N	\N	\N	\N
835	835	\N	\N	\N	\N
836	836	\N	\N	\N	\N
837	837	\N	\N	\N	\N
838	838	\N	\N	\N	\N
839	839	\N	\N	\N	\N
840	840	\N	\N	\N	\N
841	841	\N	\N	\N	\N
842	842	\N	V	\N	\N
843	843	\N	\N	\N	\N
844	844	\N	\N	\N	\N
845	845	\N	\N	\N	\N
846	846	\N	\N	\N	\N
847	847	\N	\N	\N	\N
848	848	\N	\N	\N	\N
849	849	\N	\N	\N	\N
850	850	\N	\N	\N	\N
851	851	\N	\N	\N	\N
852	852	\N	\N	\N	\N
853	853	\N	\N	\N	\N
854	854	\N	\N	\N	\N
855	855	\N	\N	\N	\N
856	856	\N	\N	\N	\N
857	857	\N	\N	\N	\N
858	858	\N	\N	\N	\N
859	859	\N	\N	\N	\N
860	860	\N	\N	V	\N
861	861	\N	\N	V	\N
862	862	\N	\N	V	\N
863	863	\N	\N	V	\N
864	864	\N	\N	V	\N
865	865	\N	\N	V	\N
866	866	\N	\N	V	\N
867	867	\N	\N	V	\N
868	868	\N	V	V	\N
869	869	\N	\N	V	\N
870	870	\N	V	V	\N
871	871	\N	\N	V	\N
872	872	\N	\N	V	\N
873	873	\N	\N	V	\N
874	874	\N	\N	V	\N
875	875	\N	\N	V	\N
876	876	\N	\N	V	\N
877	877	\N	\N	V	\N
878	878	\N	\N	V	\N
879	879	\N	\N	V	\N
880	880	\N	\N	V	\N
881	881	\N	\N	V	\N
882	882	\N	\N	V	\N
883	883	\N	\N	V	\N
884	884	\N	\N	V	\N
885	885	\N	\N	V	\N
886	886	\N	\N	V	\N
887	887	\N	\N	V	\N
888	888	\N	\N	V	\N
889	889	\N	\N	V	\N
890	890	\N	\N	V	\N
891	891	\N	\N	V	\N
892	892	\N	\N	V	\N
893	893	\N	\N	V	\N
894	894	\N	\N	V	\N
895	895	\N	\N	V	\N
896	896	\N	\N	V	\N
897	897	\N	\N	V	\N
898	898	\N	\N	V	\N
899	899	\N	\N	V	\N
900	900	\N	\N	V	\N
901	901	\N	V	\N	\N
902	902	\N	V	\N	\N
903	903	\N	V	\N	\N
904	904	\N	V	\N	\N
905	905	\N	V	\N	\N
906	906	\N	V	\N	\N
907	907	\N	V	\N	\N
908	908	\N	\N	\N	\N
909	909	\N	V	\N	\N
910	910	\N	V	\N	\N
911	911	\N	V	\N	\N
912	912	\N	V	\N	\N
913	913	\N	\N	\N	\N
914	914	\N	V	\N	\N
915	915	\N	\N	\N	\N
916	916	\N	V	\N	\N
917	917	\N	V	\N	\N
918	918	\N	V	\N	\N
919	919	\N	V	\N	\N
920	920	\N	\N	\N	\N
921	921	\N	V	\N	\N
922	922	\N	\N	\N	\N
923	923	\N	\N	\N	\N
924	924	\N	\N	\N	\N
925	925	\N	\N	\N	\N
926	926	\N	\N	\N	\N
927	927	\N	V	\N	\N
928	928	\N	\N	\N	\N
929	929	\N	\N	\N	\N
930	930	\N	\N	\N	\N
931	931	\N	\N	\N	\N
932	932	\N	\N	\N	\N
933	933	\N	\N	\N	\N
934	934	\N	\N	\N	\N
935	935	\N	\N	\N	\N
936	936	\N	\N	\N	\N
937	937	\N	\N	\N	\N
938	938	\N	\N	\N	\N
939	939	\N	\N	\N	\N
940	940	\N	\N	\N	\N
941	941	\N	\N	\N	\N
942	942	\N	\N	\N	\N
943	943	\N	\N	\N	\N
944	944	\N	\N	\N	\N
945	945	\N	\N	\N	\N
946	946	\N	\N	V	\N
947	947	\N	\N	V	\N
948	948	\N	\N	V	\N
949	949	\N	\N	\N	\N
950	950	\N	\N	\N	\N
951	951	\N	\N	\N	\N
952	952	\N	\N	\N	\N
953	953	\N	\N	\N	\N
954	954	\N	\N	\N	\N
955	955	\N	\N	\N	\N
956	956	\N	\N	\N	\N
957	957	\N	\N	\N	\N
958	958	\N	\N	\N	\N
959	959	\N	\N	\N	\N
960	960	\N	\N	\N	\N
961	961	\N	\N	\N	\N
962	962	\N	\N	\N	\N
963	963	\N	\N	\N	\N
964	964	\N	\N	\N	\N
965	965	\N	\N	\N	\N
966	966	\N	\N	\N	\N
967	967	\N	\N	\N	\N
968	968	\N	\N	\N	\N
969	969	\N	\N	\N	\N
970	970	\N	\N	\N	\N
971	971	\N	\N	\N	\N
972	972	\N	\N	\N	\N
973	973	\N	\N	\N	\N
974	974	\N	\N	\N	\N
975	975	\N	\N	\N	\N
976	976	\N	\N	\N	\N
977	977	\N	\N	\N	\N
978	978	\N	\N	\N	\N
979	979	\N	\N	\N	\N
980	980	\N	\N	\N	\N
981	981	\N	\N	\N	\N
982	982	\N	\N	\N	\N
983	983	\N	\N	\N	\N
984	984	\N	\N	\N	\N
985	985	\N	\N	\N	\N
986	986	\N	\N	\N	\N
987	987	\N	\N	\N	\N
988	988	\N	\N	\N	\N
989	989	\N	\N	\N	\N
990	990	\N	\N	\N	\N
991	991	\N	\N	\N	\N
992	992	\N	\N	\N	\N
993	993	\N	\N	\N	\N
994	994	\N	\N	\N	\N
995	995	\N	\N	\N	\N
996	996	\N	\N	\N	\N
997	997	\N	\N	\N	\N
998	998	\N	\N	\N	\N
999	999	\N	\N	\N	\N
1000	1000	\N	\N	\N	\N
1001	1001	\N	\N	\N	\N
1002	1002	\N	\N	\N	\N
1003	1003	\N	\N	\N	\N
1004	1004	\N	\N	\N	\N
1005	1005	\N	\N	\N	\N
1006	1006	\N	\N	\N	\N
1007	1007	\N	\N	\N	\N
1008	1008	\N	\N	\N	\N
1009	1009	\N	\N	\N	\N
1010	1010	\N	\N	\N	\N
1011	1011	\N	\N	\N	\N
1012	1012	\N	\N	\N	\N
1013	1013	\N	\N	\N	\N
1014	1014	\N	\N	\N	\N
1015	1015	\N	\N	\N	\N
1016	1016	\N	\N	\N	\N
1017	1017	\N	\N	\N	\N
\.


--
-- TOC entry 5028 (class 0 OID 16447)
-- Dependencies: 235
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, name, model_number, material_number, hot_stamping_paper_type) FROM stdin;
1	432	432018	RRFOI002000111	\N
2	595	595004	RRFOI002000478	\N
3	595	595002	RRFOI002000792	\N
4	A19	A19-3COPPER	RRFOI002000943	\N
5	A19	A19-3PINK	RRFOI002000977	\N
6	A19	A19-3MS	RRFOI002001046	\N
7	A23	A23-1G	RRFOI002000095	\N
8	A23	A23-1S	RRFOI002000096	\N
9	A23	A23-2RED	RRFOI002000097	\N
10	A23	A23-3MS	RRFOI002000098	\N
11	A23	A23-6G	RRFOI002000099	\N
12	A23	A23-BLACK	RRFOI002000100	\N
13	A23	A23-GREEN	RRFOI002000101	\N
14	A23	A23-PINK	RRFOI002000102	\N
15	A23	A23-4MG	RRFOI002000107	\N
16	A23	A23-GUNMETAL	RRFOI002000109	\N
17	A23	A23-TSF-N	RRFOI002000112	\N
18	A23	A23-429	RRFOI002000262	\N
19	A23	A23-368G	RRFOI002000743	\N
20	A23	A23-8BLUE	RRFOI002000798	\N
21	A23	A23-3COPPER	RRFOI002000806	\N
22	A23	A23-368G	RRFOI002000835	\N
23	A38	A38-480	RRFOI002000103	\N
24	A38	A38-923K	RRFOI002000114	\N
25	A38	A38-309K	RRFOI002000124	\N
26	AF	706AF-538	RRFOI002000127	\N
27	AF	AF502	RRFOI002000227	\N
28	AF	705AF-572	RRFOI002000264	\N
29	AF	AF503	RRFOI002000283	\N
30	AF	AF592	RRFOI002000285	\N
31	AF	AF533	RRFOI002000287	\N
32	AF	AF593	RRFOI002000423	\N
33	AF	AF538A	RRFOI002000426	\N
34	AF	AF536	RRFOI002000437	\N
35	AF	AF502	RRFOI002000499	\N
36	AF	AF536A	RRFOI002000595	\N
37	AF	AF538	RRFOI002000772	\N
38	AF	AF572	RRFOI002000773	\N
39	AF	AF540	RRFOI002000828	\N
40	AF	705AF-501	RRFOI002000830	\N
41	AF	AF516	RRFOI002000833	\N
42	AF	AF595	RRFOI002000839	\N
43	AF	AF574A	RRFOI002000850	\N
44	AF	AF535	RRFOI002000892	\N
45	AF	AF591	RRFOI002000928	\N
46	AF	AF396A	RRFOI002000938	\N
47	AF	AF530	RRFOI002000954	\N
48	AF	AF540S	RRFOI002001042	\N
49	AF	AF576	RRFOI002001051	\N
50	AF	AF532	RRFOI002001069	\N
51	CW	CW923K	RRFOI002000195	\N
52	DW	DW716	RRFOI002000132	\N
53	DW	DW723A	RRFOI002000133	\N
54	DW	DW771	RRFOI002000140	\N
55	DW	DW759	RRFOI002000346	\N
56	DW	DW773	RRFOI002000379	\N
57	DW	DW713	RRFOI002000759	\N
58	DW	DW762	RRFOI002000821	\N
59	DW	DW763	RRFOI002000832	\N
60	DW	DW772	RRFOI002000947	\N
61	E8	E8-S	RRFOI002000004	普通金纸
62	E8	E8-S	RRFOI002000849	普通金纸
63	FA	FA730	RRFOI002000927	\N
64	FA	FA809	RRFOI002000936	\N
65	FA	FA420	RRFOI002000979	\N
66	FF-12	FF-12	RRFOI002000213	\N
67	G1	G1-26W-Y55	RRFOI001000167	普通金纸
68	G1	G1-G48	RRFOI002000158	普通金纸
69	G1	G1-R09	RRFOI002000159	普通金纸
70	G1	G1-B55	RRFOI002000160	普通金纸
71	G1	G1-B54	RRFOI002000161	普通金纸
72	G1	G1-R10	RRFOI002000166	普通金纸
73	G1	G1-G49	RRFOI002000167	普通金纸
74	G1	G1-Y21	RRFOI002000173	普通金纸
75	G1	G1-M86-S	RRFOI002000187	普通金纸
76	G1	G1-5734	RRFOI002000232	普通金纸
77	G1	G1-0174	RRFOI002000235	普通金纸
78	G1	G1-0635	RRFOI002000238	普通金纸
79	G1	G1-0700	RRFOI002000240	普通金纸
80	G1	G1-0205	RRFOI002000241	普通金纸
81	G1	G1-0201	RRFOI002000250	普通金纸
82	G1	G1-Y30	RRFOI002000252	普通金纸
83	G1	G1-R79	RRFOI002000302	普通金纸
84	G1	G1-D67	RRFOI002000474	普通金纸
85	G1	G1-5736	RRFOI002000549	普通金纸
86	G1	G1B48	RRFOI002000780	普通金纸
87	G1	G1-M87	RRFOI002000781	普通金纸
88	G1	G1-Y24	RRFOI002000856	普通金纸
89	G1	G1-Y07	RRFOI002000858	普通金纸
90	G1	G1-M82	RRFOI002000863	普通金纸
91	G1	G1-B48	RRFOI002000871	普通金纸
92	G1	G1-R20	RRFOI002000872	普通金纸
93	G1	G1-S20	RRFOI002000876	普通金纸
94	G1	G1-Y30	RRFOI002000878	普通金纸
95	G1	G1-Y17	RRFOI002000881	普通金纸
96	G1	G1-Y52	RRFOI002000883	普通金纸
97	G1	G1-M77	RRFOI002000884	普通金纸
98	G1	G1-R11	RRFOI002000888	普通金纸
99	G1	G1-M13	RRFOI002000893	普通金纸
100	G1	G1-Y25	RRFOI002000896	普通金纸
101	G1	G1-M89	RRFOI002000897	普通金纸
102	G1	G1-R16	RRFOI002000905	普通金纸
103	G1	G1-B49	RRFOI002000931	普通金纸
104	G1	G1-G64	RRFOI002001052	普通金纸
105	G6	G6-27W-R40	RRFOI001000004	鐳射燙金紙
106	G6	G6-Y98-01W	RRFOI001000005	鐳射燙金紙
107	G6	G6-29W-S20	RRFOI001000021	鐳射燙金紙
108	G6	G6-26W-B63	RRFOI001000031	鐳射燙金紙
109	G6	G6-26W-R40	RRFOI001000040	鐳射燙金紙
110	G6	G6-113-S20	RRFOI001000055	鐳射燙金紙
111	G6	G6-26W-G48	RRFOI001000056	鐳射燙金紙
112	G6	G6-26W-Y55	RRFOI001000057	鐳射燙金紙
113	G6	G6-27W-S20	RRFOI001000064	鐳射燙金紙
114	G6	G6-S20-26W	RRFOI001000066	鐳射燙金紙
115	G6	G6-01WP-S20	RRFOI001000090	鐳射燙金紙
116	G6	G6-S20-76W	RRFOI001000132	鐳射燙金紙
117	G6	G6-26W-Y93	RRFOI001000199	鐳射燙金紙
118	G6	G6-Y89-27W	RRFOI001000207	鐳射燙金紙
119	G6	G6-S20-74W	RRFOI001000226	鐳射燙金紙
120	G6	G6-S20-75W	RRFOI001000227	鐳射燙金紙
121	G6	G6-26W-Y55	RRFOI001000235	鐳射燙金紙
122	G6	G6-S20-26W	RRFOI001000236	鐳射燙金紙
123	G6	G6-B61-05	RRFOI001000242	鐳射燙金紙
124	GB	GB711	RRFOI002000196	\N
125	GB	GB730	RRFOI002000199	\N
126	GB	GB707A	RRFOI002000236	\N
127	GB	GB709	RRFOI002000254	\N
128	GB	GB732	RRFOI002000260	\N
129	GB	GB705	RRFOI002000307	\N
130	GB	GB710	RRFOI002000343	\N
131	GB	GB701	RRFOI002000511	\N
132	GB	GB704	RRFOI002000523	\N
133	GB	GB708	RRFOI002000538	\N
134	GB	GB703	RRFOI002000646	\N
135	GB	GB735	RRFOI002000746	\N
136	GB	GB731	RRFOI002000770	\N
137	GB	GB707	RRFOI002000776	\N
138	GB	GB574	RRFOI002000791	\N
139	GB	GB705B	RRFOI002000820	\N
140	GB	GB964	RRFOI002000826	\N
141	GB	GB777	RRFOI002000840	\N
142	GB	GB705A	RRFOI002000864	\N
143	GB	GB701A	RRFOI002000877	\N
144	GB	GB577	RRFOI002000890	\N
145	GB	GB735	RRFOI002000925	\N
146	GB	GB573	RRFOI002000949	\N
147	GB	GB702	RRFOI002000952	\N
148	GB	GB677A	RRFOI002001012	\N
149	GB	GB591	RRFOI002001016	\N
150	GFM14066	GFM14066-385	RRFOI002000001	\N
151	GFM14066	GFM14066-AL	RRFOI002000113	\N
152	GFM14066	GFM14066-385	RRFOI002001017	\N
153	GN	GN-R16	RRFOI002000141	普通耐磨
154	GN	GN-B48-A	RRFOI002000162	普通耐磨
155	GN	GN-5734	RRFOI002000233	普通耐磨
156	GN	GN-0205	RRFOI002000234	普通耐磨
157	GN	GN-0201	RRFOI002000249	普通耐磨
158	GN	GN-0182	RRFOI002000255	普通耐磨
159	GN	GN-0700	RRFOI002000275	普通耐磨
160	GN	GN0720	RRFOI002000297	普通耐磨
161	GN	GN-0201	RRFOI002000431	普通耐磨
162	GN	GN-Y17	RRFOI002000432	普通耐磨
163	GN	GN-R15-A	RRFOI002000475	普通耐磨
164	GN	GN2-Y17	RRFOI002000494	普通耐磨
165	GN	GNB60-A	RRFOI002000779	普通耐磨
166	GN	GN-G48	RRFOI002000887	普通耐磨
167	GN	GN-D67-A	RRFOI002000889	普通耐磨
168	GN2	GN2-2152	RRFOI002000248	普通耐磨
169	GN2	GN2-0205	RRFOI002000256	普通耐磨
170	GN2	GN2-0174	RRFOI002000272	普通耐磨
171	GN2	GN2-0183	RRFOI002000276	普通耐磨
172	GN2	GN2-R15-A	RRFOI002000282	普通耐磨
173	GN2	GN2-0201	RRFOI002000304	普通耐磨
174	GN2	GN2-Y17	RRFOI002000508	普通耐磨
175	GN2	GN2-R15-A	RRFOI002000785	普通耐磨
176	GTS	GTS-377	RRFOI002000181	\N
177	GTS	GTS289	RRFOI002000410	\N
178	HL	HL391A	RRFOI002000175	\N
179	HL	HL-12	RRFOI002000918	\N
180	KA	KA234	RRFOI002000239	普通金纸
181	KA	KA230a	RRFOI002000244	普通金纸
182	KA	KA207	RRFOI002000308	普通金纸
183	KA	KA232Ａ	RRFOI002000331	普通金纸
184	KA	KA202	RRFOI002000339	普通金纸
185	KA	KA-CS-490	RRFOI002000353	普通金纸
186	KA	KA-MATT	RRFOI002000358	普通金纸
187	KA	KA-CS-559S	RRFOI002000362	普通金纸
188	KA	KA-CS-422K	RRFOI002000364	普通金纸
189	KA	KA233	RRFOI002000365	普通金纸
190	KA	KA204	RRFOI002000383	普通金纸
191	KA	KA-CS-923K	RRFOI002000420	普通金纸
192	KA	KA215	RRFOI002000518	普通金纸
193	KA	KA581	RRFOI002000534	普通金纸
194	KA	KA203	RRFOI002000719	普通金纸
195	KA	KA-391u	RRFOI002000790	普通金纸
196	KA	KA-CS-529	RRFOI002000796	普通金纸
197	KA	KA211A	RRFOI002000802	普通金纸
198	KA	KA205	RRFOI002000813	普通金纸
199	KA	KA238	RRFOI002000819	普通金纸
200	KA	KA-CS-740	RRFOI002000836	普通金纸
201	KA	KA-CS-562-1	RRFOI002000837	普通金纸
202	KA	KA-CS-165F	RRFOI002000869	普通金纸
203	KA	KA207A	RRFOI002000932	普通金纸
204	KA	KA210	RRFOI002000935	普通金纸
205	KA	KA-CS-256	RRFOI002000937	普通金纸
206	KA	KA-216	RRFOI002000942	普通金纸
207	KA	KA231	RRFOI002000950	普通金纸
208	KA	KA205B	RRFOI002000978	普通金纸
209	KA	KA230	RRFOI002001021	普通金纸
210	KB	705KB235	RRFOI002000281	普通金纸
211	KB	KB235	RRFOI002000288	普通金纸
212	KB	KB232A	RRFOI002000333	普通金纸
213	KB	KB232A	RRFOI002000411	普通金纸
214	KB	KB234	RRFOI002000771	普通金纸
215	KB	KB-216	RRFOI002000825	普通金纸
216	KM	KM100L-51	RRFOI001000077	鐳射燙金紙
217	KM	KM100H-38	RRFOI001000078	鐳射燙金紙
218	KM	KM100R-82	RRFOI001000079	鐳射燙金紙
219	KM	KM100G-83	RRFOI001000082	鐳射燙金紙
220	KM	KM100Z-38Z	RRFOI001000083	鐳射燙金紙
221	KM	KM100Z-84	RRFOI001000179	鐳射燙金紙
222	KM	KM100S-P220	RRFOI001000194	鐳射燙金紙
223	KM	KM465-07	RRFOI001000244	鐳射燙金紙
224	KM	KM-459-08A	RRFOI001000263	鐳射燙金紙
225	KS	KS210	RRFOI002000147	\N
226	L	L010	RRFOI002000136	\N
227	L	L171	RRFOI002000137	\N
228	L	L025	RRFOI002000138	\N
229	L	L021	RRFOI002000139	\N
230	L	L272	RRFOI002000198	\N
231	L	6680	RRFOI002000292	\N
232	L	L277	RRFOI002000491	\N
233	L	L005	RRFOI002000800	\N
234	L	L181	RRFOI002000827	\N
235	L	DA-277	RRFOI002000880	\N
236	L	L012	RRFOI002000898	\N
237	L	L308	RRFOI002000899	\N
238	L	L260	RRFOI002000900	\N
239	L	L206	RRFOI002000976	\N
240	L	DA-2220	RRFOI002000983	\N
241	L	DA-6279S	RRFOI002001011	\N
242	L	DA-274	RRFOI002001013	\N
243	L	DA-3316M	RRFOI002001020	\N
244	L817	L817-OL5	RRFOI002000002	普通金纸
245	L817	L817-MGA	RRFOI002000005	普通金纸
246	L817	L817-392	RRFOI002000104	普通金纸
247	L817	L817-CU8	RRFOI002000105	普通金纸
248	L817	L817-MS3	RRFOI002000106	普通金纸
249	L817	L817-OLA	RRFOI002000108	普通金纸
250	L817	L817-OL5	RRFOI002000115	普通金纸
251	L817	L817-MS3	RRFOI002000814	普通金纸
252	L817	L817-CU9	RRFOI002000816	普通金纸
253	L817	L817-398	RRFOI002000831	普通金纸
254	L817	L817-MGA	RRFOI002000975	普通金纸
255	L817	L817-392	RRFOI002001030	普通金纸
256	LA	LA-401-00	RRFOI001000001	鐳射燙金紙
257	LA	LA-459-01A	RRFOI001000020	鐳射燙金紙
258	LA	LA-459-08A	RRFOI001000022	鐳射燙金紙
259	LA	LA-421-02	RRFOI001000038	鐳射燙金紙
260	LA	LA-266-00	RRFOI001000039	鐳射燙金紙
261	LA	LA-213-00	RRFOI001000046	鐳射燙金紙
262	LA	LA-263A-00	RRFOI001000048	鐳射燙金紙
263	LA	LA-410A-00	RRFOI001000109	鐳射燙金紙
264	LA	LA-438-03	RRFOI001000176	鐳射燙金紙
265	LA	LA-303-705	RRFOI001000184	鐳射燙金紙
266	LA	LA-459-07A	RRFOI001000188	鐳射燙金紙
267	LA	LA-459-00	RRFOI001000216	鐳射燙金紙
268	LA	LA-212-00	RRFOI001000241	鐳射燙金紙
269	LBW	LBW-421-00A	RRFOI001000191	鐳射燙金紙
270	LBW	LBW-421-01A	RRFOI001000192	鐳射燙金紙
271	LLA	LLA-401-00	RRFOI001000254	\N
272	LLO	LLO-468-T	RRFOI001000012	\N
273	LLO	LLO-266-T	RRFOI001000017	\N
274	LLO	LLO-465-T	RRFOI001000018	\N
275	LLO	LLO-410A-T	RRFOI001000023	\N
276	LLO	LLO-263A-T	RRFOI001000045	\N
277	LLO	LLO-459-T	RRFOI001000070	\N
278	LLO	LLO-410A-T	RRFOI001000085	\N
279	LLO	LLO-777-T	RRFOI001000092	\N
280	LLo	LLO-212-T	RRFOI001000096	\N
281	LLO	LLO-401-T	RRFOI001000112	\N
282	LLO	LLO-410A-T	RRFOI001000181	\N
283	LLO	LLO-223-T	RRFOI001000255	\N
284	LLO	LLO-224-T	RRFOI001000264	\N
285	LLW	LLW-499-00	RRFOI001000002	鐳射燙金紙
286	LLW	LLW-459-00	RRFOI001000006	鐳射燙金紙
287	LLW	LLW-401-705	RRFOI001000008	鐳射燙金紙
288	LLW	LL/LLW-401-07	RRFOI001000009	鐳射燙金紙
289	LLW	LLW-438-02	RRFOI001000010	鐳射燙金紙
290	LLW	LLW-459-06A	RRFOI001000011	鐳射燙金紙
291	LLW	LLW-459-205	RRFOI001000013	鐳射燙金紙
292	LLW	LLW-465-00	RRFOI001000014	鐳射燙金紙
293	LLW	LLW-212-06	RRFOI001000015	鐳射燙金紙
294	LLW	LLW-459-05	RRFOI001000016	鐳射燙金紙
295	LLW	LLW-465-05	RRFOI001000019	鐳射燙金紙
296	LLW	LLW-263-215	RRFOI001000024	鐳射燙金紙
297	LLW	LLW-421-00	RRFOI001000025	鐳射燙金紙
298	LLW	LLW-459-03	RRFOI001000027	鐳射燙金紙
299	LLW	LLW-206-00	RRFOI001000028	鐳射燙金紙
300	LLW	LLW-459-214	RRFOI001000029	鐳射燙金紙
301	LLW	LLW-421-00	RRFOI001000030	鐳射燙金紙
302	LLW	LLW-468-00	RRFOI001000032	鐳射燙金紙
303	LLW	LLW-213-00	RRFOI001000033	鐳射燙金紙
304	LLW	LA-438-02	RRFOI001000034	鐳射燙金紙
305	LLW	LLW-468-307	RRFOI001000035	鐳射燙金紙
306	LLW	LLW-212-01	RRFOI001000036	鐳射燙金紙
307	LLW	LLW-459-07	RRFOI001000037	鐳射燙金紙
308	LLW	LLW-421-04	RRFOI001000041	鐳射燙金紙
309	LLW	LLW-777-00	RRFOI001000042	鐳射燙金紙
310	LLW	LLW-401-01	RRFOI001000043	鐳射燙金紙
311	LLW	LLW-263A-05	RRFOI001000044	鐳射燙金紙
312	LLW	LLW-555-00	RRFOI001000047	鐳射燙金紙
313	LLW	LLW-459-02	RRFOI001000049	鐳射燙金紙
314	LLW	LLW-459-08A	RRFOI001000050	鐳射燙金紙
315	LLW	LLW-468-07	RRFOI001000051	鐳射燙金紙
316	LLW	LLW-459-03A	RRFOI001000052	鐳射燙金紙
317	LLW	LLW-436-00	RRFOI001000058	鐳射燙金紙
318	LLW	LLW-465-04	RRFOI001000059	鐳射燙金紙
319	LLW	LLW-410A-05C	RRFOI001000060	鐳射燙金紙
320	LLW	LLW-212-00	RRFOI001000065	鐳射燙金紙
321	LLW	LLW-777-01	RRFOI001000067	鐳射燙金紙
322	LLW	LLW-474-00	RRFOI001000068	鐳射燙金紙
323	LLW	LLW-401-700	RRFOI001000069	鐳射燙金紙
324	LLW	LLW-419-574	RRFOI001000072	鐳射燙金紙
325	LLW	LLW-401-05C	RRFOI001000073	鐳射燙金紙
326	LLW	LLW-433-00	RRFOI001000074	鐳射燙金紙
327	LLW	LLW-401(P)-00	RRFOI001000075	鐳射燙金紙
328	LLW	LLW-421-214	RRFOI001000076	鐳射燙金紙
329	LLW	LLW-410A-01	RRFOI001000080	鐳射燙金紙
330	LLW	LLW-212-05	RRFOI001000081	鐳射燙金紙
331	LLW	LL-410A-03	RRFOI001000086	鐳射燙金紙
332	LLW	LLW-224-06	RRFOI001000087	鐳射燙金紙
333	LLW	LLW-438-03	RRFOI001000088	鐳射燙金紙
334	LLW	LLW-421-03	RRFOI001000107	鐳射燙金紙
335	LLW	LLW-421-705	RRFOI001000114	鐳射燙金紙
336	LLW	LLW-401-05	RRFOI001000116	鐳射燙金紙
337	LLW	LLW-465-02	RRFOI001000119	鐳射燙金紙
338	LLW	LLW-212-05	RRFOI001000128	鐳射燙金紙
339	LLW	LX-YT-00	RRFOI001000166	鐳射燙金紙
340	LLW	LLW-401-01	RRFOI001000177	鐳射燙金紙
341	LLW	LLW-410A-02	RRFOI001000178	鐳射燙金紙
342	LLW	LLW-212-307	RRFOI001000180	鐳射燙金紙
343	LLW	LLW-459-307	RRFOI001000183	鐳射燙金紙
344	LLW	LLW-465-07	RRFOI001000185	鐳射燙金紙
345	LLW	LLW-417-03	RRFOI001000186	鐳射燙金紙
346	LLW	LLW-263A-00	RRFOI001000187	鐳射燙金紙
347	LLW	LLW-410A-705	RRFOI001000189	鐳射燙金紙
348	LLW	LLW-459-04	RRFOI001000190	鐳射燙金紙
349	LLW	LLW-499(P)-00	RRFOI001000193	鐳射燙金紙
350	LLW	LLW-417-00	RRFOI001000195	鐳射燙金紙
351	LLW	LA-302-00	RRFOI001000196	鐳射燙金紙
352	LLW	LA-263-05	RRFOI001000197	鐳射燙金紙
353	LLW	LA-224A-07	RRFOI001000198	鐳射燙金紙
354	LLW	LLW-468-01	RRFOI001000200	鐳射燙金紙
355	LLW	LLW-459-08	RRFOI001000201	鐳射燙金紙
356	LLW	LLW-263A-05A	RRFOI001000202	鐳射燙金紙
357	LLW	LLW-468-02	RRFOI001000203	鐳射燙金紙
358	LLW	LLW-417-02	RRFOI001000205	鐳射燙金紙
359	LLW	LA-238-00	RRFOI001000208	鐳射燙金紙
360	LLW	LLW-212-02	RRFOI001000209	鐳射燙金紙
361	LLW	LLW-555-600	RRFOI001000210	鐳射燙金紙
362	LLW	LLW-438-00	RRFOI001000211	鐳射燙金紙
363	LLW	LLW-263-00	RRFOI001000212	鐳射燙金紙
364	LLW	LLW-417-705	RRFOI001000213	鐳射燙金紙
365	LLW	LLW-212-07	RRFOI001000214	鐳射燙金紙
366	LLW	LLW-417-07	RRFOI001000215	鐳射燙金紙
367	LLW	LLW-302-01	RRFOI001000217	鐳射燙金紙
368	LLW	LLW-468-05	RRFOI001000218	鐳射燙金紙
369	LLW	LLW-468-03	RRFOI001000219	鐳射燙金紙
370	LLW	LLW-498-00	RRFOI001000220	鐳射燙金紙
371	LLW	LLW-401-02	RRFOI001000221	鐳射燙金紙
372	LLW	LLW-303-705	RRFOI001000222	鐳射燙金紙
373	LLW	LLW-212-208	RRFOI001000223	鐳射燙金紙
374	LLW	LLW-401-03	RRFOI001000224	鐳射燙金紙
375	LLW	LLW-468-08	RRFOI001000225	鐳射燙金紙
376	LLW	LLW-459-01A	RRFOI001000228	鐳射燙金紙
377	LLW	LLW-434-00	RRFOI001000229	鐳射燙金紙
378	LLW	LLW-303-00	RRFOI001000230	鐳射燙金紙
379	LLW	LLW-410A-00	RRFOI001000231	鐳射燙金紙
380	LLW	LLW-213-00	RRFOI001000232	鐳射燙金紙
381	LLW	LA-286-00	RRFOI001000233	鐳射燙金紙
382	LLW	LLW-465-01	RRFOI001000234	鐳射燙金紙
383	LLW	LLW-401-00	RRFOI001000237	鐳射燙金紙
384	LLW	LLW-401-05C	RRFOI001000238	鐳射燙金紙
385	LLW	LLW-418-01	RRFOI001000239	鐳射燙金紙
386	LLW	LLW-421-06A	RRFOI001000240	鐳射燙金紙
387	LLW	LLW-263-591	RRFOI001000243	鐳射燙金紙
388	LLW	LLW-417-05	RRFOI001000246	鐳射燙金紙
389	LLW	LLW-401-00	RRFOI001000247	鐳射燙金紙
390	LLW	LLW-263A-06	RRFOI001000248	鐳射燙金紙
391	LLW	LLW-421-705A	RRFOI001000249	鐳射燙金紙
392	LLW	LLW-777-11	RRFOI001000251	鐳射燙金紙
393	LLW	LLW-421-05	RRFOI001000252	鐳射燙金紙
394	LLW	LLW-263A-02	RRFOI001000253	鐳射燙金紙
395	LLW	LLW-224-00	RRFOI001000256	鐳射燙金紙
396	LLW	LLW-410A-214	RRFOI001000257	鐳射燙金紙
397	LLW	LLW-410A-01	RRFOI001000258	鐳射燙金紙
398	LLW	LLW-459-03A	RRFOI001000259	鐳射燙金紙
399	LLW	LLW-459-05	RRFOI001000260	鐳射燙金紙
400	LLW	LLW-901-09	RRFOI001000261	鐳射燙金紙
401	MB	MB808	RRFOI002000338	\N
402	NV	NV-100	RRFOI002000870	\N
403	PP	PP111	RRFOI002000857	\N
404	S15	S15-371	RRFOI002000492	\N
405	S16	S16-220	RRFOI002000155	普通金纸
406	S16	S16-355	RRFOI002000225	普通金纸
407	S16	S16-428	RRFOI002000253	普通金纸
408	S16	S16-427	RRFOI002000289	普通金纸
409	S16	S16-425	RRFOI002000301	普通金纸
410	S16	S16-423	RRFOI002000366	普通金纸
411	S16	S16-420	RRFOI002000382	普通金纸
412	S16	S16-231	RRFOI002000387	普通金纸
413	S16	S16-423	RRFOI002000500	普通金纸
414	S16	S16ALUFINS	RRFOI002000769	普通金纸
415	S16	S16-338	RRFOI002000803	普通金纸
416	S16	S16-397	RRFOI002000817	普通金纸
417	S16	S16-319	RRFOI002000822	普通金纸
418	S16	S16-AL	RRFOI002000829	普通金纸
419	S16	S16-429	RRFOI002000855	普通金纸
420	S16	S16-404	RRFOI002000894	普通金纸
421	S16	S16-289	RRFOI002000901	普通金纸
422	S16	S16-418	RRFOI002000909	普通金纸
423	S16	S16-391	RRFOI002000956	普通金纸
424	S16	S16-349	RRFOI002000962	普通金纸
425	S16	S16-232	RRFOI002000988	普通金纸
426	S16	S16-ALUFINSATINGLOSS	RRFOI002001036	普通金纸
427	S16	S16-396	RRFOI002001050	普通金纸
428	S16	S16-385	RRFOI002001056	普通金纸
429	S16	S16-376	RRFOI002001065	普通金纸
430	S16	S16-341	RRFOI002001072	普通金纸
431	S19	S19-420	RRFOI002000169	普通金纸
432	S19	S19-355	RRFOI002000174	普通金纸
433	S19	S19-302	RRFOI002000200	普通金纸
434	S19	S19-362	RRFOI002000243	普通金纸
435	S19	S19-307	RRFOI002000267	普通金纸
436	S19	S19-418	RRFOI002000284	普通金纸
437	S19	S19-220	RRFOI002000295	普通金纸
438	S19	S19-319	RRFOI002000311	普通金纸
439	S19	S19-AL	RRFOI002000320	普通金纸
440	S19	S19-404	RRFOI002000325	普通金纸
441	S19	S19-427	RRFOI002000327	普通金纸
442	S19	S19-429	RRFOI002000334	普通金纸
443	S19	S19-392	RRFOI002000335	普通金纸
444	S19	S19-420	RRFOI002000386	普通金纸
445	S19	S19-317	RRFOI002000413	普通金纸
446	S19	S19-396	RRFOI002000414	普通金纸
447	S19	S19-232	RRFOI002000442	普通金纸
448	S19	S19-425	RRFOI002000493	普通金纸
449	S19	S19-125326	RRFOI002000658	普通金纸
450	S19	S19-423	RRFOI002000767	普通金纸
451	S19	S19-3MS	RRFOI002000774	普通金纸
452	S19	S19-391	RRFOI002000804	普通金纸
453	S19	S19-3MS	RRFOI002000844	普通金纸
454	S19	S19-349	RRFOI002000852	普通金纸
455	S19	S19-428	RRFOI002000853	普通金纸
456	S19	S19-423	RRFOI002000910	普通金纸
457	S19	S19-376	RRFOI002000969	普通金纸
458	S19	S19-AL	RRFOI002000981	普通金纸
459	S19	S19-332	RRFOI002001014	普通金纸
460	S19	S19-371	RRFOI002001044	普通金纸
461	S21	S21-397	RRFOI002000319	\N
462	SA	SA977	RRFOI002000134	普通金纸
463	SA	SA971	RRFOI002000135	普通金纸
464	SA	SA902-1	RRFOI002000165	普通金纸
465	SA	SA956	RRFOI002000170	普通金纸
466	SA	SA940-1	RRFOI002000177	普通金纸
467	SA	SA988A	RRFOI002000178	普通金纸
468	SA	SA997-1	RRFOI002000179	普通金纸
469	SA	SA993	RRFOI002000183	普通金纸
470	SA	SA952	RRFOI002000184	普通金纸
471	SA	SA978	RRFOI002000197	普通金纸
472	SA	SA991	RRFOI002000202	普通金纸
473	SA	SA953	RRFOI002000203	普通金纸
474	SA	SA979	RRFOI002000222	普通金纸
475	SA	SA903	RRFOI002000258	普通金纸
476	SA	SA957-1	RRFOI002000268	普通金纸
477	SA	SA997A	RRFOI002000274	普通金纸
478	SA	SA907	RRFOI002000277	普通金纸
479	SA	SA951	RRFOI002000279	普通金纸
480	SA	SA909	RRFOI002000286	普通金纸
481	SA	SA998-1	RRFOI002000290	普通金纸
482	SA	SA901-1	RRFOI002000303	普通金纸
483	SA	SA906A	RRFOI002000305	普通金纸
484	SA	SA935	RRFOI002000306	普通金纸
485	SA	SA906	RRFOI002000309	普通金纸
486	SA	SA904-1	RRFOI002000312	普通金纸
487	SA	SA998A	RRFOI002000314	普通金纸
488	SA	SA906-1	RRFOI002000315	普通金纸
489	SA	SA314	RRFOI002000317	普通金纸
490	SA	SA911	RRFOI002000337	普通金纸
491	SA	SA901	RRFOI002000342	普通金纸
492	SA	SA933	RRFOI002000347	普通金纸
493	SA	SA930A	RRFOI002000349	普通金纸
494	SA	SA940	RRFOI002000354	普通金纸
495	SA	SA936	RRFOI002000377	普通金纸
496	SA	SA932	RRFOI002000378	普通金纸
497	SA	SA531	RRFOI002000384	普通金纸
498	SA	SA958-1	RRFOI002000385	普通金纸
499	SA	SA938	RRFOI002000396	普通金纸
500	SA	SA959	RRFOI002000408	普通金纸
501	SA	SA992-1	RRFOI002000418	普通金纸
502	SA	SA961	RRFOI002000421	普通金纸
503	SA	SA908	RRFOI002000424	普通金纸
504	SA	SA935	RRFOI002000441	普通金纸
505	SA	SA984	RRFOI002000463	普通金纸
506	SA	SA957	RRFOI002000477	普通金纸
507	SA	SA968	RRFOI002000480	普通金纸
508	SA	SA968-1	RRFOI002000482	普通金纸
509	SA	SA931	RRFOI002000513	普通金纸
510	SA	SA954	RRFOI002000516	普通金纸
511	SA	SA951-3	RRFOI002000543	普通金纸
512	SA	SA943	RRFOI002000544	普通金纸
513	SA	SA941	RRFOI002000572	普通金纸
514	SA	SA989	RRFOI002000581	普通金纸
515	SA	SA937	RRFOI002000592	普通金纸
516	SA	SA930-1	RRFOI002000747	普通金纸
517	SA	SA940-2	RRFOI002000749	普通金纸
518	SA	SA905	RRFOI002000750	普通金纸
519	SA	SA608	RRFOI002000756	普通金纸
520	SA	SA906A	RRFOI002000764	普通金纸
521	SA	SA910	RRFOI002000768	普通金纸
522	SA	SA905	RRFOI002000777	普通金纸
523	SA	SA571	RRFOI002000778	普通金纸
524	SA	SA940-2	RRFOI002000784	普通金纸
525	SA	SA106	RRFOI002000799	普通金纸
526	SA	SA992	RRFOI002000805	普通金纸
527	SA	SA974	RRFOI002000807	普通金纸
528	SA	SA951-2	RRFOI002000823	普通金纸
529	SA	SA571	RRFOI002000841	普通金纸
530	SA	SA939	RRFOI002000843	普通金纸
531	SA	SA963-1	RRFOI002000845	普通金纸
532	SA	SA957-2	RRFOI002000847	普通金纸
533	SA	SA944	RRFOI002000848	普通金纸
534	SA	SA907A	RRFOI002000851	普通金纸
535	SA	SA983	RRFOI002000854	普通金纸
536	SA	SA965-1	RRFOI002000859	普通金纸
537	SA	SA997	RRFOI002000861	普通金纸
538	SA	SA972	RRFOI002000866	普通金纸
539	SA	SA995	RRFOI002000867	普通金纸
540	SA	SA981	RRFOI002000875	普通金纸
541	SA	SA966-1	RRFOI002000886	普通金纸
542	SA	SA904	RRFOI002000911	普通金纸
543	SA	SA901A	RRFOI002000917	普通金纸
544	SA	SA930-1	RRFOI002000923	普通金纸
545	SA	SA939-1	RRFOI002000939	普通金纸
546	SA	SA995-1	RRFOI002000941	普通金纸
547	SA	SA930	RRFOI002000953	普通金纸
548	SA	SA902	RRFOI002000957	普通金纸
549	SA	SA992-2	RRFOI002000960	普通金纸
550	SA	SA908A	RRFOI002000965	普通金纸
551	SA	SA988	RRFOI002000966	普通金纸
552	SA	SA996	RRFOI002000972	普通金纸
553	SA	SA944	RRFOI002000985	普通金纸
554	SA	SA934	RRFOI002000989	普通金纸
555	SA	SA975	RRFOI002000990	普通金纸
556	SA	SA909-1	RRFOI002001010	普通金纸
557	SA	SA985A	RRFOI002001019	普通金纸
558	SA	SA998	RRFOI002001022	普通金纸
559	SA	SA962	RRFOI002001058	普通金纸
560	SA	SA987-2	RRFOI002001066	普通金纸
561	SA	SA939A	RRFOI002001074	普通金纸
562	SB	SB991	RRFOI002000151	普通金纸
563	SB	SB985	RRFOI002000152	普通金纸
564	SB	SB951-2	RRFOI002000154	普通金纸
565	SB	SB901A	RRFOI002000163	普通金纸
566	SB	SB984-1	RRFOI002000168	普通金纸
567	SB	SB904A	RRFOI002000171	普通金纸
568	SB	SB997	RRFOI002000172	普通金纸
569	SB	SB956	RRFOI002000176	普通金纸
570	SB	SB952	RRFOI002000180	普通金纸
571	SB	SB965	RRFOI002000182	普通金纸
572	SB	SB984	RRFOI002000190	普通金纸
573	SB	SB902-1	RRFOI002000191	普通金纸
574	SB	SB990	RRFOI002000193	普通金纸
575	SB	SB906	RRFOI002000204	普通金纸
576	SB	SB988A	RRFOI002000205	普通金纸
577	SB	SB901	RRFOI002000206	普通金纸
578	SB	SB932	RRFOI002000207	普通金纸
579	SB	SB908A	RRFOI002000220	普通金纸
580	SB	SB909	RRFOI002000228	普通金纸
581	SB	SB930A	RRFOI002000245	普通金纸
582	SB	SB956-1	RRFOI002000261	普通金纸
583	SB	SB974	RRFOI002000263	普通金纸
584	SB	SB951	RRFOI002000265	普通金纸
585	SB	SB902	RRFOI002000269	普通金纸
586	SB	SB937	RRFOI002000270	普通金纸
587	SB	SB983	RRFOI002000271	普通金纸
588	SB	SB933	RRFOI002000278	普通金纸
589	SB	SB911	RRFOI002000280	普通金纸
590	SB	SB905	RRFOI002000299	普通金纸
591	SB	SB907	RRFOI002000310	普通金纸
592	SB	SB944	RRFOI002000313	普通金纸
593	SB	SB951-2	RRFOI002000336	普通金纸
594	SB	SB971	RRFOI002000340	普通金纸
595	SB	SB972-3	RRFOI002000341	普通金纸
596	SB	SB909-1	RRFOI002000344	普通金纸
597	SB	SB907	RRFOI002000345	普通金纸
598	SB	SB910	RRFOI002000352	普通金纸
599	SB	SB908	RRFOI002000355	普通金纸
600	SB	SB908-1	RRFOI002000367	普通金纸
601	SB	SB992-2	RRFOI002000389	普通金纸
602	SB	SB963-1	RRFOI002000398	普通金纸
603	SB	SB975	RRFOI002000417	普通金纸
604	SB	SB939-1	RRFOI002000419	普通金纸
605	SB	SB980	RRFOI002000422	普通金纸
606	SB	SB940-2	RRFOI002000425	普通金纸
607	SB	SB930-1	RRFOI002000476	普通金纸
608	SB	SB957	RRFOI002000481	普通金纸
609	SB	SB977	RRFOI002000495	普通金纸
610	SB	SB968-1	RRFOI002000501	普通金纸
611	SB	SB995-1	RRFOI002000504	普通金纸
612	SB	SB955	RRFOI002000507	普通金纸
613	SB	SB951-4	RRFOI002000514	普通金纸
614	SB	SB966-1	RRFOI002000548	普通金纸
615	SB	SB987	RRFOI002000575	普通金纸
616	SB	SB972	RRFOI002000708	普通金纸
617	SB	SB959	RRFOI002000745	普通金纸
618	SB	SB951-1	RRFOI002000755	普通金纸
619	SB	SB953	RRFOI002000757	普通金纸
620	SB	SB106	RRFOI002000782	普通金纸
621	SB	SB935	RRFOI002000783	普通金纸
622	SB	SB940	RRFOI002000797	普通金纸
623	SB	SB966	RRFOI002000808	普通金纸
624	SB	SB967	RRFOI002000809	普通金纸
625	SB	SB995	RRFOI002000810	普通金纸
626	SB	SB987-1	RRFOI002000811	普通金纸
627	SB	SB964	RRFOI002000812	普通金纸
628	SB	SB994	RRFOI002000815	普通金纸
629	SB	SB988	RRFOI002000818	普通金纸
630	SB	SB-951-3	RRFOI002000824	普通金纸
631	SB	SB934	RRFOI002000834	普通金纸
632	SB	SB939-2	RRFOI002000842	普通金纸
633	SB	SB981	RRFOI002000860	普通金纸
634	SB	SB930B	RRFOI002000891	普通金纸
635	SB	SB930	RRFOI002000913	普通金纸
636	SB	SB982-1	RRFOI002000919	普通金纸
637	SB	SB954	RRFOI002000921	普通金纸
638	SB	SB959	RRFOI002000924	普通金纸
639	SB	SB958-1	RRFOI002000933	普通金纸
640	SB	SB941	RRFOI002000945	普通金纸
641	SB	SB939	RRFOI002000955	普通金纸
642	SB	SB998	RRFOI002001023	普通金纸
643	SB	SB992-3	RRFOI002001034	普通金纸
644	SB	SB940-1	RRFOI002001035	普通金纸
645	SB	SB956	RRFOI002001041	普通金纸
646	SB	SB963-1	RRFOI002001068	普通金纸
647	SK	SK0700	RRFOI002001024	\N
648	SK	SK0174	RRFOI002001025	\N
649	SK	SK0149	RRFOI002001026	\N
650	SK	SK111	RRFOI002001031	\N
651	SK	SK121	RRFOI002001032	\N
652	SK	SK52214	RRFOI002001045	\N
653	SK	SK-K0100	RRFOI002001048	\N
654	SP	SP-07	RRFOI002000153	\N
655	SP	SP-03	RRFOI002000157	\N
656	SP	SP-03	RRFOI002000381	\N
657	SP	SP-01	RRFOI002000394	\N
658	SP	SP-01	RRFOI002000395	\N
659	SP	SP-02	RRFOI002000879	\N
660	SP	SP-08	RRFOI002001015	\N
661	SST	SST0634	RRFOI002000251	普通耐磨
662	SST	SST0156	RRFOI002000257	普通耐磨
663	SST	SST0182	RRFOI002000291	普通耐磨
664	SST	SST0700	RRFOI002000296	普通耐磨
665	SST	SST0174	RRFOI002000298	普通耐磨
666	SST	SST0149	RRFOI002000318	普通耐磨
667	SST	SST111	RRFOI002000348	普通耐磨
668	SST	SST0700	RRFOI002000350	普通耐磨
669	SST	SST0156	RRFOI002000351	普通耐磨
670	SST	SSTF160	RRFOI002000356	普通耐磨
671	SST	SST111	RRFOI002000359	普通耐磨
672	SST	SST5738	RRFOI002000361	普通耐磨
673	SST	SST120	RRFOI002000368	普通耐磨
674	SST	SST0149	RRFOI002000369	普通耐磨
675	SST	SST0174	RRFOI002000371	普通耐磨
676	SST	SST0183	RRFOI002000372	普通耐磨
677	SST	SST0634	RRFOI002000373	普通耐磨
678	SST	SSTF160	RRFOI002000374	普通耐磨
679	SST	SST0751	RRFOI002000376	普通耐磨
680	SST	SST120	RRFOI002000380	普通耐磨
681	SST	SST0751	RRFOI002000388	普通耐磨
682	SST	SST5738	RRFOI002000525	普通耐磨
683	SST	SST0205	RRFOI002000536	普通耐磨
684	SST	SST0635	RRFOI002000537	普通耐磨
685	SST	SST0183	RRFOI002000568	普通耐磨
686	SST	SST0691	RRFOI002000570	普通耐磨
687	SST	SST0182	RRFOI002000585	普通耐磨
688	SST	SST0205	RRFOI002000586	普通耐磨
689	SST	SST5734	RRFOI002000590	普通耐磨
690	SST	SST0720	RRFOI002000601	普通耐磨
691	SST	SST0720	RRFOI002000602	普通耐磨
692	SST	SST2152	RRFOI002000603	普通耐磨
693	SST	SST5734	RRFOI002000739	普通耐磨
694	SST	SST0201	RRFOI002000752	普通耐磨
695	SST	SST0201	RRFOI002000801	普通耐磨
696	SST	SST606	RRFOI002001055	普通耐磨
697	SSY	SSYZ122	RRFOI002000128	普通金纸
698	SSY	SSY218	RRFOI002000131	普通金纸
699	SSY	SSY140	RRFOI002000142	普通金纸
700	SSY	SSY111	RRFOI002000145	普通金纸
701	SSY	SSY170	RRFOI002000185	普通金纸
702	SSY	SSY121	RRFOI002000189	普通金纸
703	SSY	SSY184	RRFOI002000208	普通金纸
704	SSY	SSY0174	RRFOI002000209	普通金纸
705	SSY	SSY0634	RRFOI002000210	普通金纸
706	SSY	SSY0720	RRFOI002000211	普通金纸
707	SSY	SSY0700	RRFOI002000212	普通金纸
708	SSY	SSY0149	RRFOI002000214	普通金纸
709	SSY	SSY0156	RRFOI002000215	普通金纸
710	SSY	SSY5734	RRFOI002000216	普通金纸
711	SSY	SSY1110	RRFOI002000217	普通金纸
712	SSY	SSY2152	RRFOI002000218	普通金纸
713	SSY	SSY0205	RRFOI002000219	普通金纸
714	SSY	SSY0182	RRFOI002000223	普通金纸
715	SSY	SSY0691	RRFOI002000224	普通金纸
716	SSY	SSY0635	RRFOI002000230	普通金纸
717	SSY	SSY0183	RRFOI002000231	普通金纸
718	SSY	SSYF160	RRFOI002000293	普通金纸
719	SSY	SSY0635	RRFOI002000326	普通金纸
720	SSY	SSY0156	RRFOI002000328	普通金纸
721	SSY	SSY2152	RRFOI002000330	普通金纸
722	SSY	SSY0174	RRFOI002000370	普通金纸
723	SSY	SSY0751	RRFOI002000375	普通金纸
724	SSY	SSYZ122	RRFOI002000460	普通金纸
725	SSY	SSY120	RRFOI002000462	普通金纸
726	SSY	SSY0634	RRFOI002000545	普通金纸
727	SSY	SSYZ122	RRFOI002000587	普通金纸
728	SSY	SSY0691	RRFOI002000588	普通金纸
729	SSY	SSY5734	RRFOI002000589	普通金纸
730	SSY	SSY0201	RRFOI002000758	普通金纸
731	SSY	SSY177-1	RRFOI002000795	普通金纸
732	SSY	SSY0156	RRFOI002000862	普通金纸
733	SSY	SSY0635	RRFOI002000984	普通金纸
734	ST	ST0183	RRFOI002000006	普通耐磨
735	ST	ST0156	RRFOI002000007	普通耐磨
736	ST	ST0700	RRFOI002000008	普通耐磨
737	ST	ST0634	RRFOI002000009	普通耐磨
738	ST	ST1110	RRFOI002000010	普通耐磨
739	ST	ST0635	RRFOI002000011	普通耐磨
740	ST	ST0205	RRFOI002000012	普通耐磨
741	ST	ST0182	RRFOI002000013	普通耐磨
742	ST	ST0149	RRFOI002000014	普通耐磨
743	ST	ST5738	RRFOI002000015	普通耐磨
744	ST	ST0174	RRFOI002000016	普通耐磨
745	ST	ST111	RRFOI002000017	普通耐磨
746	ST	ST0700	RRFOI002000020	普通耐磨
747	ST	ST0156	RRFOI002000021	普通耐磨
748	ST	ST111	RRFOI002000022	普通耐磨
749	ST	ST0201	RRFOI002000023	普通耐磨
750	ST	ST120	RRFOI002000026	普通耐磨
751	ST	ST0182	RRFOI002000027	普通耐磨
752	ST	ST120	RRFOI002000052	普通耐磨
753	ST	ST0149	RRFOI002000053	普通耐磨
754	ST	ST0174	RRFOI002000054	普通耐磨
755	ST	ST0183	RRFOI002000055	普通耐磨
756	ST	ST0205	RRFOI002000056	普通耐磨
757	ST	ST0634	RRFOI002000057	普通耐磨
758	ST	ST0691	RRFOI002000058	普通耐磨
759	ST	STF160	RRFOI002000059	普通耐磨
760	ST	ST1110	RRFOI002000060	普通耐磨
761	ST	ST2152	RRFOI002000061	普通耐磨
762	ST	ST170	RRFOI002000062	普通耐磨
763	ST	ST0635	RRFOI002000063	普通耐磨
764	ST	ST0751	RRFOI002000064	普通耐磨
765	ST	ST0720	RRFOI002000065	普通耐磨
766	ST	ST0720	RRFOI002000067	普通耐磨
767	ST	STF160	RRFOI002000069	普通耐磨
768	ST	ST0201	RRFOI002000072	普通耐磨
769	ST	ST5734	RRFOI002000073	普通耐磨
770	ST	ST0691	RRFOI002000080	普通耐磨
771	ST	ST0751	RRFOI002000081	普通耐磨
772	ST	ST2152	RRFOI002000085	普通耐磨
773	ST	ST170	RRFOI002000087	普通耐磨
774	ST	ST5738	RRFOI002000088	普通耐磨
775	ST	ST425	RRFOI002000439	普通耐磨
776	ST	ST2152	RRFOI002000555	普通耐磨
777	ST	ST606	RRFOI002000974	普通耐磨
778	ST	ST618	RRFOI002000980	普通耐磨
779	ST	ST5734	RRFOI002000986	普通耐磨
780	ST	ST684	RRFOI002000987	普通耐磨
781	ST	ST677-1	RRFOI002000992	普通耐磨
782	ST	STA604-4	RRFOI002000993	普通耐磨
783	ST	ST677-2	RRFOI002000994	普通耐磨
784	ST	ST621	RRFOI002000995	普通耐磨
785	ST	ST618-B	RRFOI002000996	普通耐磨
786	ST	ST618-A	RRFOI002000997	普通耐磨
787	ST	ST650A	RRFOI002000998	普通耐磨
788	ST	ST697-5	RRFOI002000999	普通耐磨
789	ST	ST650-2	RRFOI002001000	普通耐磨
790	ST	ST652-4	RRFOI002001001	普通耐磨
791	ST	ST656-1	RRFOI002001002	普通耐磨
792	ST	ST631	RRFOI002001003	普通耐磨
793	ST	STA638-1	RRFOI002001004	普通耐磨
794	ST	ST635-3	RRFOI002001005	普通耐磨
795	ST	ST638	RRFOI002001007	普通耐磨
796	ST	ST640	RRFOI002001008	普通耐磨
797	ST	ST5736	RRFOI002001028	普通耐磨
798	ST	ST5739	RRFOI002001029	普通耐磨
799	SY+	SY170+	RRFOI002000003	普通金纸
800	SY+	SH111+	RRFOI002000018	普通金纸
801	SY+	SY0156+	RRFOI002000019	普通金纸
802	SY+	SY0700+	RRFOI002000024	普通金纸
803	SY+	SH120+	RRFOI002000025	普通金纸
804	SY+	SY0149+	RRFOI002000028	普通金纸
805	SY+	SY0174+	RRFOI002000029	普通金纸
806	SY+	SY0182+	RRFOI002000030	普通金纸
807	SY+	SY0183+	RRFOI002000031	普通金纸
808	SY+	SY0201+	RRFOI002000032	普通金纸
809	SY+	SY0205+	RRFOI002000033	普通金纸
810	SY+	SY0634+	RRFOI002000034	普通金纸
811	SY+	SY0635+	RRFOI002000035	普通金纸
812	SY+	SYZ122+	RRFOI002000036	普通金纸
813	SY+	SY0691+	RRFOI002000037	普通金纸
814	SY+	SYF160+	RRFOI002000038	普通金纸
815	SY+	SY0720+	RRFOI002000039	普通金纸
816	SY+	SY0751+	RRFOI002000040	普通金纸
817	SY+	SY1110+	RRFOI002000041	普通金纸
818	SY+	SY2152+	RRFOI002000042	普通金纸
819	SY+	SY5734+	RRFOI002000043	普通金纸
820	SY+	SY5738+	RRFOI002000044	普通金纸
821	SY+	SY5739+	RRFOI002000045	普通金纸
822	SY+	SY0149+	RRFOI002000046	普通金纸
823	SY+	SY0183+	RRFOI002000047	普通金纸
824	SY+	SY0700+	RRFOI002000048	普通金纸
825	SY+	SH111+	RRFOI002000049	普通金纸
826	SY+	SY0634+	RRFOI002000050	普通金纸
827	SY+	SY0174+	RRFOI002000051	普通金纸
828	SY+	SY0635+	RRFOI002000066	普通金纸
829	SY+	SH120+	RRFOI002000068	普通金纸
830	SY+	SY0720+	RRFOI002000070	普通金纸
831	SY+	SY0156+	RRFOI002000074	普通金纸
832	SY+	SY0182+	RRFOI002000075	普通金纸
833	SY+	SY0691+	RRFOI002000076	普通金纸
834	SY+	SYZ122+	RRFOI002000077	普通金纸
835	SY+	SYF160+	RRFOI002000078	普通金纸
836	SY+	SY0205+	RRFOI002000079	普通金纸
837	SY+	SY0751+	RRFOI002000082	普通金纸
838	SY+	SY1110+	RRFOI002000083	普通金纸
839	SY+	SY2152+	RRFOI002000084	普通金纸
840	SY+	SY170+	RRFOI002000086	普通金纸
841	SY+	SY5738+	RRFOI002000089	普通金纸
842	SY+	SH111	RRFOI002000461	普通金纸
843	SY+	SY0201+	RRFOI002000591	普通金纸
844	SY+	SY5734+	RRFOI002000604	普通金纸
845	SY+	SY5736+	RRFOI002000605	普通金纸
846	SY1-	SY2152-	RRFOI002000226	普通金纸
847	SY1-	SY0634-	RRFOI002000237	普通金纸
848	SY1-	SY111-	RRFOI002000329	普通金纸
849	SY1-	SY0720-	RRFOI002000332	普通金纸
850	SY1-	SY0700-	RRFOI002000360	普通金纸
851	SY1-	SY0183-	RRFOI002000502	普通金纸
852	SY1-	SY0174-	RRFOI002000541	普通金纸
853	SY1-	SY120-	RRFOI002000744	普通金纸
854	SY1-	SY0635-	RRFOI002000794	普通金纸
855	SY1-	SY0205-	RRFOI002000865	普通金纸
856	SY1-	SY0751-	RRFOI002000873	普通金纸
857	SY1-	SY0156-	RRFOI002000874	普通金纸
858	SY1-	SY106-	RRFOI002001018	普通金纸
859	SY1-	SY184-	RRFOI002001027	普通金纸
860	SY6	SY618	RRFOI002000116	普通金纸
861	SY6	SYA638-1	RRFOI002000117	普通金纸
862	SY6	SY618-B	RRFOI002000118	普通金纸
863	SY6	SY631	RRFOI002000119	普通金纸
864	SY6	SY606	RRFOI002000120	普通金纸
865	SY6	SY618	RRFOI002000121	普通金纸
866	SY6	SY677-1	RRFOI002000122	普通金纸
867	SY6	SY184	RRFOI002000125	普通金纸
868	SY6	SY621	RRFOI002000126	普通金纸
869	SY6	SY103	RRFOI002000130	普通金纸
870	SY6	SYA604-4	RRFOI002000143	普通金纸
871	SY6	SY604-3	RRFOI002000149	普通金纸
872	SY6	SY652-4	RRFOI002000150	普通金纸
873	SY6	SY640	RRFOI002000164	普通金纸
874	SY6	SY663-1	RRFOI002000186	普通金纸
875	SY6	SY131-7	RRFOI002000188	普通金纸
876	SY6	SY150-2	RRFOI002000192	普通金纸
877	SY6	SY650	RRFOI002000194	普通金纸
878	SY6	SY0205	RRFOI002000259	普通金纸
879	SY6	SY60751	RRFOI002000266	普通金纸
880	SY6	SY60149	RRFOI002000273	普通金纸
881	SY6	SY60201	RRFOI002000357	普通金纸
882	SY6	SY5734	RRFOI002000363	普通金纸
883	SY6	SY677-2	RRFOI002000467	普通金纸
884	SY6	SY697-5	RRFOI002000469	普通金纸
885	SY6	SY655	RRFOI002000471	普通金纸
886	SY6	SY0700	RRFOI002000496	普通金纸
887	SY6	SY0183	RRFOI002000503	普通金纸
888	SY6	SY684	RRFOI002000509	普通金纸
889	SY6	SY60183	RRFOI002000512	普通金纸
890	SY6	SY0700	RRFOI002000533	普通金纸
891	SY6	SY0149	RRFOI002000573	普通金纸
892	SY6	SY111-WL	RRFOI002000725	普通金纸
893	SY6	SY5738	RRFOI002000748	普通金纸
894	SY6	SYA138-1	RRFOI002000775	普通金纸
895	SY6	SYZ622	RRFOI002000868	普通金纸
896	SY6	SY670	RRFOI002000907	普通金纸
897	SY6	SY028	RRFOI002001043	普通金纸
898	SY6	SY150B	RRFOI002001049	普通金纸
899	SY6	SY648-1	RRFOI002001057	普通金纸
900	SY6	SY663-1	RRFOI002001060	普通金纸
901	SY6-	SY620-	RRFOI002000071	普通金纸
902	SY6-	SY611-	RRFOI002000090	普通金纸
903	SY6-	SY606-	RRFOI002000091	普通金纸
904	SY6-	SY677-2-	RRFOI002000092	普通金纸
905	SY6-	SY677-1-	RRFOI002000093	普通金纸
906	SY6-	SY684-	RRFOI002000094	普通金纸
907	SY6-	SY618-A	RRFOI002000201	普通金纸
908	SY6-	SYA604-4-	RRFOI002000221	普通金纸
909	SY6-	SY618-	RRFOI002000390	普通金纸
910	SY6-	SY621-	RRFOI002000391	普通金纸
911	SY6-	SY670-	RRFOI002000392	普通金纸
912	SY6-	SY697-5-	RRFOI002000393	普通金纸
913	SY6-	SY650A-	RRFOI002000402	普通金纸
914	SY6-	SY631-	RRFOI002000403	普通金纸
915	SY6-	SY638-	RRFOI002000404	普通金纸
916	SY6-	SY640-	RRFOI002000405	普通金纸
917	SY6-	SYF660-	RRFOI002000406	普通金纸
918	SY6-	SY618-B-	RRFOI002000407	普通金纸
919	SY6-	SY652-4-	RRFOI002000409	普通金纸
920	SY6-	SY650-2-	RRFOI002000412	普通金纸
921	SY6-	SY656-1-	RRFOI002000415	普通金纸
922	SY6-	SY604-1-	RRFOI002000416	普通金纸
923	SY6-	SY604-1-	RRFOI002000427	普通金纸
924	SY6-	SY618-A-	RRFOI002000428	普通金纸
925	SY6-	SY666-1-	RRFOI002000429	普通金纸
926	SY6-	SY663-1-	RRFOI002000430	普通金纸
927	SY6-	SY652-4-	RRFOI002000704	普通金纸
928	SY6-	SY635-3-	RRFOI002001006	普通金纸
929	SY6-	SY630-12-	RRFOI002001009	普通金纸
930	SY6-	SY5757G-	RRFOI002001039	普通金纸
931	SY6-	SY5774G-	RRFOI002001040	普通金纸
932	TA	TA308	RRFOI002000920	\N
933	TA	TA352	RRFOI002000940	\N
934	TA	TA368	RRFOI002000948	\N
935	TA	TA366	RRFOI002000959	\N
936	TA	TA339	RRFOI002000963	\N
937	TA	TA303	RRFOI002000964	\N
938	TA	TA306-1	RRFOI002000968	\N
939	TA	TA339-2	RRFOI002000970	\N
940	TA	TA383	RRFOI002000991	\N
941	TA	TA308-1	RRFOI002001033	\N
942	TA	TA386	RRFOI002001054	\N
943	TA	TA355A	RRFOI002001073	\N
944	TB	TB-HA700	RRFOI002000434	\N
945	TB	TB953	RRFOI002000472	\N
946	TB813	TB813-137	RRFOI002000229	\N
947	TB813	TB813-137	RRFOI002000300	\N
948	TB813	TB813-751	RRFOI002000497	\N
949	TB813D	TB813D0156	RRFOI002000242	\N
950	TB813D	TB813D-0205	RRFOI002000246	\N
951	TB813D	TB813D-137	RRFOI002000247	\N
952	TB813G	TB813G-0100	RRFOI001000003	\N
953	TB815	TB815-429A	RRFOI002000436	高耐磨
954	TB815	TB815-429A	RRFOI002000440	高耐磨
955	TB815	TB815-L45S	RRFOI002000753	高耐磨
956	TB815	TB815-0156	RRFOI002000786	高耐磨
957	TB815	TB815-0694	RRFOI002000788	高耐磨
958	TB815	TB815-L44S	RRFOI002000789	高耐磨
959	TB815	TB815-0634	RRFOI002000882	高耐磨
960	TB815	TB815-0149	RRFOI002000902	高耐磨
961	TB815	TB815-0700	RRFOI002000903	高耐磨
962	TB815	TB815-0205	RRFOI002000904	高耐磨
963	TB815	TB815-5738	RRFOI002000914	高耐磨
964	TB815	TB815-1PK	RRFOI002000915	高耐磨
965	TB815	TB815-432	RRFOI002000916	高耐磨
966	TB815	TB815-L45S	RRFOI002000922	高耐磨
967	TB815	TB815-L45S	RRFOI002000926	高耐磨
968	TB815	TB815-1260	RRFOI002000944	高耐磨
969	TB815	TB815-4MG	RRFOI002001059	高耐磨
970	TB815	TB815-4MG	RRFOI002001061	高耐磨
971	V	V972	RRFOI002000146	色箔系列
972	V	V911	RRFOI002000156	色箔系列
973	V	V912	RRFOI002000961	色箔系列
974	VB	VB911	RRFOI002000123	色箔系列
975	VB	VB912	RRFOI002000908	色箔系列
976	WP	WP808	RRFOI002000294	\N
977	WP	WP803	RRFOI002000527	\N
978	YED	YED-2000	RRFOI002000110	\N
979	YZT	CW-LA01	RRFOI001000053	客人指定
980	YZT	YZT-LA02	RRFOI001000054	客人指定
981	YZT	YZT-LE01	RRFOI001000061	客人指定
982	YZT	YZT-LA07	RRFOI001000063	客人指定
983	YZT	YZT-G0206-6	RRFOI001000071	客人指定
984	YZT	YZT-LA03-1	RRFOI001000084	客人指定
985	YZT	CW-LA01	RRFOI001000204	客人指定
986	YZT	YZT-L019CU	RRFOI001000206	客人指定
987	YZT	YZT-L011BL-H	RRFOI001000245	客人指定
988	YZT	YZT-SP-301	RRFOI001000250	客人指定
989	YZT	YZT-WJB02	RRFOI001000262	客人指定
990	YZT	YZT-9402	RRFOI002000321	客人指定
991	YZT	YZT-9339	RRFOI002000322	客人指定
992	YZT	YZT-9121	RRFOI002000323	客人指定
993	YZT	YZT-9323	RRFOI002000324	客人指定
994	YZT	YZT-9402	RRFOI002000397	客人指定
995	YZT	YZT-9030	RRFOI002000399	客人指定
996	YZT	YZT-9339	RRFOI002000400	客人指定
997	YZT	YZT-9323	RRFOI002000401	客人指定
998	YZT	YZT913B	RRFOI002000433	客人指定
999	YZT	YZT-G56	RRFOI002000435	客人指定
1000	YZT	YZT-Y053	RRFOI002000546	客人指定
1001	YZT	YZT-Y065	RRFOI002000696	客人指定
1002	YZT	YZT-5MATS	RRFOI002000787	客人指定
1003	YZT	YZT-Y051	RRFOI002000885	客人指定
1004	YZT	YZT54405	RRFOI002000958	客人指定
1005	YZT	YZT-K0100(H)	RRFOI002000967	客人指定
1006	YZT	YZT-549G	RRFOI002000971	客人指定
1007	YZT	YZT-52214	RRFOI002000973	客人指定
1008	YZT	YZT-57153G	RRFOI002001037	客人指定
1009	YZT	YZT-5779G	RRFOI002001038	客人指定
1010	YZT	YZT-VC10	RRFOI002001047	客人指定
1011	YZT	YZT-495	RRFOI002001053	客人指定
1012	YZT	YZT-52570	RRFOI002001062	客人指定
1013	YZT	YZT-52172	RRFOI002001063	客人指定
1014	YZT	YZT-S1000	RRFOI002001064	客人指定
1015	YZT	YZT-WJB02	RRFOI002001067	客人指定
1016	YZT	YZT-5757G	RRFOI002001070	客人指定
1017	YZT	YZT-SP-82	RRFOI002001071	客人指定
\.


--
-- TOC entry 5030 (class 0 OID 16453)
-- Dependencies: 237
-- Data for Name: specifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.specifications (id, project_id, color, size, tightness, temperature_range, performance) FROM stdin;
1	1	白色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
2	2	金色	610mm x 120m	\N	\N	 性能 : 非耐磨 
3	3	金色	610mm x 120m	\N	\N	 性能 : 非耐磨 
4	4	銅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
5	5	粉紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
6	6	啞銀色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
7	7	金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
8	8	銀色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
9	9	紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
10	10	啞銀	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
11	11	金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
12	12	黑色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
13	13	綠色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
14	14	粉紅	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
15	15	啞金	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
16	16	青銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
17	17	透明	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
18	18	啞金	640mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
19	19	金色	640mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
20	20	藍色	640mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
21	21	銅色	640mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
22	22	金色	640mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
23	23	啞金	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
24	24	啞銅色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
25	25	金色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
26	26	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
27	27	金色	1280mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
28	28	紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
29	29	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
30	30	綠色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
31	31	紫色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
32	32	綠色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
33	33	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
34	34	粉紅	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
35	35	金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
36	36	紫色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
37	37	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
38	38	紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
39	39	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
40	40	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
41	41	紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
42	42	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
43	43	紫色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
44	44	紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
45	45	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
46	46	綠色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
47	47	灰金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
48	48	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
49	49	紫色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
50	50	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
51	51	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
52	52	藍色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
53	53	灰色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
54	54	黃色	610mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
55	55	紅色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
56	56	綠色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
57	57	黃色	610mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
58	58	紅色	610mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
59	59	紅色	610mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
60	60	黃色	610mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
61	61	銀色	1280mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨
62	62	銀色	640mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨
63	63	綠色	640mm x 120m	\N	\N	 性能 : 非耐磨 
64	64	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
65	65	紅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
66	66	白色	680mm x 120m	\N	\N	 性能 : 非耐磨 
67	67	金	630mm x 240m	 版縫距離 : 無縫	100~120	 性能 : 非耐磨 
68	68	綠色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
69	69	紫色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
70	70	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
71	71	藍色	1280mm x 240m	 金紙松緊度 : 檣准	100~120	 性能 : 非耐磨 
72	72	粉紅	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
73	73	綠色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
74	74	銅色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
75	75	啞金	1280mm x 240m	 金紙松緊度 : 標準偏緊	100~120	 性能 : 非耐磨 
76	76	粉紅	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
77	77	啞金	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
78	78	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
79	79	啞金	1280mm x 360m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
80	80	淺藍	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
81	81	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
82	82	金色	1280mm x 360m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
83	83	紅色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
84	84	黑色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
85	85	紫色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
86	86	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
87	87	啞金	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
88	88	銅色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
89	89	金色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
90	90	紫色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
91	91	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
92	92	玫瑰紅	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
93	93	銀色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
94	94	金色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
95	95	金色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
96	96	金色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
97	97	啞金	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
98	98	粉紅色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
99	99	啞銀	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
100	100	銅色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
101	101	啞金	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
102	102	紅色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
103	103	藍色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
104	104	綠色	1280mm x 240m	 金紙松緊度 : 標準	100~120	 性能 : 非耐磨 
105	105	紅	630mm x 240m	 版縫距離 : 無縫	100~120	 性能 : 非耐磨 
106	106	金	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
107	107	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
108	108	藍	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
109	109	紅	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
110	110	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
111	111	綠	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
112	112	金	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
113	113	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
114	114	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
115	115	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
116	116	銀	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
117	117	金	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
118	118	金色	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
119	119	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
120	120	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
121	121	金	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
122	122	銀	630mm x 240m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
123	123	藍	630mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
124	124	黑色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
125	125	啞銀	640mm x 120m	\N	\N	 性能 : 非耐磨 
126	126	紅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
127	127	綠色	640mm x 120m	\N	\N	 性能 : 非耐磨 
128	128	啞金	640mm x 120m	\N	\N	 性能 : 非耐磨 
129	129	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
130	130	銀色	640mm x 120m	\N	\N	 性能 : 非耐磨 
131	131	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
132	132	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
133	133	藍色	640mm x 120m	\N	\N	 性能 : 非耐磨 
134	134	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
135	135	粉紅	640mm x 120m	\N	\N	 性能 : 非耐磨 
136	136	啞金	640mm x 120m	\N	\N	 性能 : 非耐磨 
137	137	紅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
138	138	紫色	640mm x 120m	\N	\N	 性能 : 非耐磨 
139	139	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
140	140	紫色	640mm x 120m	\N	\N	 性能 : 非耐磨 
141	141	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
142	142	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
143	143	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
144	144	啡色	640mm x 120m	\N	\N	 性能 : 非耐磨 
145	145	粉紅	640mm x 120m	\N	\N	 性能 : 非耐磨 
146	146	紫色	640mm x 120m	\N	\N	 性能 : 非耐磨 
147	147	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
148	148	灰色	640mm x 120m	 金紙松緊度 : 標準	\N	 性能 : 非耐磨 
149	149	綠色	640mm x 120m	\N	\N	 性能 : 非耐磨 
150	150	金色	630mm x 183m	\N	\N	 性能 : 非耐磨
151	151	銀色	630mm x 183m	\N	\N	 性能 : 非耐磨
152	152	金色	630mm x 120m	\N	\N	 性能 : 非耐磨
153	153	紅色	1280mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 普通耐磨 
154	154	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
155	155	粉紅	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
156	156	淺藍	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
157	157	藍色	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
158	158	藍色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
159	159	啞金	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
160	160	灰金色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
161	161	藍色	1280mm x 480m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
162	162	金色	1280mm x 480m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
163	163	紅色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
164	164	金色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
165	165	藍綠色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
166	166	綠色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
167	167	黑色	1280mm x 240m	 金紙松緊度 : 標准	100~120	 性能 : 普通耐磨 
168	168	綠色	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
169	169	淺藍	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
170	170	啞金	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
171	171	粉紅	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
172	172	紅色	640mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
173	173	藍色	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
174	174	金色	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
175	175	紅色	1280mm x 240m	 金紙松緊度 : 松身	100~120	 性能 : 普通耐磨 
176	176	青銅色	610mm x 120m	 金紙松緊度 : 標准	100~120	 性能 : 非耐磨 
177	177	金色 	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
178	178	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
179	179	拉絲銀色	640mm x 120m	\N	\N	 性能 : 非耐磨 
180	180	啞金	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
181	181	啞銀	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
182	182	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
183	183	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
184	184	金色 	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
185	185	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
186	186	啞銀	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
187	187	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
188	188	啞銀	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
189	189	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
190	190	金色 	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
191	191	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
192	192	紫紅色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
193	193	啞金	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
194	194	金色 	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
195	195	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
196	196	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
197	197	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
198	198	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
199	199	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
200	200	紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
201	201	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
202	202	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
203	203	紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
204	204	銀色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
205	205	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
206	206	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
207	207	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
208	208	啡色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
209	209	啞銀	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
210	210	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
211	211	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
212	212	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
213	213	銅色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
214	214	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
215	215	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
216	216	藍	640mm x 120m	 版縫距離 : 46CM	105~125	版縫粗0.2mm
217	217	綠	640mm x 120m	 版縫距離 : 46CM	105~125	版縫粗0.2mm
218	218	粉紅	640mm x 120m	 版縫距離 : 46CM	110~130	版縫粗0.2mm
219	219	金	640mm x 120m	 版縫距離 : 46CM	110~130	版縫粗0.2mm
220	220	銅色	640mm x 120m	 版縫距離 : 46CM	110~130	版縫粗0.2mm
221	221	銅色	640mm x 120m	 版縫距離 : 46CM	110~130	版縫粗0.2mm
222	222	銀色	640mm x 240m	 版縫距離 : 無縫(KM100S-P220)	110~130	 性能 : 非耐磨 
223	223	藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
224	224	紫色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
225	225	銀色	1280mm x 240m	\N	110~130	 性能 : 非耐磨 
226	226	紅色	610mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
227	227	藍色	610mm x 120m	\N	\N	 性能 : 非耐磨 
228	228	藍色	610mm x 120m	\N	\N	 性能 : 非耐磨 
229	229	黑色	610mm x 120m	\N	\N	 性能 : 非耐磨 
230	230	啡色	610mm x 120m	\N	\N	 性能 : 非耐磨 
231	231	紫色	610mm x 120m	\N	\N	 性能 : 非耐磨 
232	232	粉紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
233	233	粉紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
234	234	紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
235	235	粉紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
236	236	黃色	610mm x 120m	\N	\N	 性能 : 非耐磨 
237	237	藍色	610mm x 120m	\N	\N	 性能 : 非耐磨 
238	238	紅色	610mm x 120m	\N	\N	 性能 : 非耐磨 
239	239	灰色	610mm x 120m	\N	\N	 性能 : 非耐磨 
240	240	藍色	610mm x 120m	\N	\N	 性能 : 非耐磨 
241	241	粉紅色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
242	242	粉紅色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
243	243	綠色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
244	244	銀色	640mm x 120m	\N	\N	 性能 : 非耐磨 
245	245	啞金	1280mm x 180m	\N	\N	 性能 : 非耐磨 
246	246	金色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
247	247	銅色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
248	248	啞銀	640mm x 120m	\N	110~130	 性能 : 非耐磨 
249	249	黑色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
250	250	銀色	640mm x 240m	\N	110~130	 性能 : 非耐磨 
251	251	啞銀	1280mm x 180m	\N	110~130	 性能 : 非耐磨 
252	252	銅色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
253	253	金色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
254	254	啞金	640mm x 240m	\N	110~130	 性能 : 非耐磨 
255	255	金色	640mm x 240m	\N	110~130	 性能 : 非耐磨 
256	256	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
257	257	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
258	258	紫色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
259	259	紅	630mm x 120m	 版縫距離 : 46CM	110~130	版縫線粗為0.46MM
260	260	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
261	261	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
262	262	銀	630mm x 120m	 版縫距離 : 無縫	110~130	燙後印
263	263	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
264	264	綠	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
265	265	銅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
266	266	淺藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
267	267	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
268	268	銀	630mm x 120m	 版縫距離 : 無縫	110~130	燙後印
269	269	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
270	270	金	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
271	271	銀	630mm x 240m	 版縫距離 : 無縫	110~130	松身實地燙
272	272	透明	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
273	273	透明	630mm x 120m	 版縫距離 : 52CM	105~125	 性能 : 非耐磨 
274	274	透明	630mm x 120m	 版縫距離 : 46CM	105~125	版縫線粗0.46MM
275	275	透明	630mm x 120m	 版縫距離 : 46CM	\N	 性能 : 非耐磨 
276	276	透明	630mm x 120m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
277	277	透明	630mm x 240m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
278	278	透明	630mm x 240m	 版縫距離 : 46CM	\N	 性能 : 非耐磨 
279	279	透明	630mm x 120m	 版縫距離 : 52CM	\N	 性能 : 非耐磨 
280	280	透明	630mm x 120m	 版縫距離 : 54CM	\N	 性能 : 非耐磨 
281	281	透明	630mm x 120m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
282	282	透明	630mm x 240m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
283	283	透明	630mm x 120m	\N	\N	 性能 : 非耐磨 
284	284	透明	630mm x 120m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
285	285	銀	630mm x 240m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
286	286	銀	630mm x 240m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
287	287	古銅色	630mm x 120m	 版縫距離 : 46CM	\N	版縫線粗0.46MM
288	288	淺藍	630mm x 120m	 版縫距離 : 46CM	\N	 性能 : 非耐磨 
289	289	紅	630mm x 120m	 版縫距離 : 61CM	110~130	 性能 : 非耐磨 
290	290	粉紅	630mm x 120m	 版縫距離 : 50CM	110~130	 性能 : 非耐磨 
291	291	銅色	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
292	292	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
293	293	粉紅	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
294	294	金黃	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
295	295	金黃	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
296	296	紫紅	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
297	297	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
298	298	綠色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
299	299	銀	630mm x 120m	 版縫距離 : 版距46CM	110~130	線粗0.3MM
300	300	紫	630mm x 120m	 版縫距離 : 46CM	110~130	版縫線粗為0.46MM
301	301	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
302	302	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
303	303	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
304	304	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
305	305	深藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
306	306	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
307	307	淺藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
308	308	藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
309	309	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
310	310	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
311	311	金黃	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
312	312	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
313	313	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
314	314	紫色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
315	315	藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
316	316	綠色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
317	317	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
318	318	藍	630mm x 120m	 版縫距離 : 46CM	110~130	版式縫線粗0.3MM
319	319	金	630mm x 120m	 版縫距離 : 50CM	110~130	 性能 : 非耐磨 
320	320	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
321	321	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
322	322	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
323	323	七彩	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
324	324	紫	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
325	325	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
326	326	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
327	327	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
328	328	紫	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
329	329	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
330	330	金	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
331	331	綠	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
332	332	粉紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
333	333	綠	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
334	334	綠色	630mm x 120m	 版縫距離 : 46CM	110~130	 性能 : 非耐磨 
335	335	橙	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
336	336	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
337	337	紅	630mm x 120m	 版縫距離 : 有縫	110~130	版距46CM
338	338	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
339	339	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
340	340	金	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
341	341	紅	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
342	342	藍色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
343	343	藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
344	344	藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
345	345	綠色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
346	346	銀色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
347	347	銅色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
348	348	藍色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
349	349	銀	630mm x 240m	 版縫距離 : 無縫(燙後印刷)	110~130	 性能 : 非耐磨 
350	350	銀色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
351	351	銀色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
352	352	金黃	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
353	353	淺藍色	630mm x 120m	 版縫距離 : 縫距:46CM	110~130	線粗0.3MM
354	354	金色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
355	355	紫色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
356	356	金黃	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
357	357	紅色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
358	358	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
359	359	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
360	360	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
361	361	彩虹	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
362	362	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
363	363	銀色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
364	364	橙	630mm x 120m	 版縫距離 : 有縫	110~130	版距50CM
365	365	淺藍	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
366	366	藍色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
367	367	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
368	368	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
369	369	綠色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
370	370	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
371	371	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
372	372	銅	630mm x 120m	 版縫距離 : 有縫	110~130	版距50CM
373	373	藍色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
374	374	綠色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
375	375	紫色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
376	376	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
377	377	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
378	378	銀色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
379	379	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
380	380	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
381	381	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
382	382	金色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
383	383	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
384	384	金	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
385	385	金色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
386	386	粉紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
387	387	淺藍色	630mm x 120m	 版縫距離 : 50CM	110~130	 性能 : 非耐磨 
388	388	金色	630mm x 120m	 版縫距離 : 有縫	110~130	版距:53.5CM
389	389	銀	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨
390	390	粉紅色	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
391	391	銅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
392	392	黑	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
393	393	金	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
394	394	紅	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
395	395	銀	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
396	396	紫	630mm x 120m	 版縫距離 : 46CM	110~130	版縫線粗為0.3MM
397	397	金	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
398	398	綠色	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
399	399	金黃	630mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
400	400	素面綠	630mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
401	401	藍色	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
402	402	金色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
403	403	珍珠白	610mm x 120m	\N	110~130	 性能 : 非耐磨
404	404	淺藍色	610mm x 120m	\N	110~130	 性能 : 非耐磨 
405	405	金色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
406	406	銅色	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
407	407	啞金	610mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
408	408	啞金	610mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
409	409	啞金	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
410	410	啞金	610mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
411	411	啞金	610mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
412	412	金色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
413	413	啞金	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
414	414	銀色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
415	415	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
416	416	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
417	417	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
418	418	銀色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
419	419	啞金	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
420	420	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
421	421	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
422	422	啞金	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
423	423	藍色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
424	424	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
425	425	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
426	426	銀色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
427	427	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
428	428	金色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
429	429	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
430	430	紫色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
431	431	啞金	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
432	432	銅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
433	433	藍色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
434	434	黑色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
435	435	紅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
436	436	啞金	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
437	437	金色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
438	438	銅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
439	439	銀色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
440	440	銅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
441	441	啞金	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
442	442	啞金	610mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
443	443	紅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
444	444	啞金	610mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
445	445	紅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
446	446	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
447	447	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
448	448	啞金	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
449	449	銅色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
450	450	啞金	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
451	451	啞銀	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
452	452	藍色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
453	453	啞銀	610mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
454	454	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
455	455	啞金	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
456	456	啞金	610mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
457	457	金色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
458	458	銀色	1220mm x 244m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
459	459	銅色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
460	460	淺藍色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
461	461	紅色	610mm x 120m	\N	105~125	 性能 : 非耐磨 
462	462	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
463	463	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
464	464	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
465	465	銅色	640mm x 120m	\N	\N	 性能 : 非耐磨 
466	466	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
467	467	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
468	468	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
469	469	綠色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
470	470	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
471	471	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
472	472	綠色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
473	473	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
474	474	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
475	475	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
476	476	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
477	477	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
478	478	金色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
479	479	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
480	480	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
481	481	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
482	482	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
483	483	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
484	484	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
485	485	金色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
486	486	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
487	487	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
488	488	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
489	489	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
490	490	黑色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
491	491	金色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
492	492	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
493	493	啞銀	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
494	494	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
495	495	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
496	496	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
497	497	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
498	498	啡色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
499	499	啞金	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
500	500	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
501	501	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
502	502	粉紅	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
503	503	金色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
504	504	啞金	640mm x 240m	\N	105~125	 性能 : 非耐磨 
505	505	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
506	506	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
507	507	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
508	508	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
509	509	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
510	510	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
511	511	銅色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
512	512	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
513	513	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
514	514	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
515	515	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
516	516	銀色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
517	517	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
518	518	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
519	519	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
520	520	金色	640mm x 360m	\N	105~125	 性能 : 非耐磨 
521	521	銀色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
522	522	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
523	523	粉紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
524	524	啞金	640mm x 240m	\N	105~125	 性能 : 非耐磨 
525	525	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
526	526	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
527	527	紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
528	528	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
529	529	粉紅色	640mm x 240m	\N	105~125	 性能 : 非耐磨 
530	530	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
531	531	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
532	532	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
533	533	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
534	534	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
535	535	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
536	536	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
537	537	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
538	538	紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
539	539	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
540	540	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
541	541	玫瑰紅	640mm x 120m	\N	105~125	 性能 : 非耐磨 
542	542	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
543	543	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
544	544	銀色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
545	545	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
546	546	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
547	547	啞銀	640mm x 120m	\N	105~125	 性能 : 非耐磨 
548	548	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
549	549	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
550	550	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
551	551	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
552	552	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
553	553	啞金	640mm x 240m	\N	105~125	 性能 : 非耐磨 
554	554	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
555	555	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
556	556	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
557	557	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
558	558	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
559	559	粉紅	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
560	560	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
561	561	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
562	562	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
563	563	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
564	564	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
565	565	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
566	566	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
567	567	金色 	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
568	568	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
569	569	銅色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
570	570	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
571	571	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
572	572	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
573	573	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
574	574	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
575	575	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
576	576	紫色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
577	577	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
578	578	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
579	579	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
580	580	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
581	581	啞銀	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
582	582	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
583	583	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
584	584	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
585	585	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
586	586	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
587	587	藍色	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
588	588	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
589	589	黑色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
590	590	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
591	591	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
592	592	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
593	593	銅色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
594	594	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
595	595	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
596	596	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
597	597	金色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
598	598	銀色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
599	599	金色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
600	600	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
601	601	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
602	602	紫色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
603	603	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
604	604	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
605	605	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
606	606	啞金	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
607	607	銀色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
608	608	銅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
609	609	紅色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
610	610	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
611	611	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
612	612	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
613	613	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
614	614	玫瑰紅	640mm x 120m	\N	105~125	 性能 : 非耐磨 
615	615	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
616	616	紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
617	617	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
618	618	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
619	619	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
620	620	金色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
621	621	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
622	622	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
623	623	玫瑰紅	640mm x 120m	\N	105~125	 性能 : 非耐磨 
624	624	紫紅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
625	625	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
626	626	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
627	627	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
628	628	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
629	629	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
630	630	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
631	631	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
632	632	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
633	633	藍色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
634	634	啞銀	640mm x 120m	\N	105~125	 性能 : 非耐磨 
635	635	啞銀	640mm x 120m	\N	105~125	 性能 : 非耐磨 
636	636	紫色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
637	637	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
638	638	銅色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
639	639	啡色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
640	640	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
641	641	啞金	640mm x 120m	\N	105~125	 性能 : 非耐磨 
642	642	綠色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
643	643	綠色	640mm x 120m	\N	105~125	 性能 : 非耐磨 
644	644	啞金	640mm x 240m	 金紙松緊度 : 標準偏緊	105~125	 性能 : 非耐磨 
645	645	銅色	640mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
646	646	紫色	640mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
647	647	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 抗氧化 
648	648	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 抗氧化 
649	649	啞銀	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 抗氧化 
650	650	金色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 抗氧化 
651	651	啞銀	1280mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 抗氧化 
652	652	啞金	1280mm x 240m	 金紙松緊度 : 標准(用於美心產品燙後不能有粉)	\N	 性能 : 抗氧化 
653	653	啞金	1280mm x 240m	 金紙松緊度 : 標准(用於美心產品燙後不能有粉)	\N	 性能 : 抗氧化 
654	654	珍珠粉紅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨
655	655	透明	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
656	656	透明	640mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
657	657	珍珠白	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨
658	658	珍珠白	640mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨
659	659	珍珠白	640mm x 120m	\N	\N	 性能 : 非耐磨
660	660	珍珠綠色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨
661	661	啡色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
662	662	紅色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
663	663	藍色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
664	664	啞金	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
665	665	啞金	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
666	666	啞銀	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
667	667	金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
668	668	啞金	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
669	669	紅色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
670	670	黑色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
671	671	金色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
672	672	銅色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
673	673	銀色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
674	674	啞銀	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
675	675	啞金	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
676	676	粉紅	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
677	677	啡色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
678	678	黑色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
679	679	綠色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
680	680	銀色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
681	681	綠色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
682	682	銅色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
683	683	淺藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
684	684	藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
685	685	粉紅	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
686	686	紅色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
687	687	藍色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
688	688	淺藍色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
689	689	粉紅	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
690	690	灰金色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
691	691	灰金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
692	692	綠色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
693	693	粉紅	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
694	694	藍色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
695	695	藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
696	696	金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 普通耐磨 
697	697	珍珠白	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨
698	698	金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
699	699	綠色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
700	700	金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
701	701	啞金	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
702	702	啞銀	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
703	703	金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
704	704	啞金	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
705	705	啡色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
706	706	灰金色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
707	707	啞金	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
708	708	啞銀	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
709	709	紅色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
710	710	粉紅	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
711	711	紫色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
712	712	綠色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
713	713	淺藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
714	714	藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
715	715	紅色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
716	716	藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
717	717	粉紅	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
718	718	黑色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
719	719	藍色	1280mm x 360m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
720	720	紅色	1280mm x 360m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
721	721	綠色	1280mm x 360m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
722	722	啞金	1280mm x 480m	\N	110~130	 性能 : 非耐磨 
723	723	綠色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
724	724	珍珠白	640mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
725	725	銀色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
726	726	啡色	1280mm x 360m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
727	727	珍珠白	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨
728	728	紅色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
729	729	粉紅	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
730	730	藍色	1280mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
731	731	啞金	1280mm x 240m	\N	110~130	 性能 : 非耐磨 
732	732	紅色	1280mm x 480m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
733	733	藍色	640mm x 240m	 金紙松緊度 : 松身	110~130	 性能 : 非耐磨 
734	734	粉紅	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
735	735	紅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
736	736	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
737	737	啡色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
738	738	紫色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
739	739	藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
740	740	淺藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
741	741	藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
742	742	啞銀	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
743	743	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
744	744	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
745	745	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
746	746	啞金	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
747	747	紅色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
748	748	金色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 耐磨普通耐磨 
749	749	藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
750	750	銀色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
751	751	藍色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
752	752	銀色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
753	753	啞銀	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
754	754	啞金	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
755	755	粉紅	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
756	756	淺藍色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
757	757	啡色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
758	758	紅色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
759	759	黑色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
760	760	紫色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
761	761	綠色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
762	762	啞金	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
763	763	藍色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
764	764	綠色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
765	765	灰金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
766	766	灰金色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
767	767	黑色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
768	768	藍色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
769	769	粉紅	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
770	770	紅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
771	771	綠色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
772	772	綠色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
773	773	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
774	774	銅色	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
775	775	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
776	776	綠色	1280mm x 360m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
777	777	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
778	778	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
779	779	粉紅	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
780	780	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 耐磨 
781	781	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
782	782	啞金	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
783	783	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
784	784	啞銀	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
785	785	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
786	786	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
787	787	藍色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
788	788	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
789	789	藍色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
790	790	藍色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
791	791	藍色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
792	792	紅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
793	793	紫色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
794	794	紫色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
795	795	粉紅	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 普通耐磨 
796	796	綠色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 普通耐磨 
797	797	紫色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 耐磨 
798	798	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 耐磨 
799	799	啞金	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
800	800	金色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
801	801	紅色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
802	802	啞金	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
803	803	銀色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
804	804	啞銀	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
805	805	啞金	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
806	806	藍色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
807	807	粉紅	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
808	808	藍色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
809	809	淺藍色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
810	810	啡色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
811	811	藍色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
812	812	珍珠白	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨
813	813	紅色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
814	814	黑色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
815	815	灰金色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
816	816	綠色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
817	817	紫色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
818	818	綠色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
819	819	粉紅	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
820	820	銅色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
821	821	青銅色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
822	822	啞銀	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
823	823	粉紅	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
824	824	啞金	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
825	825	金色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
826	826	啡色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
827	827	啞金	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
828	828	藍色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
829	829	銀色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
830	830	灰金色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
831	831	紅色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
832	832	藍色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
833	833	紅色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
834	834	珍珠白	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨
835	835	黑色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
836	836	淺藍色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
837	837	綠色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
838	838	紫色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
839	839	綠色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
840	840	啞金	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
841	841	銅色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
842	842	金色	1280mm x 240m	 金紙松緊度 : 檣准	110~130	 性能 : 非耐磨 
843	843	藍色	1280mm x 480m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
844	844	粉紅	1280mm x 480m	 金紙松緊度 : 標準偏松	110~130	 性能 : 非耐磨 
845	845	紫色	1280mm x 240m	 金紙松緊度 : 標准偏松	110~130	 性能 : 非耐磨 
846	846	綠色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
847	847	啡色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
848	848	金色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
849	849	灰金色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
850	850	啞金	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
851	851	粉紅	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
852	852	啞金	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
853	853	銀色	640mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
854	854	藍色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
855	855	淺藍色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
856	856	綠色	640mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
857	857	紅色	640mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
858	858	金色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
859	859	金色	1280mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
860	860	金色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
861	861	紫色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
862	862	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
863	863	紅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
864	864	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
865	865	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
866	866	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
867	867	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
868	868	啞銀	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
869	869	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
870	870	啞金	1280mm x 240m	\N	110~130	 性能 : 非耐磨 
871	871	金色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
872	872	藍色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
873	873	綠色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
874	874	啞金	1280mm x 240m	\N	110~130	 性能 : 非耐磨 
875	875	紅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
876	876	藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
877	877	藍色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
878	878	淺藍色	1280mm x 360m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
879	879	綠色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
880	880	啞銀	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
881	881	藍色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
882	882	粉紅	1280mm x 480m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
883	883	啞金	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
884	884	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
885	885	藍色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
886	886	啞金	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
887	887	粉紅	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
888	888	金色	1280mm x 360m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
889	889	粉紅	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
890	890	啞金	1280mm x 480m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
891	891	啞銀	1280mm x 480m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
892	892	金色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
893	893	銅色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
894	894	紫色	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
895	895	珍珠白	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨
896	896	啞金	1280mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
897	897	紅色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
898	898	藍色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
899	899	綠色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
900	900	啞金	640mm x 120m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
901	901	銀色	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
902	902	金色  	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
903	903	金色	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
904	904	啞金	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
905	905	啞金	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
906	906	金色	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
907	907	銅色	1280mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
908	908	啞金	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
909	909	金色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
910	910	啞銀	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
911	911	啞金	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
912	912	銅色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
913	913	藍色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
914	914	紅色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
915	915	粉紅	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
916	916	綠色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
917	917	黑色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
918	918	銅色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
919	919	藍色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
920	920	藍色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
921	921	藍色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
922	922	啞金	640mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
923	923	啞金	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
924	924	銅色	1280mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
925	925	青銅色	640mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
926	926	啞金	640mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
927	927	藍色	640mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
928	928	紫色	1280mm x 240m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
929	929	紅色	640mm x 240m	 金紙松緊度 : 標準偏緊	110~130	 性能 : 非耐磨 
930	930	啞金	640mm x 240m	 金紙松緊度 : 標准偏緊	110~130	燙後不能有粉
931	931	粉紅	640mm x 240m	 金紙松緊度 : 標准偏緊	110~130	燙後不能有粉
932	932	金色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
933	933	銅色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
934	934	橙色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
935	935	橙色	640mm x 120m	\N	110~130	 性能 : 非耐磨 
936	936	啞金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
937	937	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
938	938	金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
939	939	啞金色	640mm x 120m	\N	\N	 性能 : 非耐磨 
940	940	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
941	941	金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
942	942	藍色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
943	943	銅色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
944	944	彩虹色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
945	945	啞金色	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 非耐磨 
946	946	金色	1280mm x 360m	 金紙松緊度 : 標准	\N	 性能 : 普通耐磨 
947	947	金色	1280mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 普通耐磨 
948	948	綠色	1280mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 普通耐磨 
949	949	紅色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
950	950	淺藍色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
951	951	金色	1280mm x 240m	 金紙松緊度 : 松身	\N	 性能 : 普通耐磨 
952	952	銀	630mm x 240m	 版縫距離 : 無縫	\N	 性能 : 普通耐磨 
953	953	啞金	640mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 高耐磨 
954	954	啞金	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 高耐磨 
955	955	啞金	640mm x 120m	 金紙松緊度 : 標准	\N	 性能 : 高耐磨 
956	956	紅色	1280mm x 240m	 金紙松緊度 : 標准	\N	 性能 : 高耐磨 
957	957	黑色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
958	958	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
959	959	啡色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
960	960	啞銀	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
961	961	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
962	962	淺藍色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
963	963	銅色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
964	964	粉紅色	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
965	965	粉紅色	1280mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 高耐磨 
966	966	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
967	967	啞金	640mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 高耐磨 
968	968	銀色	1280mm x 240m	 金紙松緊度 : 標準	105~125	 性能 : 高耐磨 
969	969	啞金	1280mm x 240m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
970	970	啞金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 高耐磨 
971	971	黃色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
972	972	白色	610mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
973	973	黑色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
974	974	白色	610mm x 120m	 金紙松緊度 : 松	105~125	 性能 : 非耐磨 
975	975	黑色	610mm x 120m	\N	105~125	 性能 : 非耐磨 
976	976	藍色	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
977	977	金	640mm x 120m	 金紙松緊度 : 標准	105~125	 性能 : 非耐磨 
978	978	白色	610mm x 120m	 金紙松緊度 : 標準	105~125	 性能 : 非耐磨 
979	979	金	640mm x 120m	 版縫距離 : 無縫	105~125	 性能 : 非耐磨 
980	980	金	640mm x 360m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
981	981	綠色	640mm x 120m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
982	982	金	640mm x 360m	 版縫距離 : 無縫	\N	 性能 : 非耐磨 
983	983	金	640mm x 360m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
984	984	金	640mm x 360m	\N	110~130	 性能 : 非耐磨 
985	985	金	640mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
986	986	素面古銅	640mm x 360m	\N	110~130	 性能 : 非耐磨 
987	987	淺藍色	640mm x 120m	 版縫距離 : 無縫	110~130	與401-07相似
988	988	紫紅色	640mm x 240m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
989	989	銀色	640mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
990	990	綠色	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
991	991	紫色	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
992	992	啡色	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
993	993	粉紅	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
994	994	綠色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
995	995	金色  	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
996	996	紫色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
997	997	粉紅	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
998	998	銅色	640mm x 240m	 金紙松緊度 : 緊身	110~130	 性能 : 非耐磨 
999	999	啞銀	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1000	1000	啞金	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1001	1001	啞金	640mm x 180m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
1002	1002	啞銀色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1003	1003	啞金	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1004	1004	綠色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
1005	1005	啞古銅	640mm x 360m	 金紙松緊度 : 標准偏緊	110~130	 性能 : 非耐磨 
1006	1006	啞金色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
1007	1007	啞金色	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1008	1008	銅色	640mm x 240m	 金紙松緊度 : 標准偏緊	110~130	燙後不能有粉
1009	1009	綠色	640mm x 240m	 金紙松緊度 : 標准偏緊	110~130	燙後不能有粉
1010	1010	金色	640mm x 240m	 金紙松緊度 : 標準	110~130	 性能 : 非耐磨 
1011	1011	啞金	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1012	1012	啞金	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1013	1013	啞金	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1014	1014	啞金	640mm x 240m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1015	1015	銀色	640mm x 120m	 版縫距離 : 無縫	110~130	 性能 : 非耐磨 
1016	1016	啞金	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
1017	1017	啞金	640mm x 120m	 金紙松緊度 : 標准	110~130	 性能 : 非耐磨 
\.


--
-- TOC entry 5035 (class 0 OID 16556)
-- Dependencies: 242
-- Data for Name: user_match_preferences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_match_preferences (id, user_id, field_name, field_value, created_at, updated_at) FROM stdin;
53	1	color	銀色	2025-06-17 17:34:23.082416	2025-06-17 17:34:23.082416
54	1	size	640mm x 240m	2025-06-17 17:34:23.082416	2025-06-17 17:34:23.082416
55	1	temperature	110~130	2025-06-17 17:34:23.082416	2025-06-17 17:34:23.082416
56	1	performance	性能 : 非耐磨	2025-06-17 17:34:23.082416	2025-06-17 17:34:23.082416
\.


--
-- TOC entry 5032 (class 0 OID 16459)
-- Dependencies: 239
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, password, email, is_active, created_at, role) FROM stdin;
1	admin	$2a$10$quIr6wkfixaFt9HNRyxyeO3r98Oi9hcl1Yo6.iCAmjZEzVSDfE9/i	134679@example.com	t	2025-04-16 11:15:42.171552	USER
\.


--
-- TOC entry 5055 (class 0 OID 0)
-- Dependencies: 218
-- Name: cloth_paper_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cloth_paper_id_seq', 1, false);


--
-- TOC entry 5056 (class 0 OID 0)
-- Dependencies: 220
-- Name: film_butter_paper_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.film_butter_paper_id_seq', 1, false);


--
-- TOC entry 5057 (class 0 OID 0)
-- Dependencies: 222
-- Name: gold_foil_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.gold_foil_type_id_seq', 1, false);


--
-- TOC entry 5058 (class 0 OID 0)
-- Dependencies: 224
-- Name: leo_gp_numbers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.leo_gp_numbers_id_seq', 3, true);


--
-- TOC entry 5059 (class 0 OID 0)
-- Dependencies: 226
-- Name: matte_lamination_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.matte_lamination_id_seq', 1, false);


--
-- TOC entry 5060 (class 0 OID 0)
-- Dependencies: 228
-- Name: oil_and_uv_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oil_and_uv_types_id_seq', 1, false);


--
-- TOC entry 5061 (class 0 OID 0)
-- Dependencies: 230
-- Name: pattern_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pattern_id_seq', 1, false);


--
-- TOC entry 5062 (class 0 OID 0)
-- Dependencies: 232
-- Name: pricing_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pricing_id_seq', 3, true);


--
-- TOC entry 5063 (class 0 OID 0)
-- Dependencies: 234
-- Name: processing_after_ironing_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.processing_after_ironing_id_seq', 1, false);


--
-- TOC entry 5064 (class 0 OID 0)
-- Dependencies: 236
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 1, false);


--
-- TOC entry 5065 (class 0 OID 0)
-- Dependencies: 238
-- Name: specifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.specifications_id_seq', 3, true);


--
-- TOC entry 5066 (class 0 OID 0)
-- Dependencies: 241
-- Name: user_match_preferences_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_match_preferences_id_seq', 56, true);


--
-- TOC entry 5067 (class 0 OID 0)
-- Dependencies: 240
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 1, true);


--
-- TOC entry 4828 (class 2606 OID 16481)
-- Name: cloth_paper cloth_paper_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cloth_paper
    ADD CONSTRAINT cloth_paper_pkey PRIMARY KEY (id);


--
-- TOC entry 4830 (class 2606 OID 16483)
-- Name: film_butter_paper film_butter_paper_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_butter_paper
    ADD CONSTRAINT film_butter_paper_pkey PRIMARY KEY (id);


--
-- TOC entry 4832 (class 2606 OID 16485)
-- Name: gold_foil_type gold_foil_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gold_foil_type
    ADD CONSTRAINT gold_foil_type_pkey PRIMARY KEY (id);


--
-- TOC entry 4834 (class 2606 OID 16487)
-- Name: leo_gp_numbers leo_gp_numbers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leo_gp_numbers
    ADD CONSTRAINT leo_gp_numbers_pkey PRIMARY KEY (id);


--
-- TOC entry 4836 (class 2606 OID 16489)
-- Name: matte_lamination matte_lamination_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matte_lamination
    ADD CONSTRAINT matte_lamination_pkey PRIMARY KEY (id);


--
-- TOC entry 4838 (class 2606 OID 16491)
-- Name: oil_and_uv_types oil_and_uv_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oil_and_uv_types
    ADD CONSTRAINT oil_and_uv_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4840 (class 2606 OID 16493)
-- Name: pattern pattern_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pattern
    ADD CONSTRAINT pattern_pkey PRIMARY KEY (id);


--
-- TOC entry 4842 (class 2606 OID 16495)
-- Name: pricing pricing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pricing
    ADD CONSTRAINT pricing_pkey PRIMARY KEY (id);


--
-- TOC entry 4844 (class 2606 OID 16497)
-- Name: processing_after_ironing processing_after_ironing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.processing_after_ironing
    ADD CONSTRAINT processing_after_ironing_pkey PRIMARY KEY (id);


--
-- TOC entry 4846 (class 2606 OID 16499)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 4848 (class 2606 OID 16501)
-- Name: specifications specifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.specifications
    ADD CONSTRAINT specifications_pkey PRIMARY KEY (id);


--
-- TOC entry 4852 (class 2606 OID 16566)
-- Name: user_match_preferences unique_user_field_value; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_match_preferences
    ADD CONSTRAINT unique_user_field_value UNIQUE (user_id, field_name, field_value);


--
-- TOC entry 4854 (class 2606 OID 16564)
-- Name: user_match_preferences user_match_preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_match_preferences
    ADD CONSTRAINT user_match_preferences_pkey PRIMARY KEY (id);


--
-- TOC entry 4850 (class 2606 OID 16503)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4855 (class 2606 OID 16504)
-- Name: cloth_paper cloth_paper_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cloth_paper
    ADD CONSTRAINT cloth_paper_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4856 (class 2606 OID 16509)
-- Name: film_butter_paper film_butter_paper_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film_butter_paper
    ADD CONSTRAINT film_butter_paper_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4857 (class 2606 OID 16514)
-- Name: gold_foil_type gold_foil_type_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gold_foil_type
    ADD CONSTRAINT gold_foil_type_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4858 (class 2606 OID 16519)
-- Name: leo_gp_numbers leo_gp_numbers_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leo_gp_numbers
    ADD CONSTRAINT leo_gp_numbers_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4859 (class 2606 OID 16524)
-- Name: matte_lamination matte_lamination_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matte_lamination
    ADD CONSTRAINT matte_lamination_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4860 (class 2606 OID 16529)
-- Name: oil_and_uv_types oil_and_uv_types_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oil_and_uv_types
    ADD CONSTRAINT oil_and_uv_types_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4861 (class 2606 OID 16534)
-- Name: pattern pattern_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pattern
    ADD CONSTRAINT pattern_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4862 (class 2606 OID 16539)
-- Name: pricing pricing_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pricing
    ADD CONSTRAINT pricing_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4863 (class 2606 OID 16544)
-- Name: processing_after_ironing processing_after_ironing_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.processing_after_ironing
    ADD CONSTRAINT processing_after_ironing_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- TOC entry 4864 (class 2606 OID 16549)
-- Name: specifications specifications_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.specifications
    ADD CONSTRAINT specifications_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


-- Completed on 2025-06-26 09:36:09

--
-- PostgreSQL database dump complete
--

-- Completed on 2025-06-26 09:36:09

--
-- PostgreSQL database cluster dump complete
--

