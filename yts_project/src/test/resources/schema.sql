--
-- PostgreSQL database dump
--

-- Dumped from database version 16.6
-- Dumped by pg_dump version 16.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: special_char; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.special_char AS ENUM (
    'V',
    'X',
    'NA',
    '▷'
);


--
-- Name: special_char_new; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.special_char_new AS ENUM (
    'V',
    'X',
    'NA_Enum',
    '▷'
);


--
-- Name: trg_permissions_touch_updated_time(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.trg_permissions_touch_updated_time() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.updated_time := CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$;


--
-- Name: update_knowledge_base_trigger(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_knowledge_base_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- 标记需要重新生成embedding
    UPDATE knowledge_base_stats 
    SET last_updated = CURRENT_TIMESTAMP;
    
    -- 删除旧的embedding（如果存在）
    DELETE FROM product_knowledge_embeddings 
    WHERE product_id = NEW.id;
    
    RETURN NEW;
END;
$$;


--
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admin_change_records; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.admin_change_records (
    id bigint NOT NULL,
    entity_type character varying(100) NOT NULL,
    entity_id bigint NOT NULL,
    change_set jsonb NOT NULL,
    snapshot_ids bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    operator_id integer NOT NULL,
    operator_name character varying(100),
    risk character varying(20) DEFAULT 'low'::character varying NOT NULL,
    reason text DEFAULT ''::text NOT NULL,
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    committed_at timestamp with time zone,
    rolled_back_at timestamp with time zone,
    rollback_record_id bigint,
    error_message text
);


--
-- Name: TABLE admin_change_records; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.admin_change_records IS '管理员变更操作记录 — 每次修改对应一条记录，支持回滚';


--
-- Name: COLUMN admin_change_records.change_set; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_records.change_set IS '变更字段明细 JSON';


--
-- Name: COLUMN admin_change_records.snapshot_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_records.snapshot_ids IS '关联的快照 ID 列表';


--
-- Name: COLUMN admin_change_records.risk; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_records.risk IS '风险评估：low/medium/high';


--
-- Name: COLUMN admin_change_records.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_records.status IS '状态：pending/committed/rolled_back/failed';


--
-- Name: COLUMN admin_change_records.rollback_record_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_records.rollback_record_id IS '回滚操作产生的记录 ID，用于追溯回滚链路';


--
-- Name: admin_change_records_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.admin_change_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: admin_change_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.admin_change_records_id_seq OWNED BY public.admin_change_records.id;


--
-- Name: admin_change_snapshots; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.admin_change_snapshots (
    id bigint NOT NULL,
    entity_type character varying(100) NOT NULL,
    entity_id bigint NOT NULL,
    before_data jsonb NOT NULL,
    after_data jsonb,
    checksum character varying(64),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by integer NOT NULL
);


--
-- Name: TABLE admin_change_snapshots; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.admin_change_snapshots IS '管理员变更数据快照 — 变更前/后完整数据，用于回滚和审计';


--
-- Name: COLUMN admin_change_snapshots.entity_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_snapshots.entity_type IS '实体类型，如 pricing、specification、product、gold_foil_type 等';


--
-- Name: COLUMN admin_change_snapshots.before_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_snapshots.before_data IS '变更前的完整数据 JSON';


--
-- Name: COLUMN admin_change_snapshots.after_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_snapshots.after_data IS '变更后的完整数据 JSON（execute 时写入）';


--
-- Name: COLUMN admin_change_snapshots.checksum; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_change_snapshots.checksum IS 'before_data 的 SHA-256，回滚时校验数据未被意外修改';


--
-- Name: admin_change_snapshots_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.admin_change_snapshots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: admin_change_snapshots_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.admin_change_snapshots_id_seq OWNED BY public.admin_change_snapshots.id;


--
-- Name: admin_operation_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.admin_operation_log (
    id bigint NOT NULL,
    operator_id integer,
    operator_name character varying(100),
    operated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    operation_type character varying(50) NOT NULL,
    module character varying(100) NOT NULL,
    target_type character varying(100),
    target_id character varying(200),
    change_summary character varying(500),
    ip character varying(64),
    user_agent character varying(500),
    target_label character varying(512),
    change_detail text
);


--
-- Name: TABLE admin_operation_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.admin_operation_log IS '后台操作日志，记录谁在何时对哪块数据做了何种操作';


--
-- Name: COLUMN admin_operation_log.operator_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.operator_id IS '操作人用户ID';


--
-- Name: COLUMN admin_operation_log.operator_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.operator_name IS '操作人用户名/显示名';


--
-- Name: COLUMN admin_operation_log.operation_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.operation_type IS 'CREATE/UPDATE/DELETE/IMPORT/RESTORE/EXPORT 等';


--
-- Name: COLUMN admin_operation_log.module; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.module IS '模块名，如 燙金物料信息、導入備份與恢復';


--
-- Name: COLUMN admin_operation_log.target_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.target_type IS '被操作对象类型，如 product、import_snapshot';


--
-- Name: COLUMN admin_operation_log.target_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.target_id IS '被操作对象ID或标识';


--
-- Name: COLUMN admin_operation_log.change_summary; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.change_summary IS '变更摘要，如 导入约100条、恢复批次20260202180228';


--
-- Name: COLUMN admin_operation_log.target_label; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.target_label IS '操作对象业务描述，如物料名称(编码)，便于识别 id 对应内容';


--
-- Name: COLUMN admin_operation_log.change_detail; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.admin_operation_log.change_detail IS '变更详情，如 燙金紙系列: 旧值 → 新值，便于管理员看出具体修改内容';


--
-- Name: admin_operation_log_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.admin_operation_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: admin_operation_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.admin_operation_log_id_seq OWNED BY public.admin_operation_log.id;


--
-- Name: ai_chat_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ai_chat_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ai_recommendation_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ai_recommendation_history (
    id integer NOT NULL,
    user_id integer NOT NULL,
    request_params jsonb NOT NULL,
    recommendations jsonb DEFAULT '[]'::jsonb,
    explanation text,
    confidence numeric(5,2) DEFAULT 0.0,
    workflow_run_id character varying(100),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: ai_recommendation_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ai_recommendation_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ai_recommendation_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ai_recommendation_history_id_seq OWNED BY public.ai_recommendation_history.id;


--
-- Name: ai_search_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ai_search_history (
    id integer NOT NULL,
    user_id integer NOT NULL,
    query text NOT NULL,
    results jsonb DEFAULT '[]'::jsonb,
    explanation text,
    confidence numeric(5,2) DEFAULT 0.0,
    workflow_run_id character varying(100),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: ai_search_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ai_search_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ai_search_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ai_search_history_id_seq OWNED BY public.ai_search_history.id;


--
-- Name: cloth_paper_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cloth_paper_compatibility (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    cloth_paper_type_id integer NOT NULL,
    compatibility_status character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    notice_ids integer[],
    CONSTRAINT cloth_paper_compatibility_compatibility_status_check CHECK ((compatibility_status = ANY (ARRAY['V'::bpchar, 'X'::bpchar, 'NA'::bpchar, '▷'::bpchar])))
);


--
-- Name: COLUMN cloth_paper_compatibility.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cloth_paper_compatibility.notice_ids IS '关联的注意事项ID数组';


--
-- Name: cloth_paper_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cloth_paper_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cloth_paper_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cloth_paper_compatibility_id_seq OWNED BY public.cloth_paper_compatibility.id;


--
-- Name: old)cloth_paper; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."old)cloth_paper" (
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


--
-- Name: cloth_paper_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cloth_paper_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cloth_paper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cloth_paper_id_seq OWNED BY public."old)cloth_paper".id;


--
-- Name: cloth_paper_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cloth_paper_types (
    id integer NOT NULL,
    type_name character varying(100) NOT NULL,
    category character varying(50),
    sort_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    notice_ids integer[],
    series_priority_rule_id integer,
    exclude_from_common_interface_matrix boolean DEFAULT false NOT NULL,
    disallow_uv_print_with_type boolean DEFAULT false NOT NULL
);


--
-- Name: COLUMN cloth_paper_types.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cloth_paper_types.notice_ids IS '关联的注意事项ID数组';


--
-- Name: COLUMN cloth_paper_types.exclude_from_common_interface_matrix; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cloth_paper_types.exclude_from_common_interface_matrix IS 'true=特殊界面布面纸，导出常用界面燙印性組合應用表时不作为特殊界面列';


--
-- Name: COLUMN cloth_paper_types.disallow_uv_print_with_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cloth_paper_types.disallow_uv_print_with_type IS 'true=當選擇此布面紙類型且同時選擇燙後加工.印刷UV時，禁止聯用（匹配結果為空）';


--
-- Name: cloth_paper_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cloth_paper_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cloth_paper_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cloth_paper_types_id_seq OWNED BY public.cloth_paper_types.id;


--
-- Name: code_mapping; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.code_mapping (
    id integer NOT NULL,
    p0_table_name character varying(200) NOT NULL,
    p0_row_id integer NOT NULL,
    p0_material_code character varying(200),
    p0_material_name character varying(500) NOT NULL,
    target_type character varying(50) NOT NULL,
    target_id integer,
    target_code character varying(200),
    target_name character varying(500),
    match_type character varying(20) DEFAULT 'auto'::character varying NOT NULL,
    confidence numeric(3,2) DEFAULT 0,
    notes text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: code_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.code_mapping_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: code_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.code_mapping_id_seq OWNED BY public.code_mapping.id;


--
-- Name: cold_foil_feasibility_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cold_foil_feasibility_rules (
    id integer NOT NULL,
    type_id integer NOT NULL,
    product_type_id integer,
    feasibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc character varying(500),
    notice_ids integer[],
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE cold_foil_feasibility_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cold_foil_feasibility_rules IS '冷烫可行性规则';


--
-- Name: COLUMN cold_foil_feasibility_rules.type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_feasibility_rules.type_id IS '冷烫类型';


--
-- Name: COLUMN cold_foil_feasibility_rules.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_feasibility_rules.product_type_id IS '产品类型';


--
-- Name: COLUMN cold_foil_feasibility_rules.feasibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_feasibility_rules.feasibility_status IS '可行性：V=可行 X=不可行 NA=不确定 ▷=有条件';


--
-- Name: COLUMN cold_foil_feasibility_rules.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_feasibility_rules.condition_desc IS '条件说明';


--
-- Name: COLUMN cold_foil_feasibility_rules.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_feasibility_rules.notice_ids IS '关联注意事项ID';


--
-- Name: cold_foil_feasibility_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cold_foil_feasibility_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cold_foil_feasibility_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cold_foil_feasibility_rules_id_seq OWNED BY public.cold_foil_feasibility_rules.id;


--
-- Name: cold_foil_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cold_foil_type_options (
    id integer NOT NULL,
    type_name character varying(100) NOT NULL,
    type_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE cold_foil_type_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cold_foil_type_options IS '冷烫类型选项';


--
-- Name: COLUMN cold_foil_type_options.type_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_type_options.type_name IS '类型名称';


--
-- Name: COLUMN cold_foil_type_options.type_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.cold_foil_type_options.type_code IS '类型编码';


--
-- Name: cold_foil_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cold_foil_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cold_foil_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cold_foil_type_options_id_seq OWNED BY public.cold_foil_type_options.id;


--
-- Name: embossing_feasibility_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.embossing_feasibility_rules (
    id integer NOT NULL,
    type_id integer NOT NULL,
    product_type_id integer,
    feasibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc character varying(500),
    notice_ids integer[],
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE embossing_feasibility_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.embossing_feasibility_rules IS '击凸可行性规则';


--
-- Name: COLUMN embossing_feasibility_rules.type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_feasibility_rules.type_id IS '击凸类型';


--
-- Name: COLUMN embossing_feasibility_rules.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_feasibility_rules.product_type_id IS '产品类型';


--
-- Name: COLUMN embossing_feasibility_rules.feasibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_feasibility_rules.feasibility_status IS '可行性：V=可行 X=不可行 NA=不确定 ▷=有条件';


--
-- Name: COLUMN embossing_feasibility_rules.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_feasibility_rules.condition_desc IS '条件说明';


--
-- Name: COLUMN embossing_feasibility_rules.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_feasibility_rules.notice_ids IS '关联注意事项ID';


--
-- Name: embossing_feasibility_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.embossing_feasibility_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: embossing_feasibility_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.embossing_feasibility_rules_id_seq OWNED BY public.embossing_feasibility_rules.id;


--
-- Name: embossing_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.embossing_type_options (
    id integer NOT NULL,
    type_name character varying(100) NOT NULL,
    type_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE embossing_type_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.embossing_type_options IS '击凸类型选项';


--
-- Name: COLUMN embossing_type_options.type_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_type_options.type_name IS '类型名称';


--
-- Name: COLUMN embossing_type_options.type_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.embossing_type_options.type_code IS '类型编码';


--
-- Name: embossing_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.embossing_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: embossing_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.embossing_type_options_id_seq OWNED BY public.embossing_type_options.id;


--
-- Name: evaluation_session; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.evaluation_session (
    id bigint NOT NULL,
    name text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL
);


--
-- Name: evaluation_session_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.evaluation_session_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: evaluation_session_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.evaluation_session_id_seq OWNED BY public.evaluation_session.id;


--
-- Name: old_film_butter_paper; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.old_film_butter_paper (
    id integer NOT NULL,
    project_id integer NOT NULL,
    vet_vvc_avet public.special_char_new,
    scratch_averse_avet_no_foil public.special_char_new,
    butter_paper_gtf public.special_char_new,
    butter_paper_ztf public.special_char_new
);


--
-- Name: film_butter_paper_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.film_butter_paper_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: film_butter_paper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.film_butter_paper_id_seq OWNED BY public.old_film_butter_paper.id;


--
-- Name: gen_table; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.gen_table (
    table_id bigint NOT NULL,
    table_name character varying(200) DEFAULT ''::character varying,
    table_comment character varying(500) DEFAULT ''::character varying,
    sub_table_name character varying(64) DEFAULT NULL::character varying,
    sub_table_fk_name character varying(64) DEFAULT NULL::character varying,
    class_name character varying(100) DEFAULT ''::character varying,
    tpl_category character varying(200) DEFAULT 'crud'::character varying,
    package_name character varying(100),
    module_name character varying(30),
    business_name character varying(30),
    function_name character varying(50),
    function_author character varying(50),
    form_col_num integer DEFAULT 1,
    gen_type character(1) DEFAULT '0'::bpchar,
    gen_path character varying(200) DEFAULT '/'::character varying,
    options character varying(1000),
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE gen_table; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.gen_table IS '代码生成业务表';


--
-- Name: COLUMN gen_table.table_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.table_id IS '编号';


--
-- Name: COLUMN gen_table.table_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.table_name IS '表名称';


--
-- Name: COLUMN gen_table.table_comment; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.table_comment IS '表描述';


--
-- Name: COLUMN gen_table.sub_table_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.sub_table_name IS '关联子表的表名';


--
-- Name: COLUMN gen_table.sub_table_fk_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.sub_table_fk_name IS '子表关联的外键名';


--
-- Name: COLUMN gen_table.class_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.class_name IS '实体类名称';


--
-- Name: COLUMN gen_table.tpl_category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.tpl_category IS '使用的模板（crud单表操作 tree树表操作）';


--
-- Name: COLUMN gen_table.package_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.package_name IS '生成包路径';


--
-- Name: COLUMN gen_table.module_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.module_name IS '生成模块名';


--
-- Name: COLUMN gen_table.business_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.business_name IS '生成业务名';


--
-- Name: COLUMN gen_table.function_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.function_name IS '生成功能名';


--
-- Name: COLUMN gen_table.function_author; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.function_author IS '生成功能作者';


--
-- Name: COLUMN gen_table.form_col_num; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.form_col_num IS '表单布局（单列 双列 三列）';


--
-- Name: COLUMN gen_table.gen_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.gen_type IS '生成代码方式（0zip压缩包 1自定义路径）';


--
-- Name: COLUMN gen_table.gen_path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.gen_path IS '生成路径（不填默认项目路径）';


--
-- Name: COLUMN gen_table.options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.options IS '其它生成选项';


--
-- Name: COLUMN gen_table.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.create_by IS '创建者';


--
-- Name: COLUMN gen_table.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.create_time IS '创建时间';


--
-- Name: COLUMN gen_table.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.update_by IS '更新者';


--
-- Name: COLUMN gen_table.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.update_time IS '更新时间';


--
-- Name: COLUMN gen_table.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table.remark IS '备注';


--
-- Name: gen_table_column; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.gen_table_column (
    column_id bigint NOT NULL,
    table_id bigint,
    column_name character varying(200) DEFAULT NULL::character varying,
    column_comment character varying(500) DEFAULT NULL::character varying,
    column_type character varying(100) DEFAULT NULL::character varying,
    java_type character varying(500) DEFAULT NULL::character varying,
    java_field character varying(200) DEFAULT NULL::character varying,
    is_pk character(1) DEFAULT NULL::bpchar,
    is_increment character(1) DEFAULT NULL::bpchar,
    is_required character(1) DEFAULT NULL::bpchar,
    is_insert character(1) DEFAULT NULL::bpchar,
    is_edit character(1) DEFAULT NULL::bpchar,
    is_list character(1) DEFAULT NULL::bpchar,
    is_query character(1) DEFAULT NULL::bpchar,
    query_type character varying(200) DEFAULT 'EQ'::character varying,
    html_type character varying(200) DEFAULT NULL::character varying,
    dict_type character varying(200) DEFAULT ''::character varying,
    sort integer,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone
);


--
-- Name: TABLE gen_table_column; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.gen_table_column IS '代码生成业务表字段';


--
-- Name: COLUMN gen_table_column.column_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.column_id IS '编号';


--
-- Name: COLUMN gen_table_column.table_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.table_id IS '归属表编号';


--
-- Name: COLUMN gen_table_column.column_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.column_name IS '列名称';


--
-- Name: COLUMN gen_table_column.column_comment; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.column_comment IS '列描述';


--
-- Name: COLUMN gen_table_column.column_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.column_type IS '列类型';


--
-- Name: COLUMN gen_table_column.java_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.java_type IS 'JAVA类型';


--
-- Name: COLUMN gen_table_column.java_field; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.java_field IS 'JAVA字段名';


--
-- Name: COLUMN gen_table_column.is_pk; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_pk IS '是否主键（1是）';


--
-- Name: COLUMN gen_table_column.is_increment; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_increment IS '是否自增（1是）';


--
-- Name: COLUMN gen_table_column.is_required; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_required IS '是否必填（1是）';


--
-- Name: COLUMN gen_table_column.is_insert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_insert IS '是否为插入字段（1是）';


--
-- Name: COLUMN gen_table_column.is_edit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_edit IS '是否编辑字段（1是）';


--
-- Name: COLUMN gen_table_column.is_list; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_list IS '是否列表字段（1是）';


--
-- Name: COLUMN gen_table_column.is_query; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.is_query IS '是否查询字段（1是）';


--
-- Name: COLUMN gen_table_column.query_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.query_type IS '查询方式（等于、不等于、大于、小于、范围）';


--
-- Name: COLUMN gen_table_column.html_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.html_type IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';


--
-- Name: COLUMN gen_table_column.dict_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.dict_type IS '字典类型';


--
-- Name: COLUMN gen_table_column.sort; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.sort IS '排序';


--
-- Name: COLUMN gen_table_column.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.create_by IS '创建者';


--
-- Name: COLUMN gen_table_column.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.create_time IS '创建时间';


--
-- Name: COLUMN gen_table_column.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.update_by IS '更新者';


--
-- Name: COLUMN gen_table_column.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.gen_table_column.update_time IS '更新时间';


--
-- Name: gen_table_column_column_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.gen_table_column_column_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: gen_table_column_column_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.gen_table_column_column_id_seq OWNED BY public.gen_table_column.column_id;


--
-- Name: gen_table_table_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.gen_table_table_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: gen_table_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.gen_table_table_id_seq OWNED BY public.gen_table.table_id;


--
-- Name: global_parameters; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.global_parameters (
    id bigint NOT NULL,
    session_id bigint NOT NULL,
    imp_range text,
    size_w integer,
    size_h integer,
    material_type text,
    thickness numeric(6,3),
    product_type text,
    division_type text
);


--
-- Name: global_parameters_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.global_parameters_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: global_parameters_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.global_parameters_id_seq OWNED BY public.global_parameters.id;


--
-- Name: gold_foil_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.gold_foil_type (
    id integer NOT NULL,
    project_id integer NOT NULL,
    flat_hot_stamping public.special_char_new,
    embossed_gold_stamping public.special_char_new,
    refractive_holographic_patterned_textured_hot_stamping public.special_char_new,
    post_hot_stamping_embossing_debossing public.special_char_new
);


--
-- Name: gold_foil_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.gold_foil_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: gold_foil_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.gold_foil_type_id_seq OWNED BY public.gold_foil_type.id;


--
-- Name: hot_stamping_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_compatibility (
    id integer NOT NULL,
    paper_performance character varying(50) NOT NULL,
    product_type character varying(50) NOT NULL,
    pattern_characteristic text NOT NULL,
    hot_stamping_type character varying(100) NOT NULL,
    compatibility character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    pre_processing_step_id integer,
    product_type_id integer,
    pattern_characteristic_id integer,
    hot_stamping_type_id integer,
    post_processing_step_id integer,
    lamination_material_id integer,
    skip_wear_resistant_mapping boolean DEFAULT false NOT NULL,
    notice_ids integer[]
);


--
-- Name: TABLE hot_stamping_compatibility; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hot_stamping_compatibility IS '烫金工艺兼容性表';


--
-- Name: COLUMN hot_stamping_compatibility.paper_performance; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.paper_performance IS '烫金纸性能类型';


--
-- Name: COLUMN hot_stamping_compatibility.product_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.product_type IS '主要应用产品类型';


--
-- Name: COLUMN hot_stamping_compatibility.pattern_characteristic; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.pattern_characteristic IS '烫金图案特征描述';


--
-- Name: COLUMN hot_stamping_compatibility.hot_stamping_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.hot_stamping_type IS '烫金类型';


--
-- Name: COLUMN hot_stamping_compatibility.compatibility; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.compatibility IS '兼容性标识 V:兼容 X:不兼容';


--
-- Name: COLUMN hot_stamping_compatibility.pre_processing_step_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.pre_processing_step_id IS '前工序id';


--
-- Name: COLUMN hot_stamping_compatibility.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.product_type_id IS '产品类型id';


--
-- Name: COLUMN hot_stamping_compatibility.pattern_characteristic_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.pattern_characteristic_id IS '图案id';


--
-- Name: COLUMN hot_stamping_compatibility.hot_stamping_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.hot_stamping_type_id IS '烫金类型id';


--
-- Name: COLUMN hot_stamping_compatibility.post_processing_step_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.post_processing_step_id IS '后工序id';


--
-- Name: COLUMN hot_stamping_compatibility.lamination_material_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.lamination_material_id IS '过胶材质ID（关联lamination_material_options表，当post_processing_step_id为过胶时使用）';


--
-- Name: COLUMN hot_stamping_compatibility.skip_wear_resistant_mapping; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.skip_wear_resistant_mapping IS '在耐磨金纸映射匹配中跳过此规则（TRUE=跳过，FALSE=参与）';


--
-- Name: COLUMN hot_stamping_compatibility.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_compatibility.notice_ids IS '关联的注意事项ID数组';


--
-- Name: hot_stamping_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_compatibility_id_seq OWNED BY public.hot_stamping_compatibility.id;


--
-- Name: hot_stamping_pattern_base; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_pattern_base (
    id integer NOT NULL,
    option_name character varying(200) NOT NULL,
    pattern_type character varying(30) NOT NULL,
    line_thickness_min numeric(5,2),
    line_thickness_max numeric(5,2),
    solid_area_min numeric(5,2),
    solid_area_max numeric(5,2),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: hot_stamping_pattern_base_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_pattern_base_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_pattern_base_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_pattern_base_id_seq OWNED BY public.hot_stamping_pattern_base.id;


--
-- Name: hot_stamping_patternx_area_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_patternx_area_compatibility (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    hot_stamping_patternx_area_id integer NOT NULL,
    compatibility_status character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    notice_ids integer[]
);


--
-- Name: COLUMN hot_stamping_patternx_area_compatibility.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_patternx_area_compatibility.notice_ids IS '关联的注意事项ID数组';


--
-- Name: hot_stamping_patternx_area_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_patternx_area_options (
    id integer NOT NULL,
    option_name character varying(100) NOT NULL,
    area_category character varying(20) NOT NULL,
    area_range character varying(50) NOT NULL,
    pattern_type character varying(50) NOT NULL,
    min_size_mm integer,
    max_size_mm integer,
    description text,
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: hot_stamping_patternx_area_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_patternx_area_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_patternx_area_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_patternx_area_options_id_seq OWNED BY public.hot_stamping_patternx_area_options.id;


--
-- Name: hot_stamping_patternx_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_patternx_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_patternx_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_patternx_compatibility_id_seq OWNED BY public.hot_stamping_patternx_area_compatibility.id;


--
-- Name: hot_stamping_type_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_type_compatibility (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    hot_stamping_type_id integer NOT NULL,
    compatibility_status character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    notice_ids integer[],
    CONSTRAINT cloth_paper_compatibility_compatibility_status_check_1 CHECK ((compatibility_status = ANY (ARRAY['V'::bpchar, 'X'::bpchar, 'NA'::bpchar, '▷'::bpchar])))
);


--
-- Name: COLUMN hot_stamping_type_compatibility.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_type_compatibility.notice_ids IS '关联的注意事项ID数组';


--
-- Name: hot_stamping_type_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_type_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_type_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_type_compatibility_id_seq OWNED BY public.hot_stamping_type_compatibility.id;


--
-- Name: hot_stamping_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hot_stamping_type_options (
    id integer NOT NULL,
    stamping_type character varying(50) NOT NULL,
    position_type character varying(30),
    description text,
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    match_mode character varying(3) DEFAULT 'OR'::character varying NOT NULL
);


--
-- Name: COLUMN hot_stamping_type_options.match_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hot_stamping_type_options.match_mode IS '烫金类型匹配模式：AND=耐磨映射+常用映射都要满足，OR=任意映射满足即可';


--
-- Name: hot_stamping_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hot_stamping_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hot_stamping_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hot_stamping_type_options_id_seq OWNED BY public.hot_stamping_type_options.id;


--
-- Name: import_snapshot; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.import_snapshot (
    id bigint NOT NULL,
    snapshot_name character varying(200) NOT NULL,
    table_name character varying(100) NOT NULL,
    snapshot_table_name character varying(200) NOT NULL,
    operation_type character varying(50) NOT NULL,
    operated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    operator_id integer,
    operator_name character varying(100),
    record_count integer DEFAULT 0,
    remark character varying(500)
);


--
-- Name: TABLE import_snapshot; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.import_snapshot IS '导入快照元数据，记录每次表快照便于按次恢复';


--
-- Name: COLUMN import_snapshot.snapshot_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.import_snapshot.snapshot_name IS '快照显示名，如 表名_yyyyMMddHHmmss';


--
-- Name: COLUMN import_snapshot.table_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.import_snapshot.table_name IS '业务表名';


--
-- Name: COLUMN import_snapshot.snapshot_table_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.import_snapshot.snapshot_table_name IS '快照表名，如 product_snapshot_20250101120000';


--
-- Name: COLUMN import_snapshot.operation_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.import_snapshot.operation_type IS 'OVERWRITE_IMPORT=覆盖式导入, BATCH_UPDATE=大批量更新';


--
-- Name: COLUMN import_snapshot.record_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.import_snapshot.record_count IS '快照时该表记录数';


--
-- Name: import_snapshot_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.import_snapshot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: import_snapshot_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.import_snapshot_id_seq OWNED BY public.import_snapshot.id;


--
-- Name: interface_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.interface_type_options (
    id integer NOT NULL,
    interface_name character varying(100) NOT NULL,
    interface_code character varying(50),
    display_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: interface_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.interface_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: interface_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.interface_type_options_id_seq OWNED BY public.interface_type_options.id;


--
-- Name: knowledge_base_stats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.knowledge_base_stats (
    id integer NOT NULL,
    total_embeddings integer DEFAULT 0,
    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    embedding_model character varying(100) DEFAULT 'text-embedding-ada-002'::character varying,
    version character varying(20) DEFAULT '1.0'::character varying
);


--
-- Name: TABLE knowledge_base_stats; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.knowledge_base_stats IS '知识库统计信息表';


--
-- Name: knowledge_base_stats_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.knowledge_base_stats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: knowledge_base_stats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.knowledge_base_stats_id_seq OWNED BY public.knowledge_base_stats.id;


--
-- Name: knowledge_base_status; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.knowledge_base_status (
    id integer NOT NULL,
    status character varying(50) DEFAULT 'ready'::character varying,
    total_embeddings integer DEFAULT 0,
    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    dify_workflow_id character varying(100),
    version character varying(20) DEFAULT '1.0'::character varying
);


--
-- Name: knowledge_base_status_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.knowledge_base_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: knowledge_base_status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.knowledge_base_status_id_seq OWNED BY public.knowledge_base_status.id;


--
-- Name: knowledge_base_versions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.knowledge_base_versions (
    id integer NOT NULL,
    version character varying(20) NOT NULL,
    description text,
    total_products integer,
    total_embeddings integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_active boolean DEFAULT false
);


--
-- Name: knowledge_base_versions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.knowledge_base_versions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: knowledge_base_versions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.knowledge_base_versions_id_seq OWNED BY public.knowledge_base_versions.id;


--
-- Name: lamination_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_compatibility (
    id integer NOT NULL,
    foil_series character varying(50) NOT NULL,
    interface_type_id integer,
    post_processing_step_id integer,
    lamination_material_id integer NOT NULL,
    compatibility character(1) DEFAULT 'V'::bpchar NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    product_type character varying,
    model_number character varying,
    is_jiehuo boolean,
    notice_ids integer[],
    model_number_key text GENERATED ALWAYS AS (COALESCE(model_number, ''::character varying)) STORED,
    CONSTRAINT check_compatibility CHECK ((compatibility = ANY (ARRAY['V'::bpchar, 'X'::bpchar])))
);


--
-- Name: COLUMN lamination_compatibility.foil_series; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.foil_series IS '对应product表的name';


--
-- Name: COLUMN lamination_compatibility.interface_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.interface_type_id IS '废弃';


--
-- Name: COLUMN lamination_compatibility.post_processing_step_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.post_processing_step_id IS '对应post_processing_options的id';


--
-- Name: COLUMN lamination_compatibility.lamination_material_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.lamination_material_id IS '对应public.lamination_material_options表的id';


--
-- Name: COLUMN lamination_compatibility.product_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.product_type IS '对应product表的hot_stamping_paper_type字段';


--
-- Name: COLUMN lamination_compatibility.model_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.model_number IS '对应product表的model_number';


--
-- Name: COLUMN lamination_compatibility.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_compatibility.notice_ids IS '关联的注意事项ID数组';


--
-- Name: lamination_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_compatibility_id_seq OWNED BY public.lamination_compatibility.id;


--
-- Name: lamination_feasibility_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_feasibility_rules (
    id integer NOT NULL,
    lamination_type_id integer NOT NULL,
    substrate_id integer NOT NULL,
    product_type_id integer,
    gsm_min numeric(10,2),
    gsm_max numeric(10,2),
    thickness_max numeric(10,2),
    feasibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc character varying(500),
    notice_ids integer[],
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE lamination_feasibility_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.lamination_feasibility_rules IS '过胶可行性规则（匹配核心）';


--
-- Name: COLUMN lamination_feasibility_rules.lamination_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.lamination_type_id IS '过胶类型';


--
-- Name: COLUMN lamination_feasibility_rules.substrate_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.substrate_id IS '基材';


--
-- Name: COLUMN lamination_feasibility_rules.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.product_type_id IS '产品类型（关联product_type_options）';


--
-- Name: COLUMN lamination_feasibility_rules.gsm_min; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.gsm_min IS '克重下限';


--
-- Name: COLUMN lamination_feasibility_rules.gsm_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.gsm_max IS '克重上限';


--
-- Name: COLUMN lamination_feasibility_rules.thickness_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.thickness_max IS '膜最大厚度（µm）';


--
-- Name: COLUMN lamination_feasibility_rules.feasibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.feasibility_status IS '可行性：V=可行 X=不可行 NA=不确定 ▷=有条件';


--
-- Name: COLUMN lamination_feasibility_rules.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.condition_desc IS '条件说明';


--
-- Name: COLUMN lamination_feasibility_rules.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_feasibility_rules.notice_ids IS '关联注意事项ID';


--
-- Name: lamination_feasibility_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_feasibility_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_feasibility_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_feasibility_rules_id_seq OWNED BY public.lamination_feasibility_rules.id;


--
-- Name: lamination_material_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_material_categories (
    id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    category_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: lamination_material_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_material_categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_material_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_material_categories_id_seq OWNED BY public.lamination_material_categories.id;


--
-- Name: lamination_material_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_material_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: lamination_material_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_material_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_material_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_material_compatibility_id_seq OWNED BY public.lamination_material_compatibility.id;


--
-- Name: lamination_material_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_material_options (
    id integer NOT NULL,
    material_name character varying(100) NOT NULL,
    material_code character varying(50) NOT NULL,
    display_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    description character varying(200),
    pid integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: lamination_material_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_material_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_material_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_material_options_id_seq OWNED BY public.lamination_material_options.id;


--
-- Name: lamination_material_products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_material_products (
    id integer NOT NULL,
    material_code character varying(100) NOT NULL,
    stock_code character varying(200) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage_text text,
    material_type character varying(200) DEFAULT NULL::character varying,
    thickness_film character varying(100) DEFAULT NULL::character varying,
    thickness_glue character varying(100) DEFAULT NULL::character varying,
    size_info character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    shape character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    category character varying(200) DEFAULT NULL::character varying,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    film_thickness character varying(100) DEFAULT NULL::character varying
);


--
-- Name: lamination_material_products_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_material_products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_material_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_material_products_id_seq OWNED BY public.lamination_material_products.id;


--
-- Name: lamination_substrate_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_substrate_options (
    id integer NOT NULL,
    substrate_name character varying(100) NOT NULL,
    category character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE lamination_substrate_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.lamination_substrate_options IS '过胶基材选项';


--
-- Name: COLUMN lamination_substrate_options.substrate_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_substrate_options.substrate_name IS '基材名称';


--
-- Name: COLUMN lamination_substrate_options.category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_substrate_options.category IS '基材分类';


--
-- Name: lamination_substrate_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_substrate_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_substrate_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_substrate_options_id_seq OWNED BY public.lamination_substrate_options.id;


--
-- Name: lamination_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lamination_type_options (
    id integer NOT NULL,
    type_name character varying(100) NOT NULL,
    type_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE lamination_type_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.lamination_type_options IS '过胶类型选项';


--
-- Name: COLUMN lamination_type_options.type_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_type_options.type_name IS '过胶类型名称';


--
-- Name: COLUMN lamination_type_options.type_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lamination_type_options.type_code IS '过胶类型编码';


--
-- Name: lamination_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lamination_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: lamination_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lamination_type_options_id_seq OWNED BY public.lamination_type_options.id;


--
-- Name: leo_gp_numbers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.leo_gp_numbers (
    id integer NOT NULL,
    project_id integer NOT NULL,
    company_number character varying(255),
    gp_no character varying(255)
);


--
-- Name: leo_gp_numbers_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.leo_gp_numbers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: leo_gp_numbers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.leo_gp_numbers_id_seq OWNED BY public.leo_gp_numbers.id;


--
-- Name: material_catalog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.material_catalog (
    id integer NOT NULL,
    material_code character varying(50) NOT NULL,
    material_name character varying(200) NOT NULL,
    supplier_model character varying(200),
    test_no character varying(100),
    supplier character varying(200),
    category_type character varying(50) NOT NULL,
    category_id integer,
    design_limits jsonb,
    substrate_compat jsonb,
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    material_usage character varying(500)
);


--
-- Name: TABLE material_catalog; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.material_catalog IS '物料目录（统一物料底座，存储所有供应商级物料）';


--
-- Name: COLUMN material_catalog.material_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.material_code IS '物料编码（自动生成，用于匹配流程关联）';


--
-- Name: COLUMN material_catalog.material_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.material_name IS '物料名称';


--
-- Name: COLUMN material_catalog.supplier_model; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.supplier_model IS '供应商物料型号';


--
-- Name: COLUMN material_catalog.test_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.test_no IS '创新中心测试单号';


--
-- Name: COLUMN material_catalog.supplier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.supplier IS '供应商名称';


--
-- Name: COLUMN material_catalog.category_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.category_type IS '分类域：screen_printing_uv / cold_foil_paper / lamination_material / printing_material';


--
-- Name: COLUMN material_catalog.category_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.category_id IS '指向各域分类表的ID（多态关联）';


--
-- Name: COLUMN material_catalog.design_limits; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.design_limits IS '设计限制（纸度/网点/线条/间距/面积）';


--
-- Name: COLUMN material_catalog.substrate_compat; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_catalog.substrate_compat IS '底材兼容性（纸面/印刷油墨面/后加工涂层面）';


--
-- Name: material_catalog_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.material_catalog_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: material_catalog_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.material_catalog_id_seq OWNED BY public.material_catalog.id;


--
-- Name: material_process_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.material_process_compatibility (
    id integer NOT NULL,
    material_id integer NOT NULL,
    operation_name character varying(100) NOT NULL,
    operation_category character varying(50),
    compatibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE material_process_compatibility; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.material_process_compatibility IS '物料-工序兼容性矩阵（V/X表）';


--
-- Name: COLUMN material_process_compatibility.material_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_process_compatibility.material_id IS '物料ID';


--
-- Name: COLUMN material_process_compatibility.operation_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_process_compatibility.operation_name IS '工序名称';


--
-- Name: COLUMN material_process_compatibility.operation_category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_process_compatibility.operation_category IS '工序大类：structural / post_processing / interface';


--
-- Name: COLUMN material_process_compatibility.compatibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_process_compatibility.compatibility_status IS '兼容性：V=可行 X=不可行 NA=不适用 ▷=有条件';


--
-- Name: COLUMN material_process_compatibility.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.material_process_compatibility.condition_desc IS '条件说明';


--
-- Name: material_process_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.material_process_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: material_process_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.material_process_compatibility_id_seq OWNED BY public.material_process_compatibility.id;


--
-- Name: old_matte_lamination; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.old_matte_lamination (
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


--
-- Name: matte_lamination_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.matte_lamination_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: matte_lamination_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.matte_lamination_id_seq OWNED BY public.old_matte_lamination.id;


--
-- Name: notice_dictionary; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notice_dictionary (
    id integer NOT NULL,
    notice_code character varying(50) NOT NULL,
    title character varying(200),
    problem_description text,
    solution text,
    category character varying(50),
    priority integer DEFAULT 0,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    rich_content text
);


--
-- Name: TABLE notice_dictionary; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.notice_dictionary IS '注意事项字典表';


--
-- Name: COLUMN notice_dictionary.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.id IS '主键ID';


--
-- Name: COLUMN notice_dictionary.notice_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.notice_code IS '注意事项编码，唯一标识';


--
-- Name: COLUMN notice_dictionary.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.title IS '标题（可选）';


--
-- Name: COLUMN notice_dictionary.problem_description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.problem_description IS '问题描述（可选）';


--
-- Name: COLUMN notice_dictionary.solution; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.solution IS '解决方案（可选）';


--
-- Name: COLUMN notice_dictionary.category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.category IS '分类：印刷、过胶、烫金、前工序等';


--
-- Name: COLUMN notice_dictionary.priority; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.priority IS '优先级，数值越大越重要';


--
-- Name: COLUMN notice_dictionary.is_active; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.is_active IS '是否启用';


--
-- Name: COLUMN notice_dictionary.created_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.created_at IS '创建时间';


--
-- Name: COLUMN notice_dictionary.updated_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.updated_at IS '更新时间';


--
-- Name: COLUMN notice_dictionary.rich_content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notice_dictionary.rich_content IS '富文本内容（支持HTML表格、格式化文本等）';


--
-- Name: notice_dictionary_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notice_dictionary_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: notice_dictionary_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notice_dictionary_id_seq OWNED BY public.notice_dictionary.id;


--
-- Name: old_oil_and_uv_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.old_oil_and_uv_types (
    id integer NOT NULL,
    project_id integer NOT NULL,
    oil_6812_glossy_3301_3440_dumb public.special_char_new,
    gv_led_uv_gloss_matte_oil public.special_char_new,
    oil_based_gloss_matte_on_powder_paper public.special_char_new,
    oil_based_gloss_matte_non_powder_paper public.special_char_new
);


--
-- Name: oil_and_uv_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.oil_and_uv_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: oil_and_uv_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.oil_and_uv_types_id_seq OWNED BY public.old_oil_and_uv_types.id;


--
-- Name: old_pattern; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.old_pattern (
    id integer NOT NULL,
    project_id integer NOT NULL,
    gradient_halftone public.special_char_new,
    thin_lines_letters public.special_char_new,
    medium_small_thin_lines_letters public.special_char_new,
    medium_large_area_fine_lines_letters public.special_char_new,
    extra_large_area public.special_char_new
);


--
-- Name: paper_material_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.paper_material_categories (
    id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    category_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: paper_material_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.paper_material_categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: paper_material_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.paper_material_categories_id_seq OWNED BY public.paper_material_categories.id;


--
-- Name: pattern_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pattern_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pattern_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pattern_id_seq OWNED BY public.old_pattern.id;


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permissions (
    id integer NOT NULL,
    permission_name character varying(100) NOT NULL,
    permission_key character varying(100) NOT NULL,
    description character varying(255),
    permission_type character varying(20) DEFAULT 'BUTTON'::character varying,
    path character varying(200) DEFAULT ''::character varying,
    component character varying(255) DEFAULT NULL::character varying,
    icon character varying(100) DEFAULT NULL::character varying,
    parent_id integer,
    order_num integer DEFAULT 0,
    status integer DEFAULT 1,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.permissions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.permissions_id_seq OWNED BY public.permissions.id;


--
-- Name: post_processing_leduvglitter; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.post_processing_leduvglitter (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    product_model_number character varying(255),
    cloth_paper_type_id integer NOT NULL,
    compatibility_status character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    notice_ids integer[]
);


--
-- Name: COLUMN post_processing_leduvglitter.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_leduvglitter.notice_ids IS '关联的注意事项ID数组';


--
-- Name: post_processing_leduvglitter_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.post_processing_leduvglitter_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: post_processing_leduvglitter_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.post_processing_leduvglitter_id_seq OWNED BY public.post_processing_leduvglitter.id;


--
-- Name: post_processing_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.post_processing_options (
    id integer NOT NULL,
    step_name character varying(200) NOT NULL,
    step_code character varying(50),
    display_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: post_processing_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.post_processing_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: post_processing_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.post_processing_options_id_seq OWNED BY public.post_processing_options.id;


--
-- Name: post_processing_print; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.post_processing_print (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    product_model_number character varying(255) NOT NULL,
    color character varying(20) NOT NULL,
    cloth_paper_type_id integer NOT NULL,
    compatibility_status character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    "PK" character varying(256),
    notice_ids integer[]
);


--
-- Name: COLUMN post_processing_print.product_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print.product_name IS '产品系列(对应product表的name)';


--
-- Name: COLUMN post_processing_print.product_model_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print.product_model_number IS '产品型号(对应product表的_model_number)';


--
-- Name: COLUMN post_processing_print.color; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print.color IS '颜色(对应public.specifications的color)';


--
-- Name: COLUMN post_processing_print.cloth_paper_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print.cloth_paper_type_id IS '对应cloth_paper_types表的id';


--
-- Name: COLUMN post_processing_print.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print.notice_ids IS '关联的注意事项ID数组';


--
-- Name: post_processing_print_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.post_processing_print_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: post_processing_print_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.post_processing_print_id_seq OWNED BY public.post_processing_print.id;


--
-- Name: post_processing_print_uv; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.post_processing_print_uv (
    id integer NOT NULL,
    product_name character varying(255) NOT NULL,
    product_model_number character varying(255) NOT NULL,
    company_number character varying(255),
    compatibility_status character(1) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    paper_type character varying,
    notice_ids integer[]
);


--
-- Name: COLUMN post_processing_print_uv.company_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print_uv.company_number IS '对应leo_gp_numbers表的';


--
-- Name: COLUMN post_processing_print_uv.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.post_processing_print_uv.notice_ids IS '关联的注意事项ID数组';


--
-- Name: post_processing_print_uv_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.post_processing_print_uv_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: post_processing_print_uv_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.post_processing_print_uv_id_seq OWNED BY public.post_processing_print_uv.id;


--
-- Name: pre_processing_steps; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pre_processing_steps (
    id integer NOT NULL,
    step_name character varying(100) NOT NULL,
    series_name character varying(100) NOT NULL,
    product_id integer,
    step_order integer DEFAULT 1,
    is_active boolean DEFAULT true,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying,
    cloth_paper_id integer,
    paper_type character varying,
    step_id integer,
    notice_ids integer[]
);


--
-- Name: COLUMN pre_processing_steps.step_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pre_processing_steps.step_name IS '废弃';


--
-- Name: COLUMN pre_processing_steps.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pre_processing_steps.description IS '废弃';


--
-- Name: COLUMN pre_processing_steps.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pre_processing_steps.notice_ids IS '关联的注意事项ID数组';


--
-- Name: pre_processing_steps_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pre_processing_steps_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pre_processing_steps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pre_processing_steps_id_seq OWNED BY public.pre_processing_steps.id;


--
-- Name: pre_processing_steps_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pre_processing_steps_options (
    id integer NOT NULL,
    pre_processing_steps_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    steps character varying,
    process character varying
);


--
-- Name: pre_processing_steps_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pre_processing_steps_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pre_processing_steps_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pre_processing_steps_options_id_seq OWNED BY public.pre_processing_steps_options.id;


--
-- Name: pricing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pricing (
    id integer NOT NULL,
    project_id integer NOT NULL,
    price numeric
);


--
-- Name: pricing_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pricing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pricing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pricing_id_seq OWNED BY public.pricing.id;


--
-- Name: pricing_snapshot_20260202180228_1770026548553; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pricing_snapshot_20260202180228_1770026548553 (
    id integer,
    project_id integer,
    price numeric
);


--
-- Name: printing_feasibility_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.printing_feasibility_rules (
    id integer NOT NULL,
    process_id integer NOT NULL,
    substrate_id integer NOT NULL,
    product_type_id integer,
    gsm_min numeric(10,2),
    gsm_max numeric(10,2),
    color_count character varying(50),
    ink_type character varying(50),
    width_max numeric(10,2),
    height_max numeric(10,2),
    feasibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc character varying(500),
    notice_ids integer[],
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE printing_feasibility_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.printing_feasibility_rules IS '印刷可行性规则（匹配核心）';


--
-- Name: COLUMN printing_feasibility_rules.process_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.process_id IS '印刷工艺';


--
-- Name: COLUMN printing_feasibility_rules.substrate_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.substrate_id IS '承印材料';


--
-- Name: COLUMN printing_feasibility_rules.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.product_type_id IS '产品类型（关联product_type_options）';


--
-- Name: COLUMN printing_feasibility_rules.gsm_min; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.gsm_min IS '克重下限';


--
-- Name: COLUMN printing_feasibility_rules.gsm_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.gsm_max IS '克重上限';


--
-- Name: COLUMN printing_feasibility_rules.color_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.color_count IS '色数';


--
-- Name: COLUMN printing_feasibility_rules.ink_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.ink_type IS '油墨类型';


--
-- Name: COLUMN printing_feasibility_rules.width_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.width_max IS '最大宽度mm（预留）';


--
-- Name: COLUMN printing_feasibility_rules.height_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.height_max IS '最大高度mm（预留）';


--
-- Name: COLUMN printing_feasibility_rules.feasibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.feasibility_status IS '可行性状态：V=可行 X=不可行 NA=不确定 ▷=有条件';


--
-- Name: COLUMN printing_feasibility_rules.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.condition_desc IS '条件说明';


--
-- Name: COLUMN printing_feasibility_rules.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_feasibility_rules.notice_ids IS '关联注意事项ID';


--
-- Name: printing_feasibility_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.printing_feasibility_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: printing_feasibility_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.printing_feasibility_rules_id_seq OWNED BY public.printing_feasibility_rules.id;


--
-- Name: printing_material_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.printing_material_categories (
    id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    category_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE printing_material_categories; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.printing_material_categories IS '印刷物料类别（UV油/水油等）';


--
-- Name: COLUMN printing_material_categories.category_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_material_categories.category_name IS '类别名称';


--
-- Name: COLUMN printing_material_categories.category_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_material_categories.category_code IS '类别编码';


--
-- Name: printing_material_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.printing_material_categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: printing_material_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.printing_material_categories_id_seq OWNED BY public.printing_material_categories.id;


--
-- Name: printing_process_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.printing_process_options (
    id integer NOT NULL,
    process_name character varying(100) NOT NULL,
    process_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE printing_process_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.printing_process_options IS '印刷工艺选项';


--
-- Name: COLUMN printing_process_options.process_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_process_options.process_name IS '工艺名称';


--
-- Name: COLUMN printing_process_options.process_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_process_options.process_code IS '工艺编码';


--
-- Name: printing_process_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.printing_process_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: printing_process_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.printing_process_options_id_seq OWNED BY public.printing_process_options.id;


--
-- Name: printing_substrate_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.printing_substrate_options (
    id integer NOT NULL,
    substrate_name character varying(100) NOT NULL,
    category character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE printing_substrate_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.printing_substrate_options IS '承印材料选项';


--
-- Name: COLUMN printing_substrate_options.substrate_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_substrate_options.substrate_name IS '材料名称';


--
-- Name: COLUMN printing_substrate_options.category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.printing_substrate_options.category IS '材料分类';


--
-- Name: printing_substrate_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.printing_substrate_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: printing_substrate_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.printing_substrate_options_id_seq OWNED BY public.printing_substrate_options.id;


--
-- Name: processing_after_ironing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.processing_after_ironing (
    id integer NOT NULL,
    project_id integer NOT NULL,
    lamination public.special_char_new,
    uv_printing public.special_char_new,
    silk_screen_led_uv_sparkle_powder public.special_char_new,
    printing public.special_char_new
);


--
-- Name: processing_after_ironing_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.processing_after_ironing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: processing_after_ironing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.processing_after_ironing_id_seq OWNED BY public.processing_after_ironing.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    model_number character varying(255) NOT NULL,
    material_number character varying(255) NOT NULL,
    hot_stamping_paper_type character varying(255),
    descirption text,
    fengdu character varying,
    paizi character varying,
    gaishu text,
    danwei character varying,
    is_changyong character varying,
    is_jiehuo boolean,
    status character varying(10) DEFAULT '可用'::character varying,
    usage_count bigint DEFAULT 0 NOT NULL,
    update_by integer,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_product_status CHECK (((status)::text = ANY ((ARRAY['可用'::character varying, '廢棄'::character varying, '有效'::character varying, '無效'::character varying, 'V'::character varying, 'X'::character varying])::text[])))
);


--
-- Name: COLUMN products.is_jiehuo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.products.is_jiehuo IS '是否街货';


--
-- Name: COLUMN products.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.products.status IS '物料状态标记：可用、废弃';


--
-- Name: COLUMN products.usage_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.products.usage_count IS '物料被筛选出来的次数，用于判断是否常用';


--
-- Name: specifications; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.specifications (
    id integer NOT NULL,
    project_id integer NOT NULL,
    color character varying(255),
    size character varying(255),
    tightness character varying(255),
    temperature_range character varying(255),
    performance text,
    color_num character varying
);


--
-- Name: product_detailed_view; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.product_detailed_view AS
 SELECT p.id,
    p.name,
    p.model_number,
    p.material_number,
    p.hot_stamping_paper_type,
    s.color,
    s.size,
    s.tightness,
    s.temperature_range,
    s.performance,
    lgn.company_number,
    lgn.gp_no,
    ouvt.oil_6812_glossy_3301_3440_dumb,
    ouvt.gv_led_uv_gloss_matte_oil,
    ouvt.oil_based_gloss_matte_on_powder_paper,
    ouvt.oil_based_gloss_matte_non_powder_paper,
    ml.standard_furonghui_22d,
    ml.pre_coated_hy1206_65,
    ml.high_tack_pre_coated_hy40,
    ml.pre_coated_economical_high_wear_resistant_yt008a,
    ml.pre_coated_high_wear_resistant_tn008,
    ml.pre_coated_standard_wear_resistant_kvmb_f,
    ml.soft_touch_matte_laminate_6015a,
    gft.flat_hot_stamping,
    gft.embossed_gold_stamping,
    gft.refractive_holographic_patterned_textured_hot_stamping,
    gft.post_hot_stamping_embossing_debossing,
    pt.gradient_halftone,
    pt.thin_lines_letters,
    pt.medium_small_thin_lines_letters,
    pt.medium_large_area_fine_lines_letters,
    pt.extra_large_area,
    pai.lamination,
    pai.uv_printing,
    pai.silk_screen_led_uv_sparkle_powder,
    pai.printing,
    pr.price
   FROM ((((((((public.products p
     LEFT JOIN public.specifications s ON ((p.id = s.project_id)))
     LEFT JOIN public.leo_gp_numbers lgn ON ((p.id = lgn.project_id)))
     LEFT JOIN public.old_oil_and_uv_types ouvt ON ((p.id = ouvt.project_id)))
     LEFT JOIN public.old_matte_lamination ml ON ((p.id = ml.project_id)))
     LEFT JOIN public.gold_foil_type gft ON ((p.id = gft.project_id)))
     LEFT JOIN public.old_pattern pt ON ((p.id = pt.project_id)))
     LEFT JOIN public.processing_after_ironing pai ON ((p.id = pai.project_id)))
     LEFT JOIN public.pricing pr ON ((p.id = pr.project_id)));


--
-- Name: VIEW product_detailed_view; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON VIEW public.product_detailed_view IS '产品详情视图，用于知识库构建';


--
-- Name: product_knowledge_embeddings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_knowledge_embeddings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_type_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_type_options (
    id integer NOT NULL,
    product_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: product_type_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_type_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_type_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_type_options_id_seq OWNED BY public.product_type_options.id;


--
-- Name: product_type_sort_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_type_sort_config (
    id integer NOT NULL,
    product_type_id integer NOT NULL,
    enable_wear_resistant_priority boolean DEFAULT false,
    wear_resistant_paper_types text,
    description text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE product_type_sort_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.product_type_sort_config IS '产品类型排序配置表：动态配置哪些产品类型需要应用特殊排序规则';


--
-- Name: COLUMN product_type_sort_config.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.product_type_sort_config.product_type_id IS '产品类型ID（关联product_type_options表）';


--
-- Name: COLUMN product_type_sort_config.enable_wear_resistant_priority; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.product_type_sort_config.enable_wear_resistant_priority IS '是否启用耐磨金纸优先排序：true=如果公司有耐磨金纸，优先排序；false=按现时规则';


--
-- Name: COLUMN product_type_sort_config.wear_resistant_paper_types; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.product_type_sort_config.wear_resistant_paper_types IS '耐磨金纸类型列表，逗号分隔，如：普通耐磨,高耐磨';


--
-- Name: COLUMN product_type_sort_config.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.product_type_sort_config.description IS '配置描述';


--
-- Name: COLUMN product_type_sort_config.is_active; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.product_type_sort_config.is_active IS '是否激活';


--
-- Name: product_type_sort_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_type_sort_config_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_type_sort_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_type_sort_config_id_seq OWNED BY public.product_type_sort_config.id;


--
-- Name: production_capacity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.production_capacity (
    id integer NOT NULL,
    resource_group_code character varying(40) NOT NULL,
    difficulty_level character varying(50) DEFAULT '易'::character varying NOT NULL,
    capacity_per_hour numeric(10,2),
    capacity_per_shift numeric(10,2),
    prep_time_electric numeric(10,2),
    prep_time_labor numeric(10,2),
    source_file character varying(300),
    review_status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    review_notes text,
    reviewed_by character varying(100),
    reviewed_at timestamp without time zone
);


--
-- Name: TABLE production_capacity; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.production_capacity IS 'Production: 产能数据';


--
-- Name: COLUMN production_capacity.review_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_capacity.review_status IS '审核状态: pending=待审核, approved=已通过, rejected=已驳回, needs_revision=需修改';


--
-- Name: COLUMN production_capacity.review_notes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_capacity.review_notes IS '审核意见';


--
-- Name: COLUMN production_capacity.reviewed_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_capacity.reviewed_by IS '审核人';


--
-- Name: COLUMN production_capacity.reviewed_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_capacity.reviewed_at IS '审核时间';


--
-- Name: production_capacity_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.production_capacity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: production_capacity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.production_capacity_id_seq OWNED BY public.production_capacity.id;


--
-- Name: production_resource_groups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.production_resource_groups (
    code character varying(40) NOT NULL,
    name character varying(300) NOT NULL,
    process_category_code character varying(50),
    difficulty character varying(50),
    notes text,
    source_files text,
    created_at timestamp without time zone DEFAULT now(),
    review_status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    review_notes text,
    reviewed_by character varying(100),
    reviewed_at timestamp without time zone
);


--
-- Name: TABLE production_resource_groups; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.production_resource_groups IS 'Production: 去重资源组';


--
-- Name: COLUMN production_resource_groups.review_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_resource_groups.review_status IS '审核状态: pending=待审核, approved=已通过, rejected=已驳回, needs_revision=需修改';


--
-- Name: COLUMN production_resource_groups.review_notes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_resource_groups.review_notes IS '审核意见';


--
-- Name: COLUMN production_resource_groups.reviewed_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_resource_groups.reviewed_by IS '审核人';


--
-- Name: COLUMN production_resource_groups.reviewed_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_resource_groups.reviewed_at IS '审核时间';


--
-- Name: production_resources; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.production_resources (
    id integer NOT NULL,
    code character varying(100),
    name character varying(300),
    resource_group_code character varying(40),
    source_file character varying(300),
    review_status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    review_notes text,
    reviewed_by character varying(100),
    reviewed_at timestamp without time zone
);


--
-- Name: TABLE production_resources; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.production_resources IS 'Production: 设备资源';


--
-- Name: COLUMN production_resources.review_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_resources.review_status IS '审核状态: pending=待审核, approved=已通过, rejected=已驳回, needs_revision=需修改';


--
-- Name: production_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.production_resources_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: production_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.production_resources_id_seq OWNED BY public.production_resources.id;


--
-- Name: production_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.production_rules (
    id integer NOT NULL,
    resource_group_code character varying(40) NOT NULL,
    rule_type character varying(50) DEFAULT 'selection'::character varying NOT NULL,
    rule_content text NOT NULL,
    difficulty character varying(50),
    source_file character varying(300),
    review_status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    review_notes text,
    reviewed_by character varying(100),
    reviewed_at timestamp without time zone
);


--
-- Name: TABLE production_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.production_rules IS 'Production: 去重规则';


--
-- Name: COLUMN production_rules.review_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_rules.review_status IS '审核状态: pending=待审核, approved=已通过, rejected=已驳回, needs_revision=需修改';


--
-- Name: COLUMN production_rules.review_notes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_rules.review_notes IS '审核意见';


--
-- Name: COLUMN production_rules.reviewed_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_rules.reviewed_by IS '审核人';


--
-- Name: COLUMN production_rules.reviewed_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.production_rules.reviewed_at IS '审核时间';


--
-- Name: production_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.production_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: production_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.production_rules_id_seq OWNED BY public.production_rules.id;


--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: products_snapshot_20260202180228_1770026548267; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products_snapshot_20260202180228_1770026548267 (
    id integer,
    name character varying(255),
    model_number character varying(255),
    material_number character varying(255),
    hot_stamping_paper_type character varying(255),
    descirption text,
    fengdu character varying,
    paizi character varying,
    gaishu text,
    danwei character varying,
    is_changyong character varying,
    is_jiehuo boolean,
    status character varying(10),
    usage_count bigint
);


--
-- Name: reference_coating_surface; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reference_coating_surface (
    id integer NOT NULL,
    category character varying(50) NOT NULL,
    sub_category character varying(50) DEFAULT NULL::character varying,
    detail_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0
);


--
-- Name: TABLE reference_coating_surface; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.reference_coating_surface IS '后加工涂层面字典';


--
-- Name: reference_coating_surface_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reference_coating_surface_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_coating_surface_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reference_coating_surface_id_seq OWNED BY public.reference_coating_surface.id;


--
-- Name: reference_ink_surface; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reference_ink_surface (
    id integer NOT NULL,
    category character varying(50) NOT NULL,
    sub_category character varying(50) DEFAULT NULL::character varying,
    detail_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE reference_ink_surface; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.reference_ink_surface IS '印刷油墨面字典';


--
-- Name: reference_ink_surface_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reference_ink_surface_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_ink_surface_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reference_ink_surface_id_seq OWNED BY public.reference_ink_surface.id;


--
-- Name: reference_paper_surface; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reference_paper_surface (
    id integer NOT NULL,
    category character varying(50) NOT NULL,
    sub_category character varying(50) DEFAULT NULL::character varying,
    detail_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE reference_paper_surface; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.reference_paper_surface IS '纸张面字典';


--
-- Name: reference_paper_surface_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reference_paper_surface_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_paper_surface_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reference_paper_surface_id_seq OWNED BY public.reference_paper_surface.id;


--
-- Name: reference_product_family; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reference_product_family (
    id integer NOT NULL,
    category character varying(50) NOT NULL,
    sub_category character varying(50) DEFAULT NULL::character varying,
    detail_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE reference_product_family; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.reference_product_family IS '产品家族字典';


--
-- Name: reference_product_family_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reference_product_family_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_product_family_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reference_product_family_id_seq OWNED BY public.reference_product_family.id;


--
-- Name: reference_writing_function; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.reference_writing_function (
    id integer NOT NULL,
    category character varying(50) NOT NULL,
    sub_category character varying(50) DEFAULT NULL::character varying,
    detail_name character varying(100) NOT NULL,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE reference_writing_function; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.reference_writing_function IS '表面书写功能字典';


--
-- Name: reference_writing_function_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reference_writing_function_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reference_writing_function_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reference_writing_function_id_seq OWNED BY public.reference_writing_function.id;


--
-- Name: rg_resource_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_group (
    id integer NOT NULL,
    resource_group_code character varying(50) NOT NULL,
    name character varying(200) NOT NULL,
    work_center_code character varying(20) NOT NULL,
    work_center_name character varying(100),
    family character varying(50),
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_resource_group; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_group IS '资源组主表';


--
-- Name: COLUMN rg_resource_group.resource_group_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group.resource_group_code IS '资源组编码';


--
-- Name: COLUMN rg_resource_group.work_center_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group.work_center_code IS '工作中心编码';


--
-- Name: COLUMN rg_resource_group.family; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group.family IS '家族分类';


--
-- Name: rg_resource_group_capacity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_group_capacity (
    id integer NOT NULL,
    resource_group_id integer NOT NULL,
    capacity_mode character varying(50) NOT NULL,
    shift_name character varying(50),
    work_hours_per_shift numeric(10,2),
    shifts_per_day numeric(10,2),
    capacity_sheet_per_hour numeric(15,2),
    capacity_sqm_per_hour numeric(15,2),
    capacity_sheet_per_day numeric(15,2),
    capacity_sqm_per_day numeric(15,2),
    utilization_rate numeric(5,2),
    effective_from date,
    effective_to date,
    remark text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_resource_group_capacity; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_group_capacity IS '资源组产能参数表';


--
-- Name: COLUMN rg_resource_group_capacity.capacity_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_capacity.capacity_mode IS '产能模式，如 STANDARD/OT 等';


--
-- Name: COLUMN rg_resource_group_capacity.capacity_sheet_per_hour; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_capacity.capacity_sheet_per_hour IS '标准产能(张/小时)';


--
-- Name: COLUMN rg_resource_group_capacity.capacity_sheet_per_day; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_capacity.capacity_sheet_per_day IS '标准产能(张/天)';


--
-- Name: rg_resource_group_capacity_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_group_capacity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_group_capacity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_group_capacity_id_seq OWNED BY public.rg_resource_group_capacity.id;


--
-- Name: rg_resource_group_detail; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_group_detail (
    id integer NOT NULL,
    resource_group_id integer NOT NULL,
    max_width_mm numeric(10,2),
    max_height_mm numeric(10,2),
    min_width_mm numeric(10,2),
    min_height_mm numeric(10,2),
    min_thickness_mm numeric(10,3),
    max_thickness_mm numeric(10,3),
    min_gsm numeric(10,2),
    max_gsm numeric(10,2),
    min_speed_sheet_per_hour numeric(10,2),
    max_speed_sheet_per_hour numeric(10,2),
    design_speed_sheet_per_hour numeric(10,2),
    setup_time_min numeric(10,2),
    changeover_time_min numeric(10,2),
    machine_model character varying(100),
    manufacturer character varying(100),
    year_of_make character varying(10),
    capability_remark text,
    extra_params jsonb,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_resource_group_detail; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_group_detail IS '资源组技术参数详情表';


--
-- Name: COLUMN rg_resource_group_detail.max_width_mm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.max_width_mm IS '最大生产宽度(mm)';


--
-- Name: COLUMN rg_resource_group_detail.max_height_mm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.max_height_mm IS '最大生产高度(mm)';


--
-- Name: COLUMN rg_resource_group_detail.min_thickness_mm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.min_thickness_mm IS '最小纸张厚度(mm)';


--
-- Name: COLUMN rg_resource_group_detail.max_thickness_mm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.max_thickness_mm IS '最大纸张厚度(mm)';


--
-- Name: COLUMN rg_resource_group_detail.min_gsm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.min_gsm IS '最小克重(g/m2)';


--
-- Name: COLUMN rg_resource_group_detail.max_gsm; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.max_gsm IS '最大克重(g/m2)';


--
-- Name: COLUMN rg_resource_group_detail.max_speed_sheet_per_hour; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_detail.max_speed_sheet_per_hour IS '最高生产速度(张/小时)';


--
-- Name: rg_resource_group_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_group_detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_group_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_group_detail_id_seq OWNED BY public.rg_resource_group_detail.id;


--
-- Name: rg_resource_group_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_group_id_seq OWNED BY public.rg_resource_group.id;


--
-- Name: rg_resource_group_task_map; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_group_task_map (
    id integer NOT NULL,
    resource_group_id integer NOT NULL,
    task_code character varying(20) NOT NULL,
    support_type character varying(20) DEFAULT 'YES'::character varying,
    remark text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_resource_group_task_map; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_group_task_map IS '资源组-任务映射表';


--
-- Name: COLUMN rg_resource_group_task_map.support_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_group_task_map.support_type IS '支持类型: YES=支持, CONDITIONAL=有条件支持';


--
-- Name: rg_resource_group_task_map_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_group_task_map_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_group_task_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_group_task_map_id_seq OWNED BY public.rg_resource_group_task_map.id;


--
-- Name: rg_resource_rule_condition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_rule_condition (
    id integer NOT NULL,
    rule_header_id integer NOT NULL,
    condition_group character varying(50),
    param_code character varying(50),
    param_name character varying(100),
    operator character varying(20),
    value_min numeric(15,2),
    value_max numeric(15,2),
    value_text text,
    rule_type character varying(30) NOT NULL,
    is_blocking boolean DEFAULT false,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_required boolean DEFAULT false
);


--
-- Name: TABLE rg_resource_rule_condition; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_rule_condition IS '规则条件表';


--
-- Name: COLUMN rg_resource_rule_condition.param_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.param_code IS '参数编码';


--
-- Name: COLUMN rg_resource_rule_condition.value_min; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.value_min IS '最小值，NULL表示无下限';


--
-- Name: COLUMN rg_resource_rule_condition.value_max; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.value_max IS '最大值，NULL表示无上限';


--
-- Name: COLUMN rg_resource_rule_condition.value_text; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.value_text IS '文本规则原文';


--
-- Name: COLUMN rg_resource_rule_condition.is_blocking; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.is_blocking IS '是否为阻塞规则（暂不上机原因）';


--
-- Name: COLUMN rg_resource_rule_condition.is_required; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_condition.is_required IS '是否为硬性规则：TRUE=必须满足（不满足就FAIL），FALSE=可选规则（满足就PASS，不满足但不违反阻塞规则时不影响判断）';


--
-- Name: rg_resource_rule_condition_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_rule_condition_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_rule_condition_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_rule_condition_id_seq OWNED BY public.rg_resource_rule_condition.id;


--
-- Name: rg_resource_rule_header; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_resource_rule_header (
    id integer NOT NULL,
    resource_group_id integer NOT NULL,
    rule_type character varying(30) NOT NULL,
    rule_name character varying(200),
    description text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_resource_rule_header; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_resource_rule_header IS '规则头表';


--
-- Name: COLUMN rg_resource_rule_header.rule_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_resource_rule_header.rule_type IS '规则类型: STRUCT_RULES=结构化规则, TEXT_RULES=文本规则';


--
-- Name: rg_resource_rule_header_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_resource_rule_header_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_resource_rule_header_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_resource_rule_header_id_seq OWNED BY public.rg_resource_rule_header.id;


--
-- Name: rg_task_definition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_task_definition (
    id integer NOT NULL,
    task_code character varying(20) NOT NULL,
    task_name character varying(100) NOT NULL,
    description text,
    sort_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_task_definition; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_task_definition IS '任务定义表';


--
-- Name: COLUMN rg_task_definition.task_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_task_definition.task_code IS '任务编码';


--
-- Name: COLUMN rg_task_definition.task_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_task_definition.task_name IS '任务名称';


--
-- Name: rg_task_definition_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_task_definition_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_task_definition_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_task_definition_id_seq OWNED BY public.rg_task_definition.id;


--
-- Name: rg_work_center; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rg_work_center (
    id integer NOT NULL,
    work_center_code character varying(20) NOT NULL,
    work_center_name character varying(100) NOT NULL,
    department_name character varying(100),
    description text,
    remark text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE rg_work_center; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.rg_work_center IS '工作中心主表';


--
-- Name: COLUMN rg_work_center.work_center_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_work_center.work_center_code IS '工作中心编码（如 WC08）';


--
-- Name: COLUMN rg_work_center.work_center_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.rg_work_center.work_center_name IS '工作中心名称（如 啤机）';


--
-- Name: rg_work_center_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rg_work_center_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rg_work_center_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rg_work_center_id_seq OWNED BY public.rg_work_center.id;


--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_permissions (
    role_id integer NOT NULL,
    permission_id integer NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    role_name character varying(50) NOT NULL,
    role_key character varying(50) NOT NULL,
    description character varying(255),
    status integer DEFAULT 1,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: rule_evaluation_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rule_evaluation_log (
    id integer NOT NULL,
    execution_id integer NOT NULL,
    resource_group_code character varying(100),
    task_code character varying(100),
    rule_applied text,
    result character varying(50),
    created_at timestamp without time zone DEFAULT now()
);


--
-- Name: rule_evaluation_log_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rule_evaluation_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rule_evaluation_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rule_evaluation_log_id_seq OWNED BY public.rule_evaluation_log.id;


--
-- Name: screen_printing_uv_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.screen_printing_uv_categories (
    id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    category_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE screen_printing_uv_categories; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.screen_printing_uv_categories IS '丝印UV类别';


--
-- Name: COLUMN screen_printing_uv_categories.category_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.screen_printing_uv_categories.category_name IS '类别名称';


--
-- Name: COLUMN screen_printing_uv_categories.category_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.screen_printing_uv_categories.category_code IS '类别编码';


--
-- Name: screen_printing_uv_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.screen_printing_uv_categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: screen_printing_uv_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.screen_printing_uv_categories_id_seq OWNED BY public.screen_printing_uv_categories.id;


--
-- Name: series_priority_rule; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.series_priority_rule (
    id integer NOT NULL,
    rule_code character varying(50) NOT NULL,
    rule_name character varying(100) NOT NULL,
    sort_order integer DEFAULT 0,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE series_priority_rule; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.series_priority_rule IS '选用界面系列优先级规则';


--
-- Name: COLUMN series_priority_rule.rule_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.series_priority_rule.rule_code IS '规则编码，如 pearl_gold_powder';


--
-- Name: COLUMN series_priority_rule.rule_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.series_priority_rule.rule_name IS '规则名称，后台显示';


--
-- Name: series_priority_rule_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.series_priority_rule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: series_priority_rule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.series_priority_rule_id_seq OWNED BY public.series_priority_rule.id;


--
-- Name: series_priority_rule_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.series_priority_rule_item (
    id integer NOT NULL,
    rule_id integer NOT NULL,
    priority_order integer NOT NULL,
    series_names character varying(500) DEFAULT ''::character varying NOT NULL,
    is_price_fallback boolean DEFAULT false,
    remark character varying(200),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE series_priority_rule_item; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.series_priority_rule_item IS '规则档位：priority_order=1 对应 series_names 如 SK，2 对应 L817，最后一档 is_price_fallback=true';


--
-- Name: COLUMN series_priority_rule_item.priority_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.series_priority_rule_item.priority_order IS '档位顺序：1=第一档，2=第二档';


--
-- Name: COLUMN series_priority_rule_item.series_names; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.series_priority_rule_item.series_names IS '该档系列名，逗号分隔，如 SK 或 L817,L817/GB';


--
-- Name: COLUMN series_priority_rule_item.is_price_fallback; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.series_priority_rule_item.is_price_fallback IS 'true=该档为「其余按价格」';


--
-- Name: series_priority_rule_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.series_priority_rule_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: series_priority_rule_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.series_priority_rule_item_id_seq OWNED BY public.series_priority_rule_item.id;


--
-- Name: silicone_coarse_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_coarse_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_coarse_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_coarse_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_coarse_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_coarse_uv_compatibility_id_seq OWNED BY public.silicone_coarse_uv_compatibility.id;


--
-- Name: silicone_coarse_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_coarse_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_coarse_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_coarse_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_coarse_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_coarse_uv_product_id_seq OWNED BY public.silicone_coarse_uv_product.id;


--
-- Name: silicone_glittering_star_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_glittering_star_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_glittering_star_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_glittering_star_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_glittering_star_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_glittering_star_compatibility_id_seq OWNED BY public.silicone_glittering_star_compatibility.id;


--
-- Name: silicone_glittering_star_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_glittering_star_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_glittering_star_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_glittering_star_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_glittering_star_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_glittering_star_product_id_seq OWNED BY public.silicone_glittering_star_product.id;


--
-- Name: silicone_glow_ink_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_glow_ink_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_glow_ink_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_glow_ink_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_glow_ink_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_glow_ink_compatibility_id_seq OWNED BY public.silicone_glow_ink_compatibility.id;


--
-- Name: silicone_glow_ink_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_glow_ink_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_glow_ink_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_glow_ink_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_glow_ink_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_glow_ink_product_id_seq OWNED BY public.silicone_glow_ink_product.id;


--
-- Name: silicone_led_uv_spray_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_led_uv_spray_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_led_uv_spray_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_led_uv_spray_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_led_uv_spray_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_led_uv_spray_compatibility_id_seq OWNED BY public.silicone_led_uv_spray_compatibility.id;


--
-- Name: silicone_led_uv_spray_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_led_uv_spray_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_led_uv_spray_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_led_uv_spray_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_led_uv_spray_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_led_uv_spray_product_id_seq OWNED BY public.silicone_led_uv_spray_product.id;


--
-- Name: silicone_mica_pearl_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_mica_pearl_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_mica_pearl_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_mica_pearl_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_mica_pearl_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_mica_pearl_compatibility_id_seq OWNED BY public.silicone_mica_pearl_compatibility.id;


--
-- Name: silicone_mica_pearl_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_mica_pearl_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_mica_pearl_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_mica_pearl_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_mica_pearl_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_mica_pearl_product_id_seq OWNED BY public.silicone_mica_pearl_product.id;


--
-- Name: silicone_orange_peel_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_orange_peel_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_orange_peel_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_orange_peel_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_orange_peel_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_orange_peel_uv_compatibility_id_seq OWNED BY public.silicone_orange_peel_uv_compatibility.id;


--
-- Name: silicone_orange_peel_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_orange_peel_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_orange_peel_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_orange_peel_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_orange_peel_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_orange_peel_uv_product_id_seq OWNED BY public.silicone_orange_peel_uv_product.id;


--
-- Name: silicone_sandblast_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_sandblast_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_sandblast_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_sandblast_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_sandblast_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_sandblast_uv_compatibility_id_seq OWNED BY public.silicone_sandblast_uv_compatibility.id;


--
-- Name: silicone_sandblast_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_sandblast_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_sandblast_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_sandblast_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_sandblast_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_sandblast_uv_product_id_seq OWNED BY public.silicone_sandblast_uv_product.id;


--
-- Name: silicone_screen_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_screen_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_screen_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_screen_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_screen_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_screen_uv_compatibility_id_seq OWNED BY public.silicone_screen_uv_compatibility.id;


--
-- Name: silicone_screen_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_screen_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_screen_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_screen_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_screen_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_screen_uv_product_id_seq OWNED BY public.silicone_screen_uv_product.id;


--
-- Name: silicone_water_base_transparent_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_water_base_transparent_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_water_base_transparent_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_water_base_transparent_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_water_base_transparent_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_water_base_transparent_compatibility_id_seq OWNED BY public.silicone_water_base_transparent_compatibility.id;


--
-- Name: silicone_water_base_transparent_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_water_base_transparent_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_water_base_transparent_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_water_base_transparent_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_water_base_transparent_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_water_base_transparent_product_id_seq OWNED BY public.silicone_water_base_transparent_product.id;


--
-- Name: silicone_watercolor_ink_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_watercolor_ink_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_watercolor_ink_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_watercolor_ink_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_watercolor_ink_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_watercolor_ink_compatibility_id_seq OWNED BY public.silicone_watercolor_ink_compatibility.id;


--
-- Name: silicone_watercolor_ink_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_watercolor_ink_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_watercolor_ink_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_watercolor_ink_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_watercolor_ink_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_watercolor_ink_product_id_seq OWNED BY public.silicone_watercolor_ink_product.id;


--
-- Name: silicone_white_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_white_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_white_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_white_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_white_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_white_uv_compatibility_id_seq OWNED BY public.silicone_white_uv_compatibility.id;


--
-- Name: silicone_white_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_white_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_white_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_white_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_white_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_white_uv_product_id_seq OWNED BY public.silicone_white_uv_product.id;


--
-- Name: silicone_wrinkle_uv_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_wrinkle_uv_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_wrinkle_uv_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_wrinkle_uv_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_wrinkle_uv_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_wrinkle_uv_compatibility_id_seq OWNED BY public.silicone_wrinkle_uv_compatibility.id;


--
-- Name: silicone_wrinkle_uv_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.silicone_wrinkle_uv_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: silicone_wrinkle_uv_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.silicone_wrinkle_uv_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: silicone_wrinkle_uv_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.silicone_wrinkle_uv_product_id_seq OWNED BY public.silicone_wrinkle_uv_product.id;


--
-- Name: specifications_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.specifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: specifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.specifications_id_seq OWNED BY public.specifications.id;


--
-- Name: staging_capacity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.staging_capacity (
    id integer NOT NULL,
    source_file character varying(200) NOT NULL,
    resource_group_code character varying(40) NOT NULL,
    difficulty_level character varying(20) DEFAULT '易'::character varying NOT NULL,
    capacity_per_hour numeric(10,2),
    capacity_per_shift numeric(10,2),
    prep_time_electric numeric(10,2),
    prep_time_labor numeric(10,2),
    imported_at timestamp without time zone DEFAULT now()
);


--
-- Name: TABLE staging_capacity; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.staging_capacity IS 'Staging: 产能数据';


--
-- Name: staging_capacity_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.staging_capacity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: staging_capacity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.staging_capacity_id_seq OWNED BY public.staging_capacity.id;


--
-- Name: staging_process_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.staging_process_categories (
    code character varying(20) NOT NULL,
    name character varying(200) NOT NULL,
    description text
);


--
-- Name: TABLE staging_process_categories; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.staging_process_categories IS 'Staging: 工序大类';


--
-- Name: staging_resource_groups; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.staging_resource_groups (
    id integer NOT NULL,
    source_type character varying(20) NOT NULL,
    source_file character varying(300) NOT NULL,
    code character varying(40) NOT NULL,
    name character varying(300) NOT NULL,
    process_category_code character varying(50),
    difficulty character varying(50),
    notes text,
    imported_at timestamp without time zone DEFAULT now()
);


--
-- Name: TABLE staging_resource_groups; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.staging_resource_groups IS 'Staging: 资源组（逐来源）';


--
-- Name: staging_resource_groups_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.staging_resource_groups_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: staging_resource_groups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.staging_resource_groups_id_seq OWNED BY public.staging_resource_groups.id;


--
-- Name: staging_resources; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.staging_resources (
    id integer NOT NULL,
    source_file character varying(300) NOT NULL,
    resource_group_code character varying(40) NOT NULL,
    code character varying(100),
    name character varying(300),
    imported_at timestamp without time zone DEFAULT now()
);


--
-- Name: TABLE staging_resources; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.staging_resources IS 'Staging: 设备资源';


--
-- Name: staging_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.staging_resources_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: staging_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.staging_resources_id_seq OWNED BY public.staging_resources.id;


--
-- Name: staging_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.staging_rules (
    id integer NOT NULL,
    source_type character varying(20) NOT NULL,
    source_file character varying(300) NOT NULL,
    resource_group_code character varying(40) NOT NULL,
    rule_type character varying(50) DEFAULT 'selection'::character varying NOT NULL,
    rule_content text NOT NULL,
    difficulty character varying(50),
    imported_at timestamp without time zone DEFAULT now()
);


--
-- Name: TABLE staging_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.staging_rules IS 'Staging: 选择规则（逐来源）';


--
-- Name: staging_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.staging_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: staging_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.staging_rules_id_seq OWNED BY public.staging_rules.id;


--
-- Name: sys_announcement; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_announcement (
    id character varying(32) NOT NULL,
    title character varying(200) NOT NULL,
    msg_content text NOT NULL,
    sender character varying(50),
    priority character varying(10),
    msg_category character varying(10) DEFAULT '1'::character varying,
    msg_type character varying(10) DEFAULT 'USER'::character varying,
    send_status integer DEFAULT 0,
    send_time timestamp without time zone,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    bus_type character varying(64),
    bus_id character varying(64),
    user_ids text,
    create_by character varying(50),
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(50),
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE sys_announcement; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_announcement IS '系统消息表（JeecgBoot标准）';


--
-- Name: COLUMN sys_announcement.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.id IS '主键ID';


--
-- Name: COLUMN sys_announcement.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.title IS '公告标题';


--
-- Name: COLUMN sys_announcement.msg_content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.msg_content IS '消息内容';


--
-- Name: COLUMN sys_announcement.sender; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.sender IS '发送人';


--
-- Name: COLUMN sys_announcement.priority; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.priority IS '优先级（LOW/NORMAL/HIGH）';


--
-- Name: COLUMN sys_announcement.msg_category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.msg_category IS '消息类型：1-通知公告，2-系统消息';


--
-- Name: COLUMN sys_announcement.msg_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.msg_type IS '发布类型：ALL-全体用户，USER-指定用户';


--
-- Name: COLUMN sys_announcement.send_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.send_status IS '发布状态：0-未发布，1-已发布，2-撤回';


--
-- Name: COLUMN sys_announcement.send_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.send_time IS '发布时间';


--
-- Name: COLUMN sys_announcement.start_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.start_time IS '生效开始时间';


--
-- Name: COLUMN sys_announcement.end_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.end_time IS '生效结束时间';


--
-- Name: COLUMN sys_announcement.bus_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.bus_type IS '业务类型（用于跳转）';


--
-- Name: COLUMN sys_announcement.bus_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.bus_id IS '业务ID（用于跳转）';


--
-- Name: COLUMN sys_announcement.user_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.user_ids IS '指定用户ID列表，逗号分隔';


--
-- Name: COLUMN sys_announcement.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.create_by IS '创建人';


--
-- Name: COLUMN sys_announcement.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.create_time IS '创建时间';


--
-- Name: COLUMN sys_announcement.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.update_by IS '更新人';


--
-- Name: COLUMN sys_announcement.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement.update_time IS '更新时间';


--
-- Name: sys_announcement_send; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_announcement_send (
    id character varying(32) NOT NULL,
    annt_id character varying(32) NOT NULL,
    user_id integer NOT NULL,
    read_flag integer DEFAULT 0,
    read_time timestamp without time zone,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE sys_announcement_send; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_announcement_send IS '用户消息关联表（JeecgBoot标准）';


--
-- Name: COLUMN sys_announcement_send.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.id IS '主键ID';


--
-- Name: COLUMN sys_announcement_send.annt_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.annt_id IS '消息ID（关联sys_announcement.id）';


--
-- Name: COLUMN sys_announcement_send.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.user_id IS '用户ID（关联users.id）';


--
-- Name: COLUMN sys_announcement_send.read_flag; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.read_flag IS '是否已读：0-未读，1-已读';


--
-- Name: COLUMN sys_announcement_send.read_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.read_time IS '阅读时间';


--
-- Name: COLUMN sys_announcement_send.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_announcement_send.create_time IS '创建时间';


--
-- Name: sys_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_config (
    config_id bigint NOT NULL,
    config_name character varying(100) DEFAULT ''::character varying,
    config_key character varying(100) DEFAULT ''::character varying,
    config_value character varying(500) DEFAULT ''::character varying,
    config_type character(1) DEFAULT 'N'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_config IS '参数配置表';


--
-- Name: COLUMN sys_config.config_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.config_id IS '参数主键';


--
-- Name: COLUMN sys_config.config_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.config_name IS '参数名称';


--
-- Name: COLUMN sys_config.config_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.config_key IS '参数键名';


--
-- Name: COLUMN sys_config.config_value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.config_value IS '参数键值';


--
-- Name: COLUMN sys_config.config_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.config_type IS '系统内置（Y是 N否）';


--
-- Name: COLUMN sys_config.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.create_by IS '创建者';


--
-- Name: COLUMN sys_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.create_time IS '创建时间';


--
-- Name: COLUMN sys_config.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.update_by IS '更新者';


--
-- Name: COLUMN sys_config.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.update_time IS '更新时间';


--
-- Name: COLUMN sys_config.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_config.remark IS '备注';


--
-- Name: sys_config_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_config_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_config_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_config_config_id_seq OWNED BY public.sys_config.config_id;


--
-- Name: sys_dept; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_dept (
    dept_id bigint NOT NULL,
    parent_id bigint DEFAULT 0,
    ancestors character varying(50) DEFAULT ''::character varying,
    dept_name character varying(30) DEFAULT ''::character varying,
    order_num integer DEFAULT 0,
    leader character varying(20) DEFAULT NULL::character varying,
    phone character varying(11) DEFAULT NULL::character varying,
    email character varying(50) DEFAULT NULL::character varying,
    status character(1) DEFAULT '0'::bpchar,
    del_flag character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone
);


--
-- Name: TABLE sys_dept; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_dept IS '部门表';


--
-- Name: COLUMN sys_dept.dept_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.dept_id IS '部门id';


--
-- Name: COLUMN sys_dept.parent_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.parent_id IS '父部门id';


--
-- Name: COLUMN sys_dept.ancestors; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.ancestors IS '祖级列表';


--
-- Name: COLUMN sys_dept.dept_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.dept_name IS '部门名称';


--
-- Name: COLUMN sys_dept.order_num; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.order_num IS '显示顺序';


--
-- Name: COLUMN sys_dept.leader; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.leader IS '负责人';


--
-- Name: COLUMN sys_dept.phone; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.phone IS '联系电话';


--
-- Name: COLUMN sys_dept.email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.email IS '邮箱';


--
-- Name: COLUMN sys_dept.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.status IS '部门状态（0正常 1停用）';


--
-- Name: COLUMN sys_dept.del_flag; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_dept.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.create_by IS '创建者';


--
-- Name: COLUMN sys_dept.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.create_time IS '创建时间';


--
-- Name: COLUMN sys_dept.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.update_by IS '更新者';


--
-- Name: COLUMN sys_dept.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dept.update_time IS '更新时间';


--
-- Name: sys_dept_dept_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_dept_dept_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_dept_dept_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_dept_dept_id_seq OWNED BY public.sys_dept.dept_id;


--
-- Name: sys_dict_data; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_dict_data (
    dict_code bigint NOT NULL,
    dict_sort integer DEFAULT 0,
    dict_label character varying(100) DEFAULT ''::character varying,
    dict_value character varying(100) DEFAULT ''::character varying,
    dict_type character varying(100) DEFAULT ''::character varying,
    css_class character varying(100) DEFAULT NULL::character varying,
    list_class character varying(100) DEFAULT NULL::character varying,
    is_default character(1) DEFAULT 'N'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_dict_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_dict_data IS '字典数据表';


--
-- Name: COLUMN sys_dict_data.dict_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.dict_code IS '字典编码';


--
-- Name: COLUMN sys_dict_data.dict_sort; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.dict_sort IS '字典排序';


--
-- Name: COLUMN sys_dict_data.dict_label; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.dict_label IS '字典标签';


--
-- Name: COLUMN sys_dict_data.dict_value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.dict_value IS '字典键值';


--
-- Name: COLUMN sys_dict_data.dict_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.dict_type IS '字典类型';


--
-- Name: COLUMN sys_dict_data.css_class; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.css_class IS '样式属性（其他样式扩展）';


--
-- Name: COLUMN sys_dict_data.list_class; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.list_class IS '表格回显样式';


--
-- Name: COLUMN sys_dict_data.is_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.is_default IS '是否默认（Y是 N否）';


--
-- Name: COLUMN sys_dict_data.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_dict_data.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.create_by IS '创建者';


--
-- Name: COLUMN sys_dict_data.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.create_time IS '创建时间';


--
-- Name: COLUMN sys_dict_data.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.update_by IS '更新者';


--
-- Name: COLUMN sys_dict_data.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.update_time IS '更新时间';


--
-- Name: COLUMN sys_dict_data.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_data.remark IS '备注';


--
-- Name: sys_dict_data_dict_code_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_dict_data_dict_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_dict_data_dict_code_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_dict_data_dict_code_seq OWNED BY public.sys_dict_data.dict_code;


--
-- Name: sys_dict_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_dict_type (
    dict_id bigint NOT NULL,
    dict_name character varying(100) DEFAULT ''::character varying,
    dict_type character varying(100) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_dict_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_dict_type IS '字典类型表';


--
-- Name: COLUMN sys_dict_type.dict_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.dict_id IS '字典主键';


--
-- Name: COLUMN sys_dict_type.dict_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.dict_name IS '字典名称';


--
-- Name: COLUMN sys_dict_type.dict_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.dict_type IS '字典类型';


--
-- Name: COLUMN sys_dict_type.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_dict_type.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.create_by IS '创建者';


--
-- Name: COLUMN sys_dict_type.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.create_time IS '创建时间';


--
-- Name: COLUMN sys_dict_type.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.update_by IS '更新者';


--
-- Name: COLUMN sys_dict_type.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.update_time IS '更新时间';


--
-- Name: COLUMN sys_dict_type.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_dict_type.remark IS '备注';


--
-- Name: sys_dict_type_dict_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_dict_type_dict_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_dict_type_dict_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_dict_type_dict_id_seq OWNED BY public.sys_dict_type.dict_id;


--
-- Name: sys_job; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_job (
    job_id bigint NOT NULL,
    job_name character varying(64) DEFAULT ''::character varying,
    job_group character varying(64) DEFAULT 'DEFAULT'::character varying,
    invoke_target character varying(500) NOT NULL,
    cron_expression character varying(255) DEFAULT ''::character varying,
    misfire_policy character varying(20) DEFAULT '3'::character varying,
    concurrent character(1) DEFAULT '1'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT ''::character varying
);


--
-- Name: TABLE sys_job; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_job IS '定时任务调度表';


--
-- Name: COLUMN sys_job.job_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.job_id IS '任务ID';


--
-- Name: COLUMN sys_job.job_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.job_name IS '任务名称';


--
-- Name: COLUMN sys_job.job_group; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.job_group IS '任务组名';


--
-- Name: COLUMN sys_job.invoke_target; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.invoke_target IS '调用目标字符串';


--
-- Name: COLUMN sys_job.cron_expression; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.cron_expression IS 'cron执行表达式';


--
-- Name: COLUMN sys_job.misfire_policy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.misfire_policy IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';


--
-- Name: COLUMN sys_job.concurrent; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.concurrent IS '是否并发执行（0允许 1禁止）';


--
-- Name: COLUMN sys_job.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.status IS '状态（0正常 1暂停）';


--
-- Name: COLUMN sys_job.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.create_by IS '创建者';


--
-- Name: COLUMN sys_job.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.create_time IS '创建时间';


--
-- Name: COLUMN sys_job.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.update_by IS '更新者';


--
-- Name: COLUMN sys_job.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.update_time IS '更新时间';


--
-- Name: COLUMN sys_job.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job.remark IS '备注信息';


--
-- Name: sys_job_job_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_job_job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_job_job_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_job_job_id_seq OWNED BY public.sys_job.job_id;


--
-- Name: sys_job_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_job_log (
    job_log_id bigint NOT NULL,
    job_name character varying(64) NOT NULL,
    job_group character varying(64) NOT NULL,
    invoke_target character varying(500) NOT NULL,
    job_message character varying(500) DEFAULT NULL::character varying,
    status character(1) DEFAULT '0'::bpchar,
    exception_info character varying(2000) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE sys_job_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_job_log IS '定时任务调度日志表';


--
-- Name: COLUMN sys_job_log.job_log_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.job_log_id IS '任务日志ID';


--
-- Name: COLUMN sys_job_log.job_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.job_name IS '任务名称';


--
-- Name: COLUMN sys_job_log.job_group; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.job_group IS '任务组名';


--
-- Name: COLUMN sys_job_log.invoke_target; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.invoke_target IS '调用目标字符串';


--
-- Name: COLUMN sys_job_log.job_message; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.job_message IS '日志信息';


--
-- Name: COLUMN sys_job_log.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.status IS '执行状态（0正常 1失败）';


--
-- Name: COLUMN sys_job_log.exception_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.exception_info IS '异常信息';


--
-- Name: COLUMN sys_job_log.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_job_log.create_time IS '创建时间';


--
-- Name: sys_job_log_job_log_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_job_log_job_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_job_log_job_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_job_log_job_log_id_seq OWNED BY public.sys_job_log.job_log_id;


--
-- Name: sys_logininfor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_logininfor (
    info_id bigint NOT NULL,
    user_name character varying(50) DEFAULT ''::character varying,
    ipaddr character varying(128) DEFAULT ''::character varying,
    login_location character varying(255) DEFAULT ''::character varying,
    browser character varying(50) DEFAULT ''::character varying,
    os character varying(50) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    msg character varying(255) DEFAULT ''::character varying,
    login_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE sys_logininfor; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_logininfor IS '系统访问记录';


--
-- Name: COLUMN sys_logininfor.info_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.info_id IS '访问ID';


--
-- Name: COLUMN sys_logininfor.user_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.user_name IS '用户账号';


--
-- Name: COLUMN sys_logininfor.ipaddr; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.ipaddr IS '登录IP地址';


--
-- Name: COLUMN sys_logininfor.login_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.login_location IS '登录地点';


--
-- Name: COLUMN sys_logininfor.browser; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.browser IS '浏览器类型';


--
-- Name: COLUMN sys_logininfor.os; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.os IS '操作系统';


--
-- Name: COLUMN sys_logininfor.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.status IS '登录状态（0成功 1失败）';


--
-- Name: COLUMN sys_logininfor.msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.msg IS '提示消息';


--
-- Name: COLUMN sys_logininfor.login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_logininfor.login_time IS '访问时间';


--
-- Name: sys_logininfor_info_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_logininfor_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_logininfor_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_logininfor_info_id_seq OWNED BY public.sys_logininfor.info_id;


--
-- Name: sys_menu; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_menu (
    menu_id bigint NOT NULL,
    menu_name character varying(50) NOT NULL,
    parent_id bigint DEFAULT 0,
    order_num integer DEFAULT 0,
    path character varying(200) DEFAULT ''::character varying,
    component character varying(255) DEFAULT NULL::character varying,
    query character varying(255) DEFAULT NULL::character varying,
    route_name character varying(50) DEFAULT ''::character varying,
    is_frame integer DEFAULT 1,
    is_cache integer DEFAULT 0,
    menu_type character(1) DEFAULT ''::bpchar,
    visible character(1) DEFAULT '0'::bpchar,
    status character(1) DEFAULT '0'::bpchar,
    perms character varying(100) DEFAULT NULL::character varying,
    icon character varying(100) DEFAULT '#'::character varying,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT ''::character varying
);


--
-- Name: TABLE sys_menu; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_menu IS '菜单权限表';


--
-- Name: COLUMN sys_menu.menu_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.menu_id IS '菜单ID';


--
-- Name: COLUMN sys_menu.menu_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.menu_name IS '菜单名称';


--
-- Name: COLUMN sys_menu.parent_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.parent_id IS '父菜单ID';


--
-- Name: COLUMN sys_menu.order_num; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.order_num IS '显示顺序';


--
-- Name: COLUMN sys_menu.path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.path IS '路由地址';


--
-- Name: COLUMN sys_menu.component; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.component IS '组件路径';


--
-- Name: COLUMN sys_menu.query; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.query IS '路由参数';


--
-- Name: COLUMN sys_menu.route_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.route_name IS '路由名称';


--
-- Name: COLUMN sys_menu.is_frame; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.is_frame IS '是否为外链（0是 1否）';


--
-- Name: COLUMN sys_menu.is_cache; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.is_cache IS '是否缓存（0缓存 1不缓存）';


--
-- Name: COLUMN sys_menu.menu_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';


--
-- Name: COLUMN sys_menu.visible; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.visible IS '菜单状态（0显示 1隐藏）';


--
-- Name: COLUMN sys_menu.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.status IS '菜单状态（0正常 1停用）';


--
-- Name: COLUMN sys_menu.perms; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.perms IS '权限标识';


--
-- Name: COLUMN sys_menu.icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.icon IS '菜单图标';


--
-- Name: COLUMN sys_menu.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.create_by IS '创建者';


--
-- Name: COLUMN sys_menu.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.create_time IS '创建时间';


--
-- Name: COLUMN sys_menu.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.update_by IS '更新者';


--
-- Name: COLUMN sys_menu.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.update_time IS '更新时间';


--
-- Name: COLUMN sys_menu.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_menu.remark IS '备注';


--
-- Name: sys_menu_menu_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_menu_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_menu_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_menu_menu_id_seq OWNED BY public.sys_menu.menu_id;


--
-- Name: sys_notice; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_notice (
    notice_id bigint NOT NULL,
    notice_title character varying(50) NOT NULL,
    notice_type character(1) NOT NULL,
    notice_content text,
    status character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(255) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_notice; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_notice IS '通知公告表';


--
-- Name: COLUMN sys_notice.notice_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.notice_id IS '公告ID';


--
-- Name: COLUMN sys_notice.notice_title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.notice_title IS '公告标题';


--
-- Name: COLUMN sys_notice.notice_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.notice_type IS '公告类型（1通知 2公告）';


--
-- Name: COLUMN sys_notice.notice_content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.notice_content IS '公告内容';


--
-- Name: COLUMN sys_notice.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.status IS '公告状态（0正常 1关闭）';


--
-- Name: COLUMN sys_notice.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.create_by IS '创建者';


--
-- Name: COLUMN sys_notice.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.create_time IS '创建时间';


--
-- Name: COLUMN sys_notice.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.update_by IS '更新者';


--
-- Name: COLUMN sys_notice.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.update_time IS '更新时间';


--
-- Name: COLUMN sys_notice.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_notice.remark IS '备注';


--
-- Name: sys_notice_notice_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_notice_notice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_notice_notice_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_notice_notice_id_seq OWNED BY public.sys_notice.notice_id;


--
-- Name: sys_oper_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_oper_log (
    oper_id bigint NOT NULL,
    title character varying(50) DEFAULT ''::character varying,
    business_type integer DEFAULT 0,
    method character varying(200) DEFAULT ''::character varying,
    request_method character varying(10) DEFAULT ''::character varying,
    operator_type integer DEFAULT 0,
    oper_name character varying(50) DEFAULT ''::character varying,
    dept_name character varying(50) DEFAULT ''::character varying,
    oper_url character varying(255) DEFAULT ''::character varying,
    oper_ip character varying(128) DEFAULT ''::character varying,
    oper_location character varying(255) DEFAULT ''::character varying,
    oper_param character varying(2000) DEFAULT ''::character varying,
    json_result character varying(2000) DEFAULT ''::character varying,
    status integer DEFAULT 0,
    error_msg character varying(2000) DEFAULT ''::character varying,
    oper_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    cost_time bigint DEFAULT 0
);


--
-- Name: TABLE sys_oper_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_oper_log IS '操作日志记录';


--
-- Name: COLUMN sys_oper_log.oper_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_id IS '日志主键';


--
-- Name: COLUMN sys_oper_log.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.title IS '模块标题';


--
-- Name: COLUMN sys_oper_log.business_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.business_type IS '业务类型（0其它 1新增 2修改 3删除）';


--
-- Name: COLUMN sys_oper_log.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.method IS '方法名称';


--
-- Name: COLUMN sys_oper_log.request_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.request_method IS '请求方式';


--
-- Name: COLUMN sys_oper_log.operator_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.operator_type IS '操作类别（0其它 1后台用户 2手机端用户）';


--
-- Name: COLUMN sys_oper_log.oper_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_name IS '操作人员';


--
-- Name: COLUMN sys_oper_log.dept_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.dept_name IS '部门名称';


--
-- Name: COLUMN sys_oper_log.oper_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_url IS '请求URL';


--
-- Name: COLUMN sys_oper_log.oper_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_ip IS '主机地址';


--
-- Name: COLUMN sys_oper_log.oper_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_location IS '操作地点';


--
-- Name: COLUMN sys_oper_log.oper_param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_param IS '请求参数';


--
-- Name: COLUMN sys_oper_log.json_result; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.json_result IS '返回参数';


--
-- Name: COLUMN sys_oper_log.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.status IS '操作状态（0正常 1异常）';


--
-- Name: COLUMN sys_oper_log.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.error_msg IS '错误消息';


--
-- Name: COLUMN sys_oper_log.oper_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.oper_time IS '操作时间';


--
-- Name: COLUMN sys_oper_log.cost_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_oper_log.cost_time IS '消耗时间';


--
-- Name: sys_oper_log_oper_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_oper_log_oper_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_oper_log_oper_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_oper_log_oper_id_seq OWNED BY public.sys_oper_log.oper_id;


--
-- Name: sys_post; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_post (
    post_id bigint NOT NULL,
    post_code character varying(64) NOT NULL,
    post_name character varying(50) NOT NULL,
    post_sort integer NOT NULL,
    status character(1) NOT NULL,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_post; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_post IS '岗位信息表';


--
-- Name: COLUMN sys_post.post_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.post_id IS '岗位ID';


--
-- Name: COLUMN sys_post.post_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.post_code IS '岗位编码';


--
-- Name: COLUMN sys_post.post_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.post_name IS '岗位名称';


--
-- Name: COLUMN sys_post.post_sort; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.post_sort IS '显示顺序';


--
-- Name: COLUMN sys_post.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN sys_post.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.create_by IS '创建者';


--
-- Name: COLUMN sys_post.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.create_time IS '创建时间';


--
-- Name: COLUMN sys_post.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.update_by IS '更新者';


--
-- Name: COLUMN sys_post.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.update_time IS '更新时间';


--
-- Name: COLUMN sys_post.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_post.remark IS '备注';


--
-- Name: sys_post_post_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_post_post_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_post_post_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_post_post_id_seq OWNED BY public.sys_post.post_id;


--
-- Name: sys_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_role (
    role_id bigint NOT NULL,
    role_name character varying(30) NOT NULL,
    role_key character varying(100) NOT NULL,
    role_sort integer NOT NULL,
    data_scope character(1) DEFAULT '1'::bpchar,
    menu_check_strictly boolean DEFAULT true,
    dept_check_strictly boolean DEFAULT true,
    status character(1) NOT NULL,
    del_flag character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_role; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_role IS '角色信息表';


--
-- Name: COLUMN sys_role.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.role_id IS '角色ID';


--
-- Name: COLUMN sys_role.role_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.role_name IS '角色名称';


--
-- Name: COLUMN sys_role.role_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.role_key IS '角色权限字符串';


--
-- Name: COLUMN sys_role.role_sort; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.role_sort IS '显示顺序';


--
-- Name: COLUMN sys_role.data_scope; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.data_scope IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';


--
-- Name: COLUMN sys_role.menu_check_strictly; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.menu_check_strictly IS '菜单树选择项是否关联显示';


--
-- Name: COLUMN sys_role.dept_check_strictly; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.dept_check_strictly IS '部门树选择项是否关联显示';


--
-- Name: COLUMN sys_role.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.status IS '角色状态（0正常 1停用）';


--
-- Name: COLUMN sys_role.del_flag; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_role.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.create_by IS '创建者';


--
-- Name: COLUMN sys_role.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.create_time IS '创建时间';


--
-- Name: COLUMN sys_role.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.update_by IS '更新者';


--
-- Name: COLUMN sys_role.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.update_time IS '更新时间';


--
-- Name: COLUMN sys_role.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role.remark IS '备注';


--
-- Name: sys_role_menu; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_role_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


--
-- Name: TABLE sys_role_menu; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_role_menu IS '角色和菜单关联表';


--
-- Name: COLUMN sys_role_menu.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role_menu.role_id IS '角色ID';


--
-- Name: COLUMN sys_role_menu.menu_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_role_menu.menu_id IS '菜单ID';


--
-- Name: sys_role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_role_role_id_seq OWNED BY public.sys_role.role_id;


--
-- Name: sys_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_user (
    user_id bigint NOT NULL,
    dept_id bigint,
    user_name character varying(30) NOT NULL,
    nick_name character varying(30) NOT NULL,
    user_type character varying(2) DEFAULT '00'::character varying,
    email character varying(50) DEFAULT ''::character varying,
    phonenumber character varying(11) DEFAULT ''::character varying,
    sex character(1) DEFAULT '0'::bpchar,
    avatar character varying(100) DEFAULT ''::character varying,
    password character varying(100) DEFAULT ''::character varying,
    status character(1) DEFAULT '0'::bpchar,
    del_flag character(1) DEFAULT '0'::bpchar,
    login_ip character varying(128) DEFAULT ''::character varying,
    login_date timestamp without time zone,
    pwd_update_date timestamp without time zone,
    create_by character varying(64) DEFAULT ''::character varying,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: TABLE sys_user; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_user IS '用户信息表';


--
-- Name: COLUMN sys_user.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.user_id IS '用户ID';


--
-- Name: COLUMN sys_user.dept_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.dept_id IS '部门ID';


--
-- Name: COLUMN sys_user.user_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.user_name IS '用户账号';


--
-- Name: COLUMN sys_user.nick_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.nick_name IS '用户昵称';


--
-- Name: COLUMN sys_user.user_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.user_type IS '用户类型（00系统用户）';


--
-- Name: COLUMN sys_user.email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.email IS '用户邮箱';


--
-- Name: COLUMN sys_user.phonenumber; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.phonenumber IS '手机号码';


--
-- Name: COLUMN sys_user.sex; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.sex IS '用户性别（0男 1女 2未知）';


--
-- Name: COLUMN sys_user.avatar; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.avatar IS '头像地址';


--
-- Name: COLUMN sys_user.password; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.password IS '密码';


--
-- Name: COLUMN sys_user.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.status IS '账号状态（0正常 1停用）';


--
-- Name: COLUMN sys_user.del_flag; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.del_flag IS '删除标志（0代表存在 2代表删除）';


--
-- Name: COLUMN sys_user.login_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.login_ip IS '最后登录IP';


--
-- Name: COLUMN sys_user.login_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.login_date IS '最后登录时间';


--
-- Name: COLUMN sys_user.pwd_update_date; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.pwd_update_date IS '密码最后更新时间';


--
-- Name: COLUMN sys_user.create_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.create_by IS '创建者';


--
-- Name: COLUMN sys_user.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.create_time IS '创建时间';


--
-- Name: COLUMN sys_user.update_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.update_by IS '更新者';


--
-- Name: COLUMN sys_user.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.update_time IS '更新时间';


--
-- Name: COLUMN sys_user.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user.remark IS '备注';


--
-- Name: sys_user_online; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_user_online (
    sessionid character varying(50) DEFAULT ''::character varying NOT NULL,
    login_name character varying(50) DEFAULT ''::character varying,
    dept_name character varying(50) DEFAULT ''::character varying,
    ipaddr character varying(128) DEFAULT ''::character varying,
    login_location character varying(255) DEFAULT ''::character varying,
    browser character varying(50) DEFAULT ''::character varying,
    os character varying(50) DEFAULT ''::character varying,
    status character varying(10) DEFAULT ''::character varying,
    start_timestamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    last_access_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    expire_time integer DEFAULT 0
);


--
-- Name: TABLE sys_user_online; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_user_online IS '在线用户记录';


--
-- Name: COLUMN sys_user_online.sessionid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.sessionid IS '用户会话id';


--
-- Name: COLUMN sys_user_online.login_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.login_name IS '登录账号';


--
-- Name: COLUMN sys_user_online.dept_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.dept_name IS '部门名称';


--
-- Name: COLUMN sys_user_online.ipaddr; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.ipaddr IS '登录IP地址';


--
-- Name: COLUMN sys_user_online.login_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.login_location IS '登录地点';


--
-- Name: COLUMN sys_user_online.browser; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.browser IS '浏览器类型';


--
-- Name: COLUMN sys_user_online.os; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.os IS '操作系统';


--
-- Name: COLUMN sys_user_online.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.status IS '在线状态on_line在线off_line离线';


--
-- Name: COLUMN sys_user_online.start_timestamp; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.start_timestamp IS 'session创建时间';


--
-- Name: COLUMN sys_user_online.last_access_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.last_access_time IS 'session最后访问时间';


--
-- Name: COLUMN sys_user_online.expire_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_online.expire_time IS '超时时间，单位为分钟';


--
-- Name: sys_user_post; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_user_post (
    user_id bigint NOT NULL,
    post_id bigint NOT NULL
);


--
-- Name: TABLE sys_user_post; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_user_post IS '用户与岗位关联表';


--
-- Name: COLUMN sys_user_post.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_post.user_id IS '用户ID';


--
-- Name: COLUMN sys_user_post.post_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_post.post_id IS '岗位ID';


--
-- Name: sys_user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sys_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: TABLE sys_user_role; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.sys_user_role IS '用户和角色关联表';


--
-- Name: COLUMN sys_user_role.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_role.user_id IS '用户ID';


--
-- Name: COLUMN sys_user_role.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.sys_user_role.role_id IS '角色ID';


--
-- Name: sys_user_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sys_user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sys_user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sys_user_user_id_seq OWNED BY public.sys_user.user_id;


--
-- Name: system_common_param_definition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_common_param_definition (
    id bigint NOT NULL,
    param_code character varying(100) NOT NULL,
    param_name character varying(200) NOT NULL,
    value_type character varying(30) NOT NULL,
    remark text
);


--
-- Name: system_common_param_definition_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.system_common_param_definition_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: system_common_param_definition_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.system_common_param_definition_id_seq OWNED BY public.system_common_param_definition.id;


--
-- Name: system_common_param_input; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_common_param_input (
    id bigint NOT NULL,
    param_code character varying(100) NOT NULL,
    param_value text,
    session_id character varying(100)
);


--
-- Name: system_common_param_input_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.system_common_param_input_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: system_common_param_input_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.system_common_param_input_id_seq OWNED BY public.system_common_param_input.id;


--
-- Name: system_content; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_content (
    id bigint NOT NULL,
    content_key character varying(128) NOT NULL,
    title character varying(512) DEFAULT ''::character varying NOT NULL,
    body_html text,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by integer
);


--
-- Name: system_content_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.system_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: system_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.system_content_id_seq OWNED BY public.system_content.id;


--
-- Name: user_match_preferences; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_match_preferences (
    id integer NOT NULL,
    user_id integer NOT NULL,
    field_name character varying(50) NOT NULL,
    field_value character varying(100) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_match_preferences_field_name_check CHECK (((field_name)::text = ANY (ARRAY[('color'::character varying)::text, ('size'::character varying)::text, ('tightness'::character varying)::text, ('temperature'::character varying)::text, ('performance'::character varying)::text])))
);


--
-- Name: user_match_preferences_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_match_preferences_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_match_preferences_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_match_preferences_id_seq OWNED BY public.user_match_preferences.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    created_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    role character varying(50) DEFAULT 'USER'::character varying,
    status character(1) DEFAULT '0'::bpchar,
    del_flag character(1) DEFAULT '0'::bpchar,
    create_by character varying(64) DEFAULT ''::character varying,
    update_by character varying(64) DEFAULT ''::character varying,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    remark character varying(500) DEFAULT NULL::character varying
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: uv_effect_feasibility_rules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.uv_effect_feasibility_rules (
    id integer NOT NULL,
    effect_id integer NOT NULL,
    product_type_id integer,
    feasibility_status character(1) DEFAULT 'V'::bpchar NOT NULL,
    condition_desc character varying(500),
    notice_ids integer[],
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE uv_effect_feasibility_rules; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.uv_effect_feasibility_rules IS 'UV烫金可行性规则';


--
-- Name: COLUMN uv_effect_feasibility_rules.effect_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_feasibility_rules.effect_id IS 'UV效果类型';


--
-- Name: COLUMN uv_effect_feasibility_rules.product_type_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_feasibility_rules.product_type_id IS '产品类型';


--
-- Name: COLUMN uv_effect_feasibility_rules.feasibility_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_feasibility_rules.feasibility_status IS '可行性：V=可行 X=不可行 NA=不确定 ▷=有条件';


--
-- Name: COLUMN uv_effect_feasibility_rules.condition_desc; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_feasibility_rules.condition_desc IS '条件说明';


--
-- Name: COLUMN uv_effect_feasibility_rules.notice_ids; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_feasibility_rules.notice_ids IS '关联注意事项ID';


--
-- Name: uv_effect_feasibility_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.uv_effect_feasibility_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: uv_effect_feasibility_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.uv_effect_feasibility_rules_id_seq OWNED BY public.uv_effect_feasibility_rules.id;


--
-- Name: uv_effect_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.uv_effect_options (
    id integer NOT NULL,
    effect_name character varying(100) NOT NULL,
    effect_code character varying(50),
    description character varying(255),
    is_active boolean DEFAULT true,
    sort_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE uv_effect_options; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.uv_effect_options IS 'UV烫金效果类型选项';


--
-- Name: COLUMN uv_effect_options.effect_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_options.effect_name IS '效果名称';


--
-- Name: COLUMN uv_effect_options.effect_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.uv_effect_options.effect_code IS '效果编码';


--
-- Name: uv_effect_options_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.uv_effect_options_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: uv_effect_options_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.uv_effect_options_id_seq OWNED BY public.uv_effect_options.id;


--
-- Name: uv_oil_matte_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.uv_oil_matte_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: uv_oil_matte_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.uv_oil_matte_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: uv_oil_matte_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.uv_oil_matte_compatibility_id_seq OWNED BY public.uv_oil_matte_compatibility.id;


--
-- Name: uv_oil_matte_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.uv_oil_matte_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: uv_oil_matte_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.uv_oil_matte_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: uv_oil_matte_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.uv_oil_matte_product_id_seq OWNED BY public.uv_oil_matte_product.id;


--
-- Name: water_varnish_matte_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.water_varnish_matte_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    post_processing_step character varying(500) NOT NULL,
    compatibility_status character varying(10) NOT NULL,
    remark text,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: water_varnish_matte_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.water_varnish_matte_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: water_varnish_matte_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.water_varnish_matte_compatibility_id_seq OWNED BY public.water_varnish_matte_compatibility.id;


--
-- Name: water_varnish_matte_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.water_varnish_matte_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(500) NOT NULL,
    usage text,
    category character varying(200) DEFAULT NULL::character varying,
    color character varying(200) DEFAULT NULL::character varying,
    responsible_person character varying(200) DEFAULT NULL::character varying,
    min_sheet_size character varying(200) DEFAULT NULL::character varying,
    max_sheet_size character varying(200) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    design_info text,
    applicable_interface text,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: water_varnish_matte_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.water_varnish_matte_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: water_varnish_matte_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.water_varnish_matte_product_id_seq OWNED BY public.water_varnish_matte_product.id;


--
-- Name: wear_resistant_gold_paper_skip_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wear_resistant_gold_paper_skip_config (
    id integer NOT NULL,
    hot_stamping_paper_type character varying(255) NOT NULL,
    remarks text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE wear_resistant_gold_paper_skip_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wear_resistant_gold_paper_skip_config IS '耐磨金纸映射跳过配置表：按烫金纸类型在 Match 中跳过耐磨映射';


--
-- Name: COLUMN wear_resistant_gold_paper_skip_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wear_resistant_gold_paper_skip_config.id IS '主键ID';


--
-- Name: COLUMN wear_resistant_gold_paper_skip_config.hot_stamping_paper_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wear_resistant_gold_paper_skip_config.hot_stamping_paper_type IS '烫金纸类型（对应 product.hot_stamping_paper_type）';


--
-- Name: COLUMN wear_resistant_gold_paper_skip_config.remarks; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wear_resistant_gold_paper_skip_config.remarks IS '备注';


--
-- Name: COLUMN wear_resistant_gold_paper_skip_config.created_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wear_resistant_gold_paper_skip_config.created_at IS '创建时间';


--
-- Name: COLUMN wear_resistant_gold_paper_skip_config.updated_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wear_resistant_gold_paper_skip_config.updated_at IS '更新时间';


--
-- Name: wear_resistant_gold_paper_skip_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.wear_resistant_gold_paper_skip_config_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: wear_resistant_gold_paper_skip_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.wear_resistant_gold_paper_skip_config_id_seq OWNED BY public.wear_resistant_gold_paper_skip_config.id;


--
-- Name: workflow_execution_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.workflow_execution_log (
    id integer NOT NULL,
    workflow_id character varying(100) NOT NULL,
    run_id character varying(100) NOT NULL,
    status character varying(50) NOT NULL,
    inputs jsonb NOT NULL,
    outputs jsonb,
    error_message text,
    execution_time integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: workflow_execution_log_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.workflow_execution_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: workflow_execution_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.workflow_execution_log_id_seq OWNED BY public.workflow_execution_log.id;


--
-- Name: yaguang_uv_oil_compatibility; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.yaguang_uv_oil_compatibility (
    id integer NOT NULL,
    product_id integer NOT NULL,
    process_category character varying(100) DEFAULT NULL::character varying,
    step_name character varying(200) DEFAULT NULL::character varying,
    compatibility_status character varying(50) DEFAULT NULL::character varying,
    display_order integer DEFAULT 0,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE yaguang_uv_oil_compatibility; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.yaguang_uv_oil_compatibility IS '哑光UV油后加工兼容性表';


--
-- Name: COLUMN yaguang_uv_oil_compatibility.process_category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yaguang_uv_oil_compatibility.process_category IS '工序大类: 印刷/烫金/过胶/丝印/植毛/啤/手工/其他';


--
-- Name: COLUMN yaguang_uv_oil_compatibility.step_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yaguang_uv_oil_compatibility.step_name IS '步骤名称';


--
-- Name: yaguang_uv_oil_compatibility_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.yaguang_uv_oil_compatibility_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: yaguang_uv_oil_compatibility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.yaguang_uv_oil_compatibility_id_seq OWNED BY public.yaguang_uv_oil_compatibility.id;


--
-- Name: yaguang_uv_oil_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.yaguang_uv_oil_product (
    id integer NOT NULL,
    material_code character varying(100) DEFAULT NULL::character varying,
    supplier_code character varying(100) DEFAULT NULL::character varying,
    stock_code character varying(100) DEFAULT NULL::character varying,
    material_name character varying(200) DEFAULT NULL::character varying,
    usage text,
    category character varying(100) DEFAULT NULL::character varying,
    thickness character varying(100) DEFAULT NULL::character varying,
    size character varying(200) DEFAULT NULL::character varying,
    color character varying(100) DEFAULT NULL::character varying,
    shape character varying(100) DEFAULT NULL::character varying,
    responsible_person character varying(100) DEFAULT NULL::character varying,
    min_sheet_size character varying(100) DEFAULT NULL::character varying,
    max_sheet_size character varying(100) DEFAULT NULL::character varying,
    min_dot character varying(100) DEFAULT NULL::character varying,
    max_dot character varying(100) DEFAULT NULL::character varying,
    min_line character varying(100) DEFAULT NULL::character varying,
    max_line character varying(100) DEFAULT NULL::character varying,
    min_spacing character varying(100) DEFAULT NULL::character varying,
    max_spacing character varying(100) DEFAULT NULL::character varying,
    min_pattern_area character varying(100) DEFAULT NULL::character varying,
    max_pattern_area character varying(100) DEFAULT NULL::character varying,
    applicable_product text,
    structure_single_side character varying(10) DEFAULT NULL::character varying,
    structure_double_side character varying(10) DEFAULT NULL::character varying,
    structure_cover character varying(10) DEFAULT NULL::character varying,
    structure_spine character varying(10) DEFAULT NULL::character varying,
    structure_deboss character varying(10) DEFAULT NULL::character varying,
    structure_inner_page character varying(10) DEFAULT NULL::character varying,
    substrate_thickness character varying(100) DEFAULT NULL::character varying,
    paper_surface_applicable text,
    paper_surface_inapplicable text,
    ink_surface_applicable text,
    ink_surface_inapplicable text,
    coating_surface_applicable text,
    coating_surface_inapplicable text,
    writing_standard_pen character varying(10) DEFAULT NULL::character varying,
    writing_customer_pen character varying(10) DEFAULT NULL::character varying,
    notes text,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE yaguang_uv_oil_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.yaguang_uv_oil_product IS '哑光UV油产品表';


--
-- Name: yaguang_uv_oil_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.yaguang_uv_oil_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: yaguang_uv_oil_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.yaguang_uv_oil_product_id_seq OWNED BY public.yaguang_uv_oil_product.id;


--
-- Name: admin_change_records id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_records ALTER COLUMN id SET DEFAULT nextval('public.admin_change_records_id_seq'::regclass);


--
-- Name: admin_change_snapshots id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_snapshots ALTER COLUMN id SET DEFAULT nextval('public.admin_change_snapshots_id_seq'::regclass);


--
-- Name: admin_operation_log id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_operation_log ALTER COLUMN id SET DEFAULT nextval('public.admin_operation_log_id_seq'::regclass);


--
-- Name: ai_recommendation_history id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ai_recommendation_history ALTER COLUMN id SET DEFAULT nextval('public.ai_recommendation_history_id_seq'::regclass);


--
-- Name: ai_search_history id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ai_search_history ALTER COLUMN id SET DEFAULT nextval('public.ai_search_history_id_seq'::regclass);


--
-- Name: cloth_paper_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_compatibility ALTER COLUMN id SET DEFAULT nextval('public.cloth_paper_compatibility_id_seq'::regclass);


--
-- Name: cloth_paper_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_types ALTER COLUMN id SET DEFAULT nextval('public.cloth_paper_types_id_seq'::regclass);


--
-- Name: code_mapping id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code_mapping ALTER COLUMN id SET DEFAULT nextval('public.code_mapping_id_seq'::regclass);


--
-- Name: cold_foil_feasibility_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_feasibility_rules ALTER COLUMN id SET DEFAULT nextval('public.cold_foil_feasibility_rules_id_seq'::regclass);


--
-- Name: cold_foil_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_type_options ALTER COLUMN id SET DEFAULT nextval('public.cold_foil_type_options_id_seq'::regclass);


--
-- Name: embossing_feasibility_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_feasibility_rules ALTER COLUMN id SET DEFAULT nextval('public.embossing_feasibility_rules_id_seq'::regclass);


--
-- Name: embossing_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_type_options ALTER COLUMN id SET DEFAULT nextval('public.embossing_type_options_id_seq'::regclass);


--
-- Name: evaluation_session id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evaluation_session ALTER COLUMN id SET DEFAULT nextval('public.evaluation_session_id_seq'::regclass);


--
-- Name: gen_table table_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gen_table ALTER COLUMN table_id SET DEFAULT nextval('public.gen_table_table_id_seq'::regclass);


--
-- Name: gen_table_column column_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gen_table_column ALTER COLUMN column_id SET DEFAULT nextval('public.gen_table_column_column_id_seq'::regclass);


--
-- Name: global_parameters id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.global_parameters ALTER COLUMN id SET DEFAULT nextval('public.global_parameters_id_seq'::regclass);


--
-- Name: gold_foil_type id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gold_foil_type ALTER COLUMN id SET DEFAULT nextval('public.gold_foil_type_id_seq'::regclass);


--
-- Name: hot_stamping_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_compatibility ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_compatibility_id_seq'::regclass);


--
-- Name: hot_stamping_pattern_base id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_pattern_base ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_pattern_base_id_seq'::regclass);


--
-- Name: hot_stamping_patternx_area_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_patternx_area_compatibility ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_patternx_compatibility_id_seq'::regclass);


--
-- Name: hot_stamping_patternx_area_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_patternx_area_options ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_patternx_area_options_id_seq'::regclass);


--
-- Name: hot_stamping_type_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_type_compatibility ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_type_compatibility_id_seq'::regclass);


--
-- Name: hot_stamping_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_type_options ALTER COLUMN id SET DEFAULT nextval('public.hot_stamping_type_options_id_seq'::regclass);


--
-- Name: import_snapshot id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.import_snapshot ALTER COLUMN id SET DEFAULT nextval('public.import_snapshot_id_seq'::regclass);


--
-- Name: interface_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interface_type_options ALTER COLUMN id SET DEFAULT nextval('public.interface_type_options_id_seq'::regclass);


--
-- Name: knowledge_base_stats id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_stats ALTER COLUMN id SET DEFAULT nextval('public.knowledge_base_stats_id_seq'::regclass);


--
-- Name: knowledge_base_status id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_status ALTER COLUMN id SET DEFAULT nextval('public.knowledge_base_status_id_seq'::regclass);


--
-- Name: knowledge_base_versions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_versions ALTER COLUMN id SET DEFAULT nextval('public.knowledge_base_versions_id_seq'::regclass);


--
-- Name: lamination_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_compatibility ALTER COLUMN id SET DEFAULT nextval('public.lamination_compatibility_id_seq'::regclass);


--
-- Name: lamination_feasibility_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_feasibility_rules ALTER COLUMN id SET DEFAULT nextval('public.lamination_feasibility_rules_id_seq'::regclass);


--
-- Name: lamination_material_categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_categories ALTER COLUMN id SET DEFAULT nextval('public.lamination_material_categories_id_seq'::regclass);


--
-- Name: lamination_material_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_compatibility ALTER COLUMN id SET DEFAULT nextval('public.lamination_material_compatibility_id_seq'::regclass);


--
-- Name: lamination_material_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_options ALTER COLUMN id SET DEFAULT nextval('public.lamination_material_options_id_seq'::regclass);


--
-- Name: lamination_material_products id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_products ALTER COLUMN id SET DEFAULT nextval('public.lamination_material_products_id_seq'::regclass);


--
-- Name: lamination_substrate_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_substrate_options ALTER COLUMN id SET DEFAULT nextval('public.lamination_substrate_options_id_seq'::regclass);


--
-- Name: lamination_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_type_options ALTER COLUMN id SET DEFAULT nextval('public.lamination_type_options_id_seq'::regclass);


--
-- Name: leo_gp_numbers id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leo_gp_numbers ALTER COLUMN id SET DEFAULT nextval('public.leo_gp_numbers_id_seq'::regclass);


--
-- Name: material_catalog id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_catalog ALTER COLUMN id SET DEFAULT nextval('public.material_catalog_id_seq'::regclass);


--
-- Name: material_process_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_process_compatibility ALTER COLUMN id SET DEFAULT nextval('public.material_process_compatibility_id_seq'::regclass);


--
-- Name: notice_dictionary id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notice_dictionary ALTER COLUMN id SET DEFAULT nextval('public.notice_dictionary_id_seq'::regclass);


--
-- Name: old)cloth_paper id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."old)cloth_paper" ALTER COLUMN id SET DEFAULT nextval('public.cloth_paper_id_seq'::regclass);


--
-- Name: old_film_butter_paper id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_film_butter_paper ALTER COLUMN id SET DEFAULT nextval('public.film_butter_paper_id_seq'::regclass);


--
-- Name: old_matte_lamination id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_matte_lamination ALTER COLUMN id SET DEFAULT nextval('public.matte_lamination_id_seq'::regclass);


--
-- Name: old_oil_and_uv_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_oil_and_uv_types ALTER COLUMN id SET DEFAULT nextval('public.oil_and_uv_types_id_seq'::regclass);


--
-- Name: old_pattern id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_pattern ALTER COLUMN id SET DEFAULT nextval('public.pattern_id_seq'::regclass);


--
-- Name: paper_material_categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.paper_material_categories ALTER COLUMN id SET DEFAULT nextval('public.paper_material_categories_id_seq'::regclass);


--
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions ALTER COLUMN id SET DEFAULT nextval('public.permissions_id_seq'::regclass);


--
-- Name: post_processing_leduvglitter id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_leduvglitter ALTER COLUMN id SET DEFAULT nextval('public.post_processing_leduvglitter_id_seq'::regclass);


--
-- Name: post_processing_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_options ALTER COLUMN id SET DEFAULT nextval('public.post_processing_options_id_seq'::regclass);


--
-- Name: post_processing_print id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_print ALTER COLUMN id SET DEFAULT nextval('public.post_processing_print_id_seq'::regclass);


--
-- Name: post_processing_print_uv id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_print_uv ALTER COLUMN id SET DEFAULT nextval('public.post_processing_print_uv_id_seq'::regclass);


--
-- Name: pre_processing_steps id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pre_processing_steps ALTER COLUMN id SET DEFAULT nextval('public.pre_processing_steps_id_seq'::regclass);


--
-- Name: pre_processing_steps_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pre_processing_steps_options ALTER COLUMN id SET DEFAULT nextval('public.pre_processing_steps_options_id_seq'::regclass);


--
-- Name: pricing id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pricing ALTER COLUMN id SET DEFAULT nextval('public.pricing_id_seq'::regclass);


--
-- Name: printing_feasibility_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_feasibility_rules ALTER COLUMN id SET DEFAULT nextval('public.printing_feasibility_rules_id_seq'::regclass);


--
-- Name: printing_material_categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_material_categories ALTER COLUMN id SET DEFAULT nextval('public.printing_material_categories_id_seq'::regclass);


--
-- Name: printing_process_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_process_options ALTER COLUMN id SET DEFAULT nextval('public.printing_process_options_id_seq'::regclass);


--
-- Name: printing_substrate_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_substrate_options ALTER COLUMN id SET DEFAULT nextval('public.printing_substrate_options_id_seq'::regclass);


--
-- Name: processing_after_ironing id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.processing_after_ironing ALTER COLUMN id SET DEFAULT nextval('public.processing_after_ironing_id_seq'::regclass);


--
-- Name: product_type_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_options ALTER COLUMN id SET DEFAULT nextval('public.product_type_options_id_seq'::regclass);


--
-- Name: product_type_sort_config id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_sort_config ALTER COLUMN id SET DEFAULT nextval('public.product_type_sort_config_id_seq'::regclass);


--
-- Name: production_capacity id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_capacity ALTER COLUMN id SET DEFAULT nextval('public.production_capacity_id_seq'::regclass);


--
-- Name: production_resources id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_resources ALTER COLUMN id SET DEFAULT nextval('public.production_resources_id_seq'::regclass);


--
-- Name: production_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_rules ALTER COLUMN id SET DEFAULT nextval('public.production_rules_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: reference_coating_surface id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_coating_surface ALTER COLUMN id SET DEFAULT nextval('public.reference_coating_surface_id_seq'::regclass);


--
-- Name: reference_ink_surface id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_ink_surface ALTER COLUMN id SET DEFAULT nextval('public.reference_ink_surface_id_seq'::regclass);


--
-- Name: reference_paper_surface id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_paper_surface ALTER COLUMN id SET DEFAULT nextval('public.reference_paper_surface_id_seq'::regclass);


--
-- Name: reference_product_family id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_product_family ALTER COLUMN id SET DEFAULT nextval('public.reference_product_family_id_seq'::regclass);


--
-- Name: reference_writing_function id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_writing_function ALTER COLUMN id SET DEFAULT nextval('public.reference_writing_function_id_seq'::regclass);


--
-- Name: rg_resource_group id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_group_id_seq'::regclass);


--
-- Name: rg_resource_group_capacity id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_capacity ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_group_capacity_id_seq'::regclass);


--
-- Name: rg_resource_group_detail id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_detail ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_group_detail_id_seq'::regclass);


--
-- Name: rg_resource_group_task_map id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_task_map ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_group_task_map_id_seq'::regclass);


--
-- Name: rg_resource_rule_condition id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_condition ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_rule_condition_id_seq'::regclass);


--
-- Name: rg_resource_rule_header id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_header ALTER COLUMN id SET DEFAULT nextval('public.rg_resource_rule_header_id_seq'::regclass);


--
-- Name: rg_task_definition id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_task_definition ALTER COLUMN id SET DEFAULT nextval('public.rg_task_definition_id_seq'::regclass);


--
-- Name: rg_work_center id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_work_center ALTER COLUMN id SET DEFAULT nextval('public.rg_work_center_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: rule_evaluation_log id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rule_evaluation_log ALTER COLUMN id SET DEFAULT nextval('public.rule_evaluation_log_id_seq'::regclass);


--
-- Name: screen_printing_uv_categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.screen_printing_uv_categories ALTER COLUMN id SET DEFAULT nextval('public.screen_printing_uv_categories_id_seq'::regclass);


--
-- Name: series_priority_rule id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule ALTER COLUMN id SET DEFAULT nextval('public.series_priority_rule_id_seq'::regclass);


--
-- Name: series_priority_rule_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule_item ALTER COLUMN id SET DEFAULT nextval('public.series_priority_rule_item_id_seq'::regclass);


--
-- Name: silicone_coarse_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_coarse_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_coarse_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_coarse_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_coarse_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_coarse_uv_product_id_seq'::regclass);


--
-- Name: silicone_glittering_star_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glittering_star_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_glittering_star_compatibility_id_seq'::regclass);


--
-- Name: silicone_glittering_star_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glittering_star_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_glittering_star_product_id_seq'::regclass);


--
-- Name: silicone_glow_ink_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glow_ink_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_glow_ink_compatibility_id_seq'::regclass);


--
-- Name: silicone_glow_ink_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glow_ink_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_glow_ink_product_id_seq'::regclass);


--
-- Name: silicone_led_uv_spray_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_led_uv_spray_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_led_uv_spray_compatibility_id_seq'::regclass);


--
-- Name: silicone_led_uv_spray_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_led_uv_spray_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_led_uv_spray_product_id_seq'::regclass);


--
-- Name: silicone_mica_pearl_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_mica_pearl_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_mica_pearl_compatibility_id_seq'::regclass);


--
-- Name: silicone_mica_pearl_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_mica_pearl_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_mica_pearl_product_id_seq'::regclass);


--
-- Name: silicone_orange_peel_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_orange_peel_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_orange_peel_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_orange_peel_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_orange_peel_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_orange_peel_uv_product_id_seq'::regclass);


--
-- Name: silicone_sandblast_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_sandblast_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_sandblast_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_sandblast_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_sandblast_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_sandblast_uv_product_id_seq'::regclass);


--
-- Name: silicone_screen_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_screen_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_screen_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_screen_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_screen_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_screen_uv_product_id_seq'::regclass);


--
-- Name: silicone_water_base_transparent_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_water_base_transparent_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_water_base_transparent_compatibility_id_seq'::regclass);


--
-- Name: silicone_water_base_transparent_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_water_base_transparent_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_water_base_transparent_product_id_seq'::regclass);


--
-- Name: silicone_watercolor_ink_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_watercolor_ink_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_watercolor_ink_compatibility_id_seq'::regclass);


--
-- Name: silicone_watercolor_ink_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_watercolor_ink_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_watercolor_ink_product_id_seq'::regclass);


--
-- Name: silicone_white_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_white_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_white_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_white_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_white_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_white_uv_product_id_seq'::regclass);


--
-- Name: silicone_wrinkle_uv_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_wrinkle_uv_compatibility ALTER COLUMN id SET DEFAULT nextval('public.silicone_wrinkle_uv_compatibility_id_seq'::regclass);


--
-- Name: silicone_wrinkle_uv_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_wrinkle_uv_product ALTER COLUMN id SET DEFAULT nextval('public.silicone_wrinkle_uv_product_id_seq'::regclass);


--
-- Name: specifications id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.specifications ALTER COLUMN id SET DEFAULT nextval('public.specifications_id_seq'::regclass);


--
-- Name: staging_capacity id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_capacity ALTER COLUMN id SET DEFAULT nextval('public.staging_capacity_id_seq'::regclass);


--
-- Name: staging_resource_groups id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_resource_groups ALTER COLUMN id SET DEFAULT nextval('public.staging_resource_groups_id_seq'::regclass);


--
-- Name: staging_resources id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_resources ALTER COLUMN id SET DEFAULT nextval('public.staging_resources_id_seq'::regclass);


--
-- Name: staging_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_rules ALTER COLUMN id SET DEFAULT nextval('public.staging_rules_id_seq'::regclass);


--
-- Name: sys_config config_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_config ALTER COLUMN config_id SET DEFAULT nextval('public.sys_config_config_id_seq'::regclass);


--
-- Name: sys_dept dept_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dept ALTER COLUMN dept_id SET DEFAULT nextval('public.sys_dept_dept_id_seq'::regclass);


--
-- Name: sys_dict_data dict_code; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dict_data ALTER COLUMN dict_code SET DEFAULT nextval('public.sys_dict_data_dict_code_seq'::regclass);


--
-- Name: sys_dict_type dict_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dict_type ALTER COLUMN dict_id SET DEFAULT nextval('public.sys_dict_type_dict_id_seq'::regclass);


--
-- Name: sys_job job_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_job ALTER COLUMN job_id SET DEFAULT nextval('public.sys_job_job_id_seq'::regclass);


--
-- Name: sys_job_log job_log_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_job_log ALTER COLUMN job_log_id SET DEFAULT nextval('public.sys_job_log_job_log_id_seq'::regclass);


--
-- Name: sys_logininfor info_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_logininfor ALTER COLUMN info_id SET DEFAULT nextval('public.sys_logininfor_info_id_seq'::regclass);


--
-- Name: sys_menu menu_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_menu ALTER COLUMN menu_id SET DEFAULT nextval('public.sys_menu_menu_id_seq'::regclass);


--
-- Name: sys_notice notice_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_notice ALTER COLUMN notice_id SET DEFAULT nextval('public.sys_notice_notice_id_seq'::regclass);


--
-- Name: sys_oper_log oper_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_oper_log ALTER COLUMN oper_id SET DEFAULT nextval('public.sys_oper_log_oper_id_seq'::regclass);


--
-- Name: sys_post post_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_post ALTER COLUMN post_id SET DEFAULT nextval('public.sys_post_post_id_seq'::regclass);


--
-- Name: sys_role role_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_role ALTER COLUMN role_id SET DEFAULT nextval('public.sys_role_role_id_seq'::regclass);


--
-- Name: sys_user user_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_user ALTER COLUMN user_id SET DEFAULT nextval('public.sys_user_user_id_seq'::regclass);


--
-- Name: system_common_param_definition id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_common_param_definition ALTER COLUMN id SET DEFAULT nextval('public.system_common_param_definition_id_seq'::regclass);


--
-- Name: system_common_param_input id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_common_param_input ALTER COLUMN id SET DEFAULT nextval('public.system_common_param_input_id_seq'::regclass);


--
-- Name: system_content id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_content ALTER COLUMN id SET DEFAULT nextval('public.system_content_id_seq'::regclass);


--
-- Name: user_match_preferences id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_match_preferences ALTER COLUMN id SET DEFAULT nextval('public.user_match_preferences_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: uv_effect_feasibility_rules id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_feasibility_rules ALTER COLUMN id SET DEFAULT nextval('public.uv_effect_feasibility_rules_id_seq'::regclass);


--
-- Name: uv_effect_options id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_options ALTER COLUMN id SET DEFAULT nextval('public.uv_effect_options_id_seq'::regclass);


--
-- Name: uv_oil_matte_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_oil_matte_compatibility ALTER COLUMN id SET DEFAULT nextval('public.uv_oil_matte_compatibility_id_seq'::regclass);


--
-- Name: uv_oil_matte_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_oil_matte_product ALTER COLUMN id SET DEFAULT nextval('public.uv_oil_matte_product_id_seq'::regclass);


--
-- Name: water_varnish_matte_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.water_varnish_matte_compatibility ALTER COLUMN id SET DEFAULT nextval('public.water_varnish_matte_compatibility_id_seq'::regclass);


--
-- Name: water_varnish_matte_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.water_varnish_matte_product ALTER COLUMN id SET DEFAULT nextval('public.water_varnish_matte_product_id_seq'::regclass);


--
-- Name: wear_resistant_gold_paper_skip_config id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wear_resistant_gold_paper_skip_config ALTER COLUMN id SET DEFAULT nextval('public.wear_resistant_gold_paper_skip_config_id_seq'::regclass);


--
-- Name: workflow_execution_log id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workflow_execution_log ALTER COLUMN id SET DEFAULT nextval('public.workflow_execution_log_id_seq'::regclass);


--
-- Name: yaguang_uv_oil_compatibility id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yaguang_uv_oil_compatibility ALTER COLUMN id SET DEFAULT nextval('public.yaguang_uv_oil_compatibility_id_seq'::regclass);


--
-- Name: yaguang_uv_oil_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yaguang_uv_oil_product ALTER COLUMN id SET DEFAULT nextval('public.yaguang_uv_oil_product_id_seq'::regclass);


--
-- Name: admin_change_records admin_change_records_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_records
    ADD CONSTRAINT admin_change_records_pkey PRIMARY KEY (id);


--
-- Name: admin_change_snapshots admin_change_snapshots_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_snapshots
    ADD CONSTRAINT admin_change_snapshots_pkey PRIMARY KEY (id);


--
-- Name: admin_operation_log admin_operation_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_operation_log
    ADD CONSTRAINT admin_operation_log_pkey PRIMARY KEY (id);


--
-- Name: ai_recommendation_history ai_recommendation_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ai_recommendation_history
    ADD CONSTRAINT ai_recommendation_history_pkey PRIMARY KEY (id);


--
-- Name: ai_search_history ai_search_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ai_search_history
    ADD CONSTRAINT ai_search_history_pkey PRIMARY KEY (id);


--
-- Name: cloth_paper_compatibility cloth_paper_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_compatibility
    ADD CONSTRAINT cloth_paper_compatibility_pkey PRIMARY KEY (id);


--
-- Name: hot_stamping_type_compatibility cloth_paper_compatibility_pkey_1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_type_compatibility
    ADD CONSTRAINT cloth_paper_compatibility_pkey_1 PRIMARY KEY (id);


--
-- Name: old)cloth_paper cloth_paper_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."old)cloth_paper"
    ADD CONSTRAINT cloth_paper_pkey PRIMARY KEY (id);


--
-- Name: cloth_paper_types cloth_paper_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_types
    ADD CONSTRAINT cloth_paper_types_pkey PRIMARY KEY (id);


--
-- Name: cloth_paper_types cloth_paper_types_type_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_types
    ADD CONSTRAINT cloth_paper_types_type_name_key UNIQUE (type_name);


--
-- Name: code_mapping code_mapping_p0_table_name_p0_row_id_target_type_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code_mapping
    ADD CONSTRAINT code_mapping_p0_table_name_p0_row_id_target_type_key UNIQUE (p0_table_name, p0_row_id, target_type);


--
-- Name: code_mapping code_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.code_mapping
    ADD CONSTRAINT code_mapping_pkey PRIMARY KEY (id);


--
-- Name: cold_foil_feasibility_rules cold_foil_feasibility_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_feasibility_rules
    ADD CONSTRAINT cold_foil_feasibility_rules_pkey PRIMARY KEY (id);


--
-- Name: cold_foil_type_options cold_foil_type_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_type_options
    ADD CONSTRAINT cold_foil_type_options_pkey PRIMARY KEY (id);


--
-- Name: embossing_feasibility_rules embossing_feasibility_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_feasibility_rules
    ADD CONSTRAINT embossing_feasibility_rules_pkey PRIMARY KEY (id);


--
-- Name: embossing_type_options embossing_type_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_type_options
    ADD CONSTRAINT embossing_type_options_pkey PRIMARY KEY (id);


--
-- Name: evaluation_session evaluation_session_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evaluation_session
    ADD CONSTRAINT evaluation_session_pkey PRIMARY KEY (id);


--
-- Name: old_film_butter_paper film_butter_paper_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_film_butter_paper
    ADD CONSTRAINT film_butter_paper_pkey PRIMARY KEY (id);


--
-- Name: gen_table_column gen_table_column_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gen_table_column
    ADD CONSTRAINT gen_table_column_pkey PRIMARY KEY (column_id);


--
-- Name: gen_table gen_table_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gen_table
    ADD CONSTRAINT gen_table_pkey PRIMARY KEY (table_id);


--
-- Name: global_parameters global_parameters_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.global_parameters
    ADD CONSTRAINT global_parameters_pkey PRIMARY KEY (id);


--
-- Name: gold_foil_type gold_foil_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gold_foil_type
    ADD CONSTRAINT gold_foil_type_pkey PRIMARY KEY (id);


--
-- Name: hot_stamping_compatibility hot_stamping_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_compatibility
    ADD CONSTRAINT hot_stamping_compatibility_pkey PRIMARY KEY (id);


--
-- Name: hot_stamping_pattern_base hot_stamping_pattern_base_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_pattern_base
    ADD CONSTRAINT hot_stamping_pattern_base_pkey PRIMARY KEY (id);


--
-- Name: hot_stamping_patternx_area_options hot_stamping_patternx_area_options_option_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_patternx_area_options
    ADD CONSTRAINT hot_stamping_patternx_area_options_option_name_key UNIQUE (option_name);


--
-- Name: hot_stamping_patternx_area_options hot_stamping_patternx_area_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_patternx_area_options
    ADD CONSTRAINT hot_stamping_patternx_area_options_pkey PRIMARY KEY (id);


--
-- Name: hot_stamping_type_compatibility hot_stamping_type_compatibility_unique; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_type_compatibility
    ADD CONSTRAINT hot_stamping_type_compatibility_unique UNIQUE (hot_stamping_type_id, product_name, paper_type);


--
-- Name: hot_stamping_type_options hot_stamping_type_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hot_stamping_type_options
    ADD CONSTRAINT hot_stamping_type_options_pkey PRIMARY KEY (id);


--
-- Name: import_snapshot import_snapshot_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.import_snapshot
    ADD CONSTRAINT import_snapshot_pkey PRIMARY KEY (id);


--
-- Name: interface_type_options interface_type_options_interface_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interface_type_options
    ADD CONSTRAINT interface_type_options_interface_name_key UNIQUE (interface_name);


--
-- Name: product_type_options interface_type_options_interface_name_key_1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_options
    ADD CONSTRAINT interface_type_options_interface_name_key_1 UNIQUE (product_name);


--
-- Name: pre_processing_steps_options interface_type_options_interface_name_key_1_1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pre_processing_steps_options
    ADD CONSTRAINT interface_type_options_interface_name_key_1_1 UNIQUE (pre_processing_steps_name);


--
-- Name: interface_type_options interface_type_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.interface_type_options
    ADD CONSTRAINT interface_type_options_pkey PRIMARY KEY (id);


--
-- Name: product_type_options interface_type_options_pkey_1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_options
    ADD CONSTRAINT interface_type_options_pkey_1 PRIMARY KEY (id);


--
-- Name: pre_processing_steps_options interface_type_options_pkey_1_1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pre_processing_steps_options
    ADD CONSTRAINT interface_type_options_pkey_1_1 PRIMARY KEY (id);


--
-- Name: knowledge_base_stats knowledge_base_stats_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_stats
    ADD CONSTRAINT knowledge_base_stats_pkey PRIMARY KEY (id);


--
-- Name: knowledge_base_status knowledge_base_status_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_status
    ADD CONSTRAINT knowledge_base_status_pkey PRIMARY KEY (id);


--
-- Name: knowledge_base_versions knowledge_base_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.knowledge_base_versions
    ADD CONSTRAINT knowledge_base_versions_pkey PRIMARY KEY (id);


--
-- Name: lamination_compatibility lamination_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_compatibility
    ADD CONSTRAINT lamination_compatibility_pkey PRIMARY KEY (id);


--
-- Name: lamination_feasibility_rules lamination_feasibility_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_feasibility_rules
    ADD CONSTRAINT lamination_feasibility_rules_pkey PRIMARY KEY (id);


--
-- Name: lamination_material_categories lamination_material_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_categories
    ADD CONSTRAINT lamination_material_categories_pkey PRIMARY KEY (id);


--
-- Name: lamination_material_compatibility lamination_material_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_compatibility
    ADD CONSTRAINT lamination_material_compatibility_pkey PRIMARY KEY (id);


--
-- Name: lamination_material_options lamination_material_options_material_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_options
    ADD CONSTRAINT lamination_material_options_material_code_key UNIQUE (material_code);


--
-- Name: lamination_material_options lamination_material_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_options
    ADD CONSTRAINT lamination_material_options_pkey PRIMARY KEY (id);


--
-- Name: lamination_material_products lamination_material_products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_products
    ADD CONSTRAINT lamination_material_products_pkey PRIMARY KEY (id);


--
-- Name: lamination_substrate_options lamination_substrate_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_substrate_options
    ADD CONSTRAINT lamination_substrate_options_pkey PRIMARY KEY (id);


--
-- Name: lamination_type_options lamination_type_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_type_options
    ADD CONSTRAINT lamination_type_options_pkey PRIMARY KEY (id);


--
-- Name: leo_gp_numbers leo_gp_numbers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leo_gp_numbers
    ADD CONSTRAINT leo_gp_numbers_pkey PRIMARY KEY (id);


--
-- Name: material_catalog material_catalog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_catalog
    ADD CONSTRAINT material_catalog_pkey PRIMARY KEY (id);


--
-- Name: material_process_compatibility material_process_compatibility_material_id_operation_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_process_compatibility
    ADD CONSTRAINT material_process_compatibility_material_id_operation_name_key UNIQUE (material_id, operation_name);


--
-- Name: material_process_compatibility material_process_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_process_compatibility
    ADD CONSTRAINT material_process_compatibility_pkey PRIMARY KEY (id);


--
-- Name: old_matte_lamination matte_lamination_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_matte_lamination
    ADD CONSTRAINT matte_lamination_pkey PRIMARY KEY (id);


--
-- Name: notice_dictionary notice_dictionary_notice_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notice_dictionary
    ADD CONSTRAINT notice_dictionary_notice_code_key UNIQUE (notice_code);


--
-- Name: notice_dictionary notice_dictionary_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notice_dictionary
    ADD CONSTRAINT notice_dictionary_pkey PRIMARY KEY (id);


--
-- Name: old_oil_and_uv_types oil_and_uv_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_oil_and_uv_types
    ADD CONSTRAINT oil_and_uv_types_pkey PRIMARY KEY (id);


--
-- Name: paper_material_categories paper_material_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.paper_material_categories
    ADD CONSTRAINT paper_material_categories_pkey PRIMARY KEY (id);


--
-- Name: old_pattern pattern_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_pattern
    ADD CONSTRAINT pattern_pkey PRIMARY KEY (id);


--
-- Name: permissions permissions_permission_key_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_permission_key_key UNIQUE (permission_key);


--
-- Name: permissions permissions_permission_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_permission_name_key UNIQUE (permission_name);


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: post_processing_leduvglitter post_processing_leduvglitter_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_leduvglitter
    ADD CONSTRAINT post_processing_leduvglitter_pk PRIMARY KEY (id);


--
-- Name: post_processing_options post_processing_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_options
    ADD CONSTRAINT post_processing_options_pkey PRIMARY KEY (id);


--
-- Name: post_processing_options post_processing_options_step_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_options
    ADD CONSTRAINT post_processing_options_step_name_key UNIQUE (step_name);


--
-- Name: post_processing_print post_processing_print_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_print
    ADD CONSTRAINT post_processing_print_pk PRIMARY KEY (id);


--
-- Name: post_processing_print_uv post_processing_print_uv_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.post_processing_print_uv
    ADD CONSTRAINT post_processing_print_uv_pk PRIMARY KEY (id);


--
-- Name: pre_processing_steps pre_processing_steps_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pre_processing_steps
    ADD CONSTRAINT pre_processing_steps_pkey PRIMARY KEY (id);


--
-- Name: pricing pricing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pricing
    ADD CONSTRAINT pricing_pkey PRIMARY KEY (id);


--
-- Name: printing_feasibility_rules printing_feasibility_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_feasibility_rules
    ADD CONSTRAINT printing_feasibility_rules_pkey PRIMARY KEY (id);


--
-- Name: printing_material_categories printing_material_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_material_categories
    ADD CONSTRAINT printing_material_categories_pkey PRIMARY KEY (id);


--
-- Name: printing_process_options printing_process_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_process_options
    ADD CONSTRAINT printing_process_options_pkey PRIMARY KEY (id);


--
-- Name: printing_substrate_options printing_substrate_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_substrate_options
    ADD CONSTRAINT printing_substrate_options_pkey PRIMARY KEY (id);


--
-- Name: processing_after_ironing processing_after_ironing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.processing_after_ironing
    ADD CONSTRAINT processing_after_ironing_pkey PRIMARY KEY (id);


--
-- Name: product_type_sort_config product_type_sort_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_sort_config
    ADD CONSTRAINT product_type_sort_config_pkey PRIMARY KEY (id);


--
-- Name: product_type_sort_config product_type_sort_config_product_type_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_sort_config
    ADD CONSTRAINT product_type_sort_config_product_type_id_key UNIQUE (product_type_id);


--
-- Name: production_capacity production_capacity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_capacity
    ADD CONSTRAINT production_capacity_pkey PRIMARY KEY (id);


--
-- Name: production_resource_groups production_resource_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_resource_groups
    ADD CONSTRAINT production_resource_groups_pkey PRIMARY KEY (code);


--
-- Name: production_resources production_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_resources
    ADD CONSTRAINT production_resources_pkey PRIMARY KEY (id);


--
-- Name: production_rules production_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.production_rules
    ADD CONSTRAINT production_rules_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: reference_coating_surface reference_coating_surface_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_coating_surface
    ADD CONSTRAINT reference_coating_surface_pkey PRIMARY KEY (id);


--
-- Name: reference_ink_surface reference_ink_surface_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_ink_surface
    ADD CONSTRAINT reference_ink_surface_pkey PRIMARY KEY (id);


--
-- Name: reference_paper_surface reference_paper_surface_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_paper_surface
    ADD CONSTRAINT reference_paper_surface_pkey PRIMARY KEY (id);


--
-- Name: reference_product_family reference_product_family_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_product_family
    ADD CONSTRAINT reference_product_family_pkey PRIMARY KEY (id);


--
-- Name: reference_writing_function reference_writing_function_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reference_writing_function
    ADD CONSTRAINT reference_writing_function_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_group_capacity rg_resource_group_capacity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_capacity
    ADD CONSTRAINT rg_resource_group_capacity_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_group_detail rg_resource_group_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_detail
    ADD CONSTRAINT rg_resource_group_detail_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_group rg_resource_group_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group
    ADD CONSTRAINT rg_resource_group_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_group rg_resource_group_resource_group_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group
    ADD CONSTRAINT rg_resource_group_resource_group_code_key UNIQUE (resource_group_code);


--
-- Name: rg_resource_group_task_map rg_resource_group_task_map_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_task_map
    ADD CONSTRAINT rg_resource_group_task_map_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_group_task_map rg_resource_group_task_map_resource_group_id_task_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_task_map
    ADD CONSTRAINT rg_resource_group_task_map_resource_group_id_task_code_key UNIQUE (resource_group_id, task_code);


--
-- Name: rg_resource_rule_condition rg_resource_rule_condition_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_condition
    ADD CONSTRAINT rg_resource_rule_condition_pkey PRIMARY KEY (id);


--
-- Name: rg_resource_rule_header rg_resource_rule_header_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_header
    ADD CONSTRAINT rg_resource_rule_header_pkey PRIMARY KEY (id);


--
-- Name: rg_task_definition rg_task_definition_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_task_definition
    ADD CONSTRAINT rg_task_definition_pkey PRIMARY KEY (id);


--
-- Name: rg_task_definition rg_task_definition_task_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_task_definition
    ADD CONSTRAINT rg_task_definition_task_code_key UNIQUE (task_code);


--
-- Name: rg_work_center rg_work_center_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_work_center
    ADD CONSTRAINT rg_work_center_pkey PRIMARY KEY (id);


--
-- Name: rg_work_center rg_work_center_work_center_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_work_center
    ADD CONSTRAINT rg_work_center_work_center_code_key UNIQUE (work_center_code);


--
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: roles roles_role_key_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_role_key_key UNIQUE (role_key);


--
-- Name: roles roles_role_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_role_name_key UNIQUE (role_name);


--
-- Name: rule_evaluation_log rule_evaluation_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rule_evaluation_log
    ADD CONSTRAINT rule_evaluation_log_pkey PRIMARY KEY (id);


--
-- Name: screen_printing_uv_categories screen_printing_uv_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.screen_printing_uv_categories
    ADD CONSTRAINT screen_printing_uv_categories_pkey PRIMARY KEY (id);


--
-- Name: series_priority_rule_item series_priority_rule_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule_item
    ADD CONSTRAINT series_priority_rule_item_pkey PRIMARY KEY (id);


--
-- Name: series_priority_rule series_priority_rule_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule
    ADD CONSTRAINT series_priority_rule_pkey PRIMARY KEY (id);


--
-- Name: series_priority_rule series_priority_rule_rule_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule
    ADD CONSTRAINT series_priority_rule_rule_code_key UNIQUE (rule_code);


--
-- Name: silicone_coarse_uv_compatibility silicone_coarse_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_coarse_uv_compatibility
    ADD CONSTRAINT silicone_coarse_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_coarse_uv_product silicone_coarse_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_coarse_uv_product
    ADD CONSTRAINT silicone_coarse_uv_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_glittering_star_compatibility silicone_glittering_star_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glittering_star_compatibility
    ADD CONSTRAINT silicone_glittering_star_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_glittering_star_product silicone_glittering_star_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glittering_star_product
    ADD CONSTRAINT silicone_glittering_star_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_glow_ink_compatibility silicone_glow_ink_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glow_ink_compatibility
    ADD CONSTRAINT silicone_glow_ink_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_glow_ink_product silicone_glow_ink_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glow_ink_product
    ADD CONSTRAINT silicone_glow_ink_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_led_uv_spray_compatibility silicone_led_uv_spray_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_led_uv_spray_compatibility
    ADD CONSTRAINT silicone_led_uv_spray_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_led_uv_spray_product silicone_led_uv_spray_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_led_uv_spray_product
    ADD CONSTRAINT silicone_led_uv_spray_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_mica_pearl_compatibility silicone_mica_pearl_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_mica_pearl_compatibility
    ADD CONSTRAINT silicone_mica_pearl_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_mica_pearl_product silicone_mica_pearl_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_mica_pearl_product
    ADD CONSTRAINT silicone_mica_pearl_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_orange_peel_uv_compatibility silicone_orange_peel_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_orange_peel_uv_compatibility
    ADD CONSTRAINT silicone_orange_peel_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_orange_peel_uv_product silicone_orange_peel_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_orange_peel_uv_product
    ADD CONSTRAINT silicone_orange_peel_uv_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_sandblast_uv_compatibility silicone_sandblast_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_sandblast_uv_compatibility
    ADD CONSTRAINT silicone_sandblast_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_sandblast_uv_product silicone_sandblast_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_sandblast_uv_product
    ADD CONSTRAINT silicone_sandblast_uv_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_screen_uv_compatibility silicone_screen_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_screen_uv_compatibility
    ADD CONSTRAINT silicone_screen_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_screen_uv_product silicone_screen_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_screen_uv_product
    ADD CONSTRAINT silicone_screen_uv_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_water_base_transparent_compatibility silicone_water_base_transparent_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_water_base_transparent_compatibility
    ADD CONSTRAINT silicone_water_base_transparent_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_water_base_transparent_product silicone_water_base_transparent_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_water_base_transparent_product
    ADD CONSTRAINT silicone_water_base_transparent_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_watercolor_ink_compatibility silicone_watercolor_ink_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_watercolor_ink_compatibility
    ADD CONSTRAINT silicone_watercolor_ink_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_watercolor_ink_product silicone_watercolor_ink_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_watercolor_ink_product
    ADD CONSTRAINT silicone_watercolor_ink_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_white_uv_compatibility silicone_white_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_white_uv_compatibility
    ADD CONSTRAINT silicone_white_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_white_uv_product silicone_white_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_white_uv_product
    ADD CONSTRAINT silicone_white_uv_product_pkey PRIMARY KEY (id);


--
-- Name: silicone_wrinkle_uv_compatibility silicone_wrinkle_uv_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_wrinkle_uv_compatibility
    ADD CONSTRAINT silicone_wrinkle_uv_compatibility_pkey PRIMARY KEY (id);


--
-- Name: silicone_wrinkle_uv_product silicone_wrinkle_uv_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_wrinkle_uv_product
    ADD CONSTRAINT silicone_wrinkle_uv_product_pkey PRIMARY KEY (id);


--
-- Name: specifications specifications_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.specifications
    ADD CONSTRAINT specifications_pkey PRIMARY KEY (id);


--
-- Name: staging_capacity staging_capacity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_capacity
    ADD CONSTRAINT staging_capacity_pkey PRIMARY KEY (id);


--
-- Name: staging_process_categories staging_process_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_process_categories
    ADD CONSTRAINT staging_process_categories_pkey PRIMARY KEY (code);


--
-- Name: staging_resource_groups staging_resource_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_resource_groups
    ADD CONSTRAINT staging_resource_groups_pkey PRIMARY KEY (id);


--
-- Name: staging_resources staging_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_resources
    ADD CONSTRAINT staging_resources_pkey PRIMARY KEY (id);


--
-- Name: staging_rules staging_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.staging_rules
    ADD CONSTRAINT staging_rules_pkey PRIMARY KEY (id);


--
-- Name: sys_announcement sys_announcement_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_announcement
    ADD CONSTRAINT sys_announcement_pkey PRIMARY KEY (id);


--
-- Name: sys_announcement_send sys_announcement_send_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_announcement_send
    ADD CONSTRAINT sys_announcement_send_pkey PRIMARY KEY (id);


--
-- Name: sys_config sys_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_config
    ADD CONSTRAINT sys_config_pkey PRIMARY KEY (config_id);


--
-- Name: sys_dept sys_dept_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dept
    ADD CONSTRAINT sys_dept_pkey PRIMARY KEY (dept_id);


--
-- Name: sys_dict_data sys_dict_data_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dict_data
    ADD CONSTRAINT sys_dict_data_pkey PRIMARY KEY (dict_code);


--
-- Name: sys_dict_type sys_dict_type_dict_type_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dict_type
    ADD CONSTRAINT sys_dict_type_dict_type_key UNIQUE (dict_type);


--
-- Name: sys_dict_type sys_dict_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_dict_type
    ADD CONSTRAINT sys_dict_type_pkey PRIMARY KEY (dict_id);


--
-- Name: sys_job_log sys_job_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_job_log
    ADD CONSTRAINT sys_job_log_pkey PRIMARY KEY (job_log_id);


--
-- Name: sys_job sys_job_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_job
    ADD CONSTRAINT sys_job_pkey PRIMARY KEY (job_id);


--
-- Name: sys_logininfor sys_logininfor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_logininfor
    ADD CONSTRAINT sys_logininfor_pkey PRIMARY KEY (info_id);


--
-- Name: sys_menu sys_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_menu
    ADD CONSTRAINT sys_menu_pkey PRIMARY KEY (menu_id);


--
-- Name: sys_notice sys_notice_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_notice
    ADD CONSTRAINT sys_notice_pkey PRIMARY KEY (notice_id);


--
-- Name: sys_oper_log sys_oper_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_oper_log
    ADD CONSTRAINT sys_oper_log_pkey PRIMARY KEY (oper_id);


--
-- Name: sys_post sys_post_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_post
    ADD CONSTRAINT sys_post_pkey PRIMARY KEY (post_id);


--
-- Name: sys_role_menu sys_role_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_role_menu
    ADD CONSTRAINT sys_role_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- Name: sys_role sys_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_role
    ADD CONSTRAINT sys_role_pkey PRIMARY KEY (role_id);


--
-- Name: sys_user_online sys_user_online_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_user_online
    ADD CONSTRAINT sys_user_online_pkey PRIMARY KEY (sessionid);


--
-- Name: sys_user sys_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT sys_user_pkey PRIMARY KEY (user_id);


--
-- Name: sys_user_post sys_user_post_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_user_post
    ADD CONSTRAINT sys_user_post_pkey PRIMARY KEY (user_id, post_id);


--
-- Name: sys_user_role sys_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_user_role
    ADD CONSTRAINT sys_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: system_common_param_definition system_common_param_definition_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_common_param_definition
    ADD CONSTRAINT system_common_param_definition_pkey PRIMARY KEY (id);


--
-- Name: system_common_param_input system_common_param_input_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_common_param_input
    ADD CONSTRAINT system_common_param_input_pkey PRIMARY KEY (id);


--
-- Name: system_content system_content_content_key_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_content
    ADD CONSTRAINT system_content_content_key_key UNIQUE (content_key);


--
-- Name: system_content system_content_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_content
    ADD CONSTRAINT system_content_pkey PRIMARY KEY (id);


--
-- Name: sys_announcement_send uk_annt_user; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_announcement_send
    ADD CONSTRAINT uk_annt_user UNIQUE (annt_id, user_id);


--
-- Name: user_match_preferences unique_user_field_value; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_match_preferences
    ADD CONSTRAINT unique_user_field_value UNIQUE (user_id, field_name, field_value);


--
-- Name: lamination_substrate_options uq_lamination_substrate_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_substrate_options
    ADD CONSTRAINT uq_lamination_substrate_name UNIQUE (substrate_name);


--
-- Name: lamination_type_options uq_lamination_type_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_type_options
    ADD CONSTRAINT uq_lamination_type_code UNIQUE (type_code);


--
-- Name: lamination_type_options uq_lamination_type_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_type_options
    ADD CONSTRAINT uq_lamination_type_name UNIQUE (type_name);


--
-- Name: material_catalog uq_material_catalog_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_catalog
    ADD CONSTRAINT uq_material_catalog_code UNIQUE (material_code);


--
-- Name: printing_material_categories uq_printing_material_cat_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_material_categories
    ADD CONSTRAINT uq_printing_material_cat_code UNIQUE (category_code);


--
-- Name: printing_material_categories uq_printing_material_cat_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_material_categories
    ADD CONSTRAINT uq_printing_material_cat_name UNIQUE (category_name);


--
-- Name: printing_process_options uq_printing_process_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_process_options
    ADD CONSTRAINT uq_printing_process_code UNIQUE (process_code);


--
-- Name: printing_process_options uq_printing_process_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_process_options
    ADD CONSTRAINT uq_printing_process_name UNIQUE (process_name);


--
-- Name: printing_substrate_options uq_printing_substrate_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_substrate_options
    ADD CONSTRAINT uq_printing_substrate_name UNIQUE (substrate_name);


--
-- Name: screen_printing_uv_categories uq_screen_printing_uv_cat_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.screen_printing_uv_categories
    ADD CONSTRAINT uq_screen_printing_uv_cat_code UNIQUE (category_code);


--
-- Name: screen_printing_uv_categories uq_screen_printing_uv_cat_name; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.screen_printing_uv_categories
    ADD CONSTRAINT uq_screen_printing_uv_cat_name UNIQUE (category_name);


--
-- Name: user_match_preferences user_match_preferences_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_match_preferences
    ADD CONSTRAINT user_match_preferences_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: uv_effect_feasibility_rules uv_effect_feasibility_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_feasibility_rules
    ADD CONSTRAINT uv_effect_feasibility_rules_pkey PRIMARY KEY (id);


--
-- Name: uv_effect_options uv_effect_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_options
    ADD CONSTRAINT uv_effect_options_pkey PRIMARY KEY (id);


--
-- Name: uv_oil_matte_compatibility uv_oil_matte_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_oil_matte_compatibility
    ADD CONSTRAINT uv_oil_matte_compatibility_pkey PRIMARY KEY (id);


--
-- Name: uv_oil_matte_product uv_oil_matte_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_oil_matte_product
    ADD CONSTRAINT uv_oil_matte_product_pkey PRIMARY KEY (id);


--
-- Name: water_varnish_matte_compatibility water_varnish_matte_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.water_varnish_matte_compatibility
    ADD CONSTRAINT water_varnish_matte_compatibility_pkey PRIMARY KEY (id);


--
-- Name: water_varnish_matte_product water_varnish_matte_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.water_varnish_matte_product
    ADD CONSTRAINT water_varnish_matte_product_pkey PRIMARY KEY (id);


--
-- Name: wear_resistant_gold_paper_skip_config wear_resistant_gold_paper_skip_conf_hot_stamping_paper_type_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wear_resistant_gold_paper_skip_config
    ADD CONSTRAINT wear_resistant_gold_paper_skip_conf_hot_stamping_paper_type_key UNIQUE (hot_stamping_paper_type);


--
-- Name: wear_resistant_gold_paper_skip_config wear_resistant_gold_paper_skip_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wear_resistant_gold_paper_skip_config
    ADD CONSTRAINT wear_resistant_gold_paper_skip_config_pkey PRIMARY KEY (id);


--
-- Name: workflow_execution_log workflow_execution_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workflow_execution_log
    ADD CONSTRAINT workflow_execution_log_pkey PRIMARY KEY (id);


--
-- Name: workflow_execution_log workflow_execution_log_run_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.workflow_execution_log
    ADD CONSTRAINT workflow_execution_log_run_id_key UNIQUE (run_id);


--
-- Name: yaguang_uv_oil_compatibility yaguang_uv_oil_compatibility_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yaguang_uv_oil_compatibility
    ADD CONSTRAINT yaguang_uv_oil_compatibility_pkey PRIMARY KEY (id);


--
-- Name: yaguang_uv_oil_compatibility yaguang_uv_oil_compatibility_product_id_step_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yaguang_uv_oil_compatibility
    ADD CONSTRAINT yaguang_uv_oil_compatibility_product_id_step_name_key UNIQUE (product_id, step_name);


--
-- Name: yaguang_uv_oil_product yaguang_uv_oil_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yaguang_uv_oil_product
    ADD CONSTRAINT yaguang_uv_oil_product_pkey PRIMARY KEY (id);


--
-- Name: cloth_paper_compatibility_unique_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX cloth_paper_compatibility_unique_idx ON public.cloth_paper_compatibility USING btree (product_name, cloth_paper_type_id, COALESCE(paper_type, ''::character varying));


--
-- Name: hot_stamping_patternx_area_compatibility_unique_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX hot_stamping_patternx_area_compatibility_unique_idx ON public.hot_stamping_patternx_area_compatibility USING btree (product_name, hot_stamping_patternx_area_id, COALESCE(paper_type, ''::character varying));


--
-- Name: idx_admin_operation_log_module; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_admin_operation_log_module ON public.admin_operation_log USING btree (module);


--
-- Name: idx_admin_operation_log_operated_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_admin_operation_log_operated_at ON public.admin_operation_log USING btree (operated_at DESC);


--
-- Name: idx_admin_operation_log_operation_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_admin_operation_log_operation_type ON public.admin_operation_log USING btree (operation_type);


--
-- Name: idx_admin_operation_log_operator_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_admin_operation_log_operator_id ON public.admin_operation_log USING btree (operator_id);


--
-- Name: idx_annt_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_annt_id ON public.sys_announcement_send USING btree (annt_id);


--
-- Name: idx_base_sort_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_base_sort_order ON public.hot_stamping_pattern_base USING btree (sort_order);


--
-- Name: idx_cffr_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cffr_product_type ON public.cold_foil_feasibility_rules USING btree (product_type_id);


--
-- Name: idx_cffr_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cffr_status ON public.cold_foil_feasibility_rules USING btree (feasibility_status);


--
-- Name: idx_cffr_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cffr_type ON public.cold_foil_feasibility_rules USING btree (type_id);


--
-- Name: idx_change_records_created_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_change_records_created_at ON public.admin_change_records USING btree (created_at DESC);


--
-- Name: idx_change_records_entity; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_change_records_entity ON public.admin_change_records USING btree (entity_type, entity_id);


--
-- Name: idx_change_records_operator; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_change_records_operator ON public.admin_change_records USING btree (operator_id);


--
-- Name: idx_change_records_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_change_records_status ON public.admin_change_records USING btree (status);


--
-- Name: idx_cloth_paper_compatibility_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cloth_paper_compatibility_notice_ids ON public.cloth_paper_compatibility USING gin (notice_ids);


--
-- Name: idx_cloth_paper_compatibility_product_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cloth_paper_compatibility_product_name ON public.cloth_paper_compatibility USING btree (product_name);


--
-- Name: idx_cloth_paper_compatibility_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cloth_paper_compatibility_type ON public.cloth_paper_compatibility USING btree (cloth_paper_type_id);


--
-- Name: idx_cloth_paper_compatibility_type_1; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cloth_paper_compatibility_type_1 ON public.hot_stamping_type_compatibility USING btree (hot_stamping_type_id);


--
-- Name: idx_cloth_paper_compatibility_type_1_1; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cloth_paper_compatibility_type_1_1 ON public.hot_stamping_patternx_area_compatibility USING btree (hot_stamping_patternx_area_id);


--
-- Name: idx_code_mapping_match; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_code_mapping_match ON public.code_mapping USING btree (match_type);


--
-- Name: idx_code_mapping_p0; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_code_mapping_p0 ON public.code_mapping USING btree (p0_table_name, p0_row_id);


--
-- Name: idx_code_mapping_target; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_code_mapping_target ON public.code_mapping USING btree (target_type, target_id);


--
-- Name: idx_compatibility_compatibility; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_compatibility_compatibility ON public.hot_stamping_compatibility USING btree (compatibility);


--
-- Name: idx_compatibility_hot_stamping_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_compatibility_hot_stamping_type ON public.hot_stamping_compatibility USING btree (hot_stamping_type);


--
-- Name: idx_compatibility_paper_performance; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_compatibility_paper_performance ON public.hot_stamping_compatibility USING btree (paper_performance);


--
-- Name: idx_compatibility_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_compatibility_product_type ON public.hot_stamping_compatibility USING btree (product_type);


--
-- Name: idx_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_create_time ON public.sys_announcement USING btree (create_time);


--
-- Name: idx_efr_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_efr_product_type ON public.embossing_feasibility_rules USING btree (product_type_id);


--
-- Name: idx_efr_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_efr_status ON public.embossing_feasibility_rules USING btree (feasibility_status);


--
-- Name: idx_efr_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_efr_type ON public.embossing_feasibility_rules USING btree (type_id);


--
-- Name: idx_end_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_end_time ON public.sys_announcement USING btree (end_time);


--
-- Name: idx_global_params_session; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_global_params_session ON public.global_parameters USING btree (session_id);


--
-- Name: idx_hot_stamping_compatibility_lamination_material; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_compatibility_lamination_material ON public.hot_stamping_compatibility USING btree (lamination_material_id);


--
-- Name: idx_hot_stamping_compatibility_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_compatibility_notice_ids ON public.hot_stamping_compatibility USING gin (notice_ids);


--
-- Name: idx_hot_stamping_compatibility_paper_performance; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_compatibility_paper_performance ON public.hot_stamping_compatibility USING btree (paper_performance);


--
-- Name: idx_hot_stamping_compatibility_post_lamination; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_compatibility_post_lamination ON public.hot_stamping_compatibility USING btree (post_processing_step_id, lamination_material_id) WHERE ((post_processing_step_id IS NOT NULL) AND (lamination_material_id IS NOT NULL));


--
-- Name: idx_hot_stamping_patternx_area_compatibility_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_patternx_area_compatibility_notice_ids ON public.hot_stamping_patternx_area_compatibility USING gin (notice_ids);


--
-- Name: idx_hot_stamping_type_compatibility_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hot_stamping_type_compatibility_notice_ids ON public.hot_stamping_type_compatibility USING gin (notice_ids);


--
-- Name: idx_hsc_hot_stamping_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_hot_stamping_type ON public.hot_stamping_compatibility USING btree (hot_stamping_type_id, paper_performance, compatibility) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_hsc_paper_performance; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_paper_performance ON public.hot_stamping_compatibility USING btree (paper_performance, compatibility);


--
-- Name: idx_hsc_pattern_compat; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_pattern_compat ON public.hot_stamping_compatibility USING btree (pattern_characteristic_id, paper_performance, compatibility) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_hsc_product_type_compat; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_product_type_compat ON public.hot_stamping_compatibility USING btree (product_type_id, paper_performance, compatibility) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_hsc_product_type_optimized; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_product_type_optimized ON public.hot_stamping_compatibility USING btree (product_type_id, compatibility, paper_performance) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_hsc_skip_wear_resistant_mapping; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hsc_skip_wear_resistant_mapping ON public.hot_stamping_compatibility USING btree (skip_wear_resistant_mapping);


--
-- Name: idx_import_snapshot_operated_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_import_snapshot_operated_at ON public.import_snapshot USING btree (operated_at);


--
-- Name: idx_import_snapshot_table_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_import_snapshot_table_name ON public.import_snapshot USING btree (table_name);


--
-- Name: idx_lamination_compatibility_foil_series; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lamination_compatibility_foil_series ON public.lamination_compatibility USING btree (foil_series, post_processing_step_id, lamination_material_id, compatibility) WHERE ((foil_series IS NOT NULL) AND ((foil_series)::text <> ''::text));


--
-- Name: idx_lamination_compatibility_model_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lamination_compatibility_model_number ON public.lamination_compatibility USING btree (model_number, post_processing_step_id, lamination_material_id, compatibility) WHERE ((model_number IS NOT NULL) AND ((model_number)::text <> ''::text));


--
-- Name: idx_lamination_compatibility_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lamination_compatibility_notice_ids ON public.lamination_compatibility USING gin (notice_ids);


--
-- Name: idx_lamination_material_options_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lamination_material_options_id ON public.lamination_material_options USING btree (id);


--
-- Name: idx_lc_compatibility_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_compatibility_status ON public.lamination_compatibility USING btree (compatibility, foil_series, model_number);


--
-- Name: idx_lc_covering_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_covering_index ON public.lamination_compatibility USING btree (foil_series, model_number, post_processing_step_id, lamination_material_id, compatibility, product_type, is_jiehuo) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_lc_foil_series_composite; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_foil_series_composite ON public.lamination_compatibility USING btree (foil_series, post_processing_step_id, lamination_material_id, compatibility, product_type) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_lc_lamination_material_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_lamination_material_id ON public.lamination_compatibility USING btree (lamination_material_id, compatibility) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_lc_model_number_composite; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_model_number_composite ON public.lamination_compatibility USING btree (model_number, post_processing_step_id, lamination_material_id, compatibility, product_type) WHERE ((model_number IS NOT NULL) AND ((model_number)::text <> ''::text) AND (compatibility = 'V'::bpchar));


--
-- Name: idx_lc_post_processing_step; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_post_processing_step ON public.lamination_compatibility USING btree (post_processing_step_id, compatibility) WHERE (compatibility = 'V'::bpchar);


--
-- Name: idx_lc_product_type_null; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lc_product_type_null ON public.lamination_compatibility USING btree (foil_series, model_number, post_processing_step_id, lamination_material_id, compatibility) WHERE ((product_type IS NULL) AND (compatibility = 'V'::bpchar));


--
-- Name: idx_leo_gp_numbers_company; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leo_gp_numbers_company ON public.leo_gp_numbers USING btree (company_number, gp_no);


--
-- Name: idx_leo_gp_numbers_project_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_leo_gp_numbers_project_id ON public.leo_gp_numbers USING btree (project_id);


--
-- Name: idx_lfr_gsm; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lfr_gsm ON public.lamination_feasibility_rules USING btree (gsm_min, gsm_max);


--
-- Name: idx_lfr_lamination_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lfr_lamination_type ON public.lamination_feasibility_rules USING btree (lamination_type_id);


--
-- Name: idx_lfr_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lfr_product_type ON public.lamination_feasibility_rules USING btree (product_type_id);


--
-- Name: idx_lfr_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lfr_status ON public.lamination_feasibility_rules USING btree (feasibility_status);


--
-- Name: idx_lfr_substrate; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_lfr_substrate ON public.lamination_feasibility_rules USING btree (substrate_id);


--
-- Name: idx_mc_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mc_active ON public.material_catalog USING btree (is_active);


--
-- Name: idx_mc_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mc_category ON public.material_catalog USING btree (category_type, category_id);


--
-- Name: idx_mc_category_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mc_category_type ON public.material_catalog USING btree (category_type);


--
-- Name: idx_mc_supplier; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mc_supplier ON public.material_catalog USING btree (supplier);


--
-- Name: idx_mpc_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mpc_category ON public.material_process_compatibility USING btree (operation_category);


--
-- Name: idx_mpc_material; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mpc_material ON public.material_process_compatibility USING btree (material_id);


--
-- Name: idx_mpc_operation; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mpc_operation ON public.material_process_compatibility USING btree (operation_name);


--
-- Name: idx_mpc_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mpc_status ON public.material_process_compatibility USING btree (compatibility_status);


--
-- Name: idx_msg_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_msg_category ON public.sys_announcement USING btree (msg_category);


--
-- Name: idx_notice_dictionary_category; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notice_dictionary_category ON public.notice_dictionary USING btree (category);


--
-- Name: idx_notice_dictionary_is_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notice_dictionary_is_active ON public.notice_dictionary USING btree (is_active);


--
-- Name: idx_notice_dictionary_priority; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notice_dictionary_priority ON public.notice_dictionary USING btree (priority DESC);


--
-- Name: idx_pattern_project_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pattern_project_id ON public.old_pattern USING btree (project_id);


--
-- Name: idx_pattern_sort_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pattern_sort_order ON public.hot_stamping_patternx_area_options USING btree (sort_order);


--
-- Name: idx_permissions_parent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_parent_id ON public.permissions USING btree (parent_id);


--
-- Name: idx_permissions_permission_key; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_permission_key ON public.permissions USING btree (permission_key);


--
-- Name: idx_permissions_permission_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_permission_type ON public.permissions USING btree (permission_type);


--
-- Name: idx_permissions_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_permissions_status ON public.permissions USING btree (status);


--
-- Name: idx_pfr_gsm; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pfr_gsm ON public.printing_feasibility_rules USING btree (gsm_min, gsm_max);


--
-- Name: idx_pfr_process; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pfr_process ON public.printing_feasibility_rules USING btree (process_id);


--
-- Name: idx_pfr_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pfr_product_type ON public.printing_feasibility_rules USING btree (product_type_id);


--
-- Name: idx_pfr_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pfr_status ON public.printing_feasibility_rules USING btree (feasibility_status);


--
-- Name: idx_pfr_substrate; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pfr_substrate ON public.printing_feasibility_rules USING btree (substrate_id);


--
-- Name: idx_post_processing_leduvglitter_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_leduvglitter_notice_ids ON public.post_processing_leduvglitter USING gin (notice_ids);


--
-- Name: idx_post_processing_leduvglitter_paper_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_leduvglitter_paper_type ON public.post_processing_leduvglitter USING btree (paper_type);


--
-- Name: idx_post_processing_options_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_options_id ON public.post_processing_options USING btree (id);


--
-- Name: idx_post_processing_print_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_notice_ids ON public.post_processing_print USING gin (notice_ids);


--
-- Name: idx_post_processing_print_uv_company_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_company_number ON public.post_processing_print_uv USING btree (company_number);


--
-- Name: idx_post_processing_print_uv_compatibility_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_compatibility_status ON public.post_processing_print_uv USING btree (compatibility_status);


--
-- Name: idx_post_processing_print_uv_composite; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_composite ON public.post_processing_print_uv USING btree (company_number, product_name, product_model_number, paper_type);


--
-- Name: idx_post_processing_print_uv_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_notice_ids ON public.post_processing_print_uv USING gin (notice_ids);


--
-- Name: idx_post_processing_print_uv_paper_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_paper_type ON public.post_processing_print_uv USING btree (paper_type);


--
-- Name: idx_post_processing_print_uv_product_model_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_product_model_number ON public.post_processing_print_uv USING btree (product_model_number);


--
-- Name: idx_post_processing_print_uv_product_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_post_processing_print_uv_product_name ON public.post_processing_print_uv USING btree (product_name);


--
-- Name: idx_pps_series_name_product_step_optimized; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pps_series_name_product_step_optimized ON public.pre_processing_steps USING btree (series_name, product_id, step_id, is_active, paper_type, status) WHERE ((series_name IS NOT NULL) AND (product_id IS NOT NULL) AND (is_active = true) AND ((status)::text = 'V'::text));


--
-- Name: idx_pps_series_name_step_optimized; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pps_series_name_step_optimized ON public.pre_processing_steps USING btree (series_name, step_id, is_active, paper_type, status, product_id) WHERE ((series_name IS NOT NULL) AND (is_active = true) AND ((status)::text = 'V'::text) AND (product_id IS NULL));


--
-- Name: idx_pps_step_id_covering; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pps_step_id_covering ON public.pre_processing_steps USING btree (step_id, is_active, paper_type, status, series_name, product_id) WHERE ((is_active = true) AND ((status)::text = 'V'::text));


--
-- Name: idx_pps_step_id_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pps_step_id_status ON public.pre_processing_steps USING btree (step_id, is_active, status, paper_type) WHERE (is_active = true);


--
-- Name: idx_pre_processing_steps_notice_ids; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pre_processing_steps_notice_ids ON public.pre_processing_steps USING gin (notice_ids);


--
-- Name: idx_pricing_price; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pricing_price ON public.pricing USING btree (price) WHERE (price IS NOT NULL);


--
-- Name: idx_pricing_project_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pricing_project_id ON public.pricing USING btree (project_id);


--
-- Name: idx_pricing_project_id_price; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pricing_project_id_price ON public.pricing USING btree (project_id, price) WHERE (price IS NOT NULL);


--
-- Name: idx_pricing_project_price; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pricing_project_price ON public.pricing USING btree (project_id, price) WHERE (price IS NOT NULL);


--
-- Name: idx_processing_after_ironing_project_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_processing_after_ironing_project_id ON public.processing_after_ironing USING btree (project_id);


--
-- Name: idx_product_type_sort_config_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_type_sort_config_active ON public.product_type_sort_config USING btree (is_active);


--
-- Name: idx_product_type_sort_config_product_type_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_product_type_sort_config_product_type_id ON public.product_type_sort_config USING btree (product_type_id);


--
-- Name: idx_production_cap_review_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_production_cap_review_status ON public.production_capacity USING btree (review_status);


--
-- Name: idx_production_rg_review_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_production_rg_review_status ON public.production_resource_groups USING btree (review_status);


--
-- Name: idx_production_rules_review_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_production_rules_review_status ON public.production_rules USING btree (review_status);


--
-- Name: idx_products_covering_model_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_covering_model_number ON public.products USING btree (model_number, id, name, material_number, hot_stamping_paper_type);


--
-- Name: idx_products_covering_paizi; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_covering_paizi ON public.products USING btree (paizi, id, name, model_number, material_number, hot_stamping_paper_type);


--
-- Name: idx_products_hot_stamping_paper_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_hot_stamping_paper_type ON public.products USING btree (hot_stamping_paper_type);


--
-- Name: idx_products_material_lower; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_material_lower ON public.products USING btree (lower((material_number)::text));


--
-- Name: idx_products_model_lower; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_model_lower ON public.products USING btree (lower((model_number)::text));


--
-- Name: idx_products_model_number; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_model_number ON public.products USING btree (model_number);


--
-- Name: idx_products_model_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_model_type ON public.products USING btree (model_number, hot_stamping_paper_type);


--
-- Name: idx_products_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_name ON public.products USING btree (name);


--
-- Name: idx_products_name_lower; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_name_lower ON public.products USING btree (lower((name)::text));


--
-- Name: idx_products_name_paper_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_name_paper_type ON public.products USING btree (name, hot_stamping_paper_type, id);


--
-- Name: idx_products_name_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_name_type ON public.products USING btree (name, hot_stamping_paper_type);


--
-- Name: idx_products_paizi_id_desc; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_paizi_id_desc ON public.products USING btree (paizi, id DESC);


--
-- Name: idx_products_paper_type_optimized; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_paper_type_optimized ON public.products USING btree (hot_stamping_paper_type, name, id);


--
-- Name: idx_products_search_tsv; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_search_tsv ON public.products USING gin (to_tsvector('simple'::regconfig, (((((((((((((((COALESCE(name, ''::character varying))::text || ' '::text) || (COALESCE(model_number, ''::character varying))::text) || ' '::text) || (COALESCE(material_number, ''::character varying))::text) || ' '::text) || (COALESCE(hot_stamping_paper_type, ''::character varying))::text) || ' '::text) || COALESCE(descirption, ''::text)) || ' '::text) || (COALESCE(fengdu, ''::character varying))::text) || ' '::text) || (COALESCE(paizi, ''::character varying))::text) || ' '::text) || COALESCE(gaishu, ''::text))));


--
-- Name: idx_products_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_status ON public.products USING btree (status);


--
-- Name: idx_products_type_id_desc; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_type_id_desc ON public.products USING btree (hot_stamping_paper_type, id DESC);


--
-- Name: idx_products_type_lower; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_type_lower ON public.products USING btree (lower((hot_stamping_paper_type)::text));


--
-- Name: idx_products_usage_count; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_products_usage_count ON public.products USING btree (usage_count DESC);


--
-- Name: idx_read_flag; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_read_flag ON public.sys_announcement_send USING btree (read_flag);


--
-- Name: idx_rg_resource_group_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_resource_group_active ON public.rg_resource_group USING btree (is_active);


--
-- Name: idx_rg_resource_group_work_center; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_resource_group_work_center ON public.rg_resource_group USING btree (work_center_code);


--
-- Name: idx_rg_rule_condition_header; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_rule_condition_header ON public.rg_resource_rule_condition USING btree (rule_header_id);


--
-- Name: idx_rg_rule_condition_param; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_rule_condition_param ON public.rg_resource_rule_condition USING btree (param_code);


--
-- Name: idx_rg_rule_condition_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_rule_condition_type ON public.rg_resource_rule_condition USING btree (rule_type);


--
-- Name: idx_rg_rule_header_resource_group; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_rule_header_resource_group ON public.rg_resource_rule_header USING btree (resource_group_id);


--
-- Name: idx_rg_rule_header_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_rule_header_type ON public.rg_resource_rule_header USING btree (rule_type);


--
-- Name: idx_rg_task_map_resource_group; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_task_map_resource_group ON public.rg_resource_group_task_map USING btree (resource_group_id);


--
-- Name: idx_rg_task_map_task_code; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_rg_task_map_task_code ON public.rg_resource_group_task_map USING btree (task_code);


--
-- Name: idx_role_permissions_permission_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_role_permissions_permission_id ON public.role_permissions USING btree (permission_id);


--
-- Name: idx_role_permissions_role_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_role_permissions_role_id ON public.role_permissions USING btree (role_id);


--
-- Name: idx_roles_role_key; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_roles_role_key ON public.roles USING btree (role_key);


--
-- Name: idx_send_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_send_status ON public.sys_announcement USING btree (send_status);


--
-- Name: idx_send_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_send_status_time ON public.sys_announcement USING btree (send_status, start_time, end_time);


--
-- Name: idx_series_priority_rule_item_rule_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_series_priority_rule_item_rule_id ON public.series_priority_rule_item USING btree (rule_id);


--
-- Name: idx_snapshots_created_by; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_snapshots_created_by ON public.admin_change_snapshots USING btree (created_by);


--
-- Name: idx_snapshots_entity; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_snapshots_entity ON public.admin_change_snapshots USING btree (entity_type, entity_id);


--
-- Name: idx_specifications_color_num; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_specifications_color_num ON public.specifications USING btree (color_num);


--
-- Name: idx_specifications_project_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_specifications_project_id ON public.specifications USING btree (project_id);


--
-- Name: idx_specifications_project_id_color_num; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_specifications_project_id_color_num ON public.specifications USING btree (project_id, color_num);


--
-- Name: idx_start_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_start_time ON public.sys_announcement USING btree (start_time);


--
-- Name: idx_sys_logininfor_lt; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sys_logininfor_lt ON public.sys_logininfor USING btree (login_time);


--
-- Name: idx_sys_logininfor_s; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sys_logininfor_s ON public.sys_logininfor USING btree (status);


--
-- Name: idx_sys_oper_log_bt; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sys_oper_log_bt ON public.sys_oper_log USING btree (business_type);


--
-- Name: idx_sys_oper_log_ot; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sys_oper_log_ot ON public.sys_oper_log USING btree (oper_time);


--
-- Name: idx_sys_oper_log_s; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sys_oper_log_s ON public.sys_oper_log USING btree (status);


--
-- Name: idx_type_sort_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_type_sort_order ON public.hot_stamping_type_options USING btree (sort_order);


--
-- Name: idx_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_id ON public.sys_announcement_send USING btree (user_id);


--
-- Name: idx_user_roles_role_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_roles_role_id ON public.user_roles USING btree (role_id);


--
-- Name: idx_user_roles_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_roles_user_id ON public.user_roles USING btree (user_id);


--
-- Name: idx_users_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_users_status ON public.users USING btree (status);


--
-- Name: idx_uvefr_effect; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_uvefr_effect ON public.uv_effect_feasibility_rules USING btree (effect_id);


--
-- Name: idx_uvefr_product_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_uvefr_product_type ON public.uv_effect_feasibility_rules USING btree (product_type_id);


--
-- Name: idx_uvefr_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_uvefr_status ON public.uv_effect_feasibility_rules USING btree (feasibility_status);


--
-- Name: idx_wrgp_skip_paper_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wrgp_skip_paper_type ON public.wear_resistant_gold_paper_skip_config USING btree (hot_stamping_paper_type);


--
-- Name: idx_yguv_compat_product_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_yguv_compat_product_id ON public.yaguang_uv_oil_compatibility USING btree (product_id);


--
-- Name: idx_yguv_product_is_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_yguv_product_is_active ON public.yaguang_uv_oil_product USING btree (is_active);


--
-- Name: idx_yguv_product_material_code; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_yguv_product_material_code ON public.yaguang_uv_oil_product USING btree (material_code);


--
-- Name: idx_yguv_product_material_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_yguv_product_material_name ON public.yaguang_uv_oil_product USING btree (material_name);


--
-- Name: lamination_compatibility_unique; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX lamination_compatibility_unique ON public.lamination_compatibility USING btree (foil_series, lamination_material_id, model_number_key, post_processing_step_id, product_type);


--
-- Name: post_processing_leduvglitter_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX post_processing_leduvglitter_id_idx ON public.post_processing_leduvglitter USING btree (id);


--
-- Name: post_processing_print_uv_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX post_processing_print_uv_id_idx ON public.post_processing_print_uv USING btree (id);


--
-- Name: permissions update_permissions_updated_at; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_permissions_updated_at BEFORE UPDATE ON public.permissions FOR EACH ROW EXECUTE FUNCTION public.trg_permissions_touch_updated_time();


--
-- Name: roles update_roles_updated_at; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_roles_updated_at BEFORE UPDATE ON public.roles FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: sys_announcement update_sys_announcement_updated_at; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_sys_announcement_updated_at BEFORE UPDATE ON public.sys_announcement FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: users update_users_updated_at; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON public.users FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- Name: old)cloth_paper cloth_paper_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."old)cloth_paper"
    ADD CONSTRAINT cloth_paper_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: cloth_paper_types cloth_paper_types_series_priority_rule_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cloth_paper_types
    ADD CONSTRAINT cloth_paper_types_series_priority_rule_id_fkey FOREIGN KEY (series_priority_rule_id) REFERENCES public.series_priority_rule(id);


--
-- Name: cold_foil_feasibility_rules cold_foil_feasibility_rules_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_feasibility_rules
    ADD CONSTRAINT cold_foil_feasibility_rules_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id);


--
-- Name: cold_foil_feasibility_rules cold_foil_feasibility_rules_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cold_foil_feasibility_rules
    ADD CONSTRAINT cold_foil_feasibility_rules_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.cold_foil_type_options(id);


--
-- Name: embossing_feasibility_rules embossing_feasibility_rules_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_feasibility_rules
    ADD CONSTRAINT embossing_feasibility_rules_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id);


--
-- Name: embossing_feasibility_rules embossing_feasibility_rules_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.embossing_feasibility_rules
    ADD CONSTRAINT embossing_feasibility_rules_type_id_fkey FOREIGN KEY (type_id) REFERENCES public.embossing_type_options(id);


--
-- Name: old_film_butter_paper film_butter_paper_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_film_butter_paper
    ADD CONSTRAINT film_butter_paper_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: sys_announcement_send fk_annt_send_annt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_announcement_send
    ADD CONSTRAINT fk_annt_send_annt FOREIGN KEY (annt_id) REFERENCES public.sys_announcement(id) ON DELETE CASCADE;


--
-- Name: sys_announcement_send fk_annt_send_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sys_announcement_send
    ADD CONSTRAINT fk_annt_send_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: admin_change_records fk_change_operator; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_records
    ADD CONSTRAINT fk_change_operator FOREIGN KEY (operator_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: lamination_compatibility fk_lamination_compatibility_lamination_material; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_compatibility
    ADD CONSTRAINT fk_lamination_compatibility_lamination_material FOREIGN KEY (lamination_material_id) REFERENCES public.lamination_material_options(id) ON DELETE CASCADE;


--
-- Name: lamination_compatibility fk_lamination_compatibility_post_processing; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_compatibility
    ADD CONSTRAINT fk_lamination_compatibility_post_processing FOREIGN KEY (post_processing_step_id) REFERENCES public.post_processing_options(id) ON DELETE CASCADE;


--
-- Name: admin_change_snapshots fk_snapshot_operator; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.admin_change_snapshots
    ADD CONSTRAINT fk_snapshot_operator FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: global_parameters global_parameters_session_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.global_parameters
    ADD CONSTRAINT global_parameters_session_id_fkey FOREIGN KEY (session_id) REFERENCES public.evaluation_session(id) ON DELETE CASCADE;


--
-- Name: gold_foil_type gold_foil_type_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gold_foil_type
    ADD CONSTRAINT gold_foil_type_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: lamination_feasibility_rules lamination_feasibility_rules_lamination_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_feasibility_rules
    ADD CONSTRAINT lamination_feasibility_rules_lamination_type_id_fkey FOREIGN KEY (lamination_type_id) REFERENCES public.lamination_type_options(id);


--
-- Name: lamination_feasibility_rules lamination_feasibility_rules_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_feasibility_rules
    ADD CONSTRAINT lamination_feasibility_rules_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id);


--
-- Name: lamination_feasibility_rules lamination_feasibility_rules_substrate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_feasibility_rules
    ADD CONSTRAINT lamination_feasibility_rules_substrate_id_fkey FOREIGN KEY (substrate_id) REFERENCES public.lamination_substrate_options(id);


--
-- Name: lamination_material_compatibility lamination_material_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lamination_material_compatibility
    ADD CONSTRAINT lamination_material_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.lamination_material_products(id);


--
-- Name: leo_gp_numbers leo_gp_numbers_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leo_gp_numbers
    ADD CONSTRAINT leo_gp_numbers_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: material_process_compatibility material_process_compatibility_material_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material_process_compatibility
    ADD CONSTRAINT material_process_compatibility_material_id_fkey FOREIGN KEY (material_id) REFERENCES public.material_catalog(id) ON DELETE CASCADE;


--
-- Name: old_matte_lamination matte_lamination_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_matte_lamination
    ADD CONSTRAINT matte_lamination_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: old_oil_and_uv_types oil_and_uv_types_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_oil_and_uv_types
    ADD CONSTRAINT oil_and_uv_types_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: old_pattern pattern_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.old_pattern
    ADD CONSTRAINT pattern_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: permissions permissions_parent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES public.permissions(id) ON DELETE CASCADE;


--
-- Name: pricing pricing_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pricing
    ADD CONSTRAINT pricing_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: printing_feasibility_rules printing_feasibility_rules_process_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_feasibility_rules
    ADD CONSTRAINT printing_feasibility_rules_process_id_fkey FOREIGN KEY (process_id) REFERENCES public.printing_process_options(id);


--
-- Name: printing_feasibility_rules printing_feasibility_rules_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_feasibility_rules
    ADD CONSTRAINT printing_feasibility_rules_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id);


--
-- Name: printing_feasibility_rules printing_feasibility_rules_substrate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.printing_feasibility_rules
    ADD CONSTRAINT printing_feasibility_rules_substrate_id_fkey FOREIGN KEY (substrate_id) REFERENCES public.printing_substrate_options(id);


--
-- Name: processing_after_ironing processing_after_ironing_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.processing_after_ironing
    ADD CONSTRAINT processing_after_ironing_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: product_type_sort_config product_type_sort_config_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_type_sort_config
    ADD CONSTRAINT product_type_sort_config_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id) ON DELETE CASCADE;


--
-- Name: rg_resource_group_capacity rg_resource_group_capacity_resource_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_capacity
    ADD CONSTRAINT rg_resource_group_capacity_resource_group_id_fkey FOREIGN KEY (resource_group_id) REFERENCES public.rg_resource_group(id) ON DELETE CASCADE;


--
-- Name: rg_resource_group_detail rg_resource_group_detail_resource_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_detail
    ADD CONSTRAINT rg_resource_group_detail_resource_group_id_fkey FOREIGN KEY (resource_group_id) REFERENCES public.rg_resource_group(id) ON DELETE CASCADE;


--
-- Name: rg_resource_group_task_map rg_resource_group_task_map_resource_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_task_map
    ADD CONSTRAINT rg_resource_group_task_map_resource_group_id_fkey FOREIGN KEY (resource_group_id) REFERENCES public.rg_resource_group(id) ON DELETE CASCADE;


--
-- Name: rg_resource_group_task_map rg_resource_group_task_map_task_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_group_task_map
    ADD CONSTRAINT rg_resource_group_task_map_task_code_fkey FOREIGN KEY (task_code) REFERENCES public.rg_task_definition(task_code);


--
-- Name: rg_resource_rule_condition rg_resource_rule_condition_rule_header_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_condition
    ADD CONSTRAINT rg_resource_rule_condition_rule_header_id_fkey FOREIGN KEY (rule_header_id) REFERENCES public.rg_resource_rule_header(id) ON DELETE CASCADE;


--
-- Name: rg_resource_rule_header rg_resource_rule_header_resource_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rg_resource_rule_header
    ADD CONSTRAINT rg_resource_rule_header_resource_group_id_fkey FOREIGN KEY (resource_group_id) REFERENCES public.rg_resource_group(id) ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_permission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(id) ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: series_priority_rule_item series_priority_rule_item_rule_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.series_priority_rule_item
    ADD CONSTRAINT series_priority_rule_item_rule_id_fkey FOREIGN KEY (rule_id) REFERENCES public.series_priority_rule(id) ON DELETE CASCADE;


--
-- Name: silicone_coarse_uv_compatibility silicone_coarse_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_coarse_uv_compatibility
    ADD CONSTRAINT silicone_coarse_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_coarse_uv_product(id);


--
-- Name: silicone_glittering_star_compatibility silicone_glittering_star_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glittering_star_compatibility
    ADD CONSTRAINT silicone_glittering_star_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_glittering_star_product(id);


--
-- Name: silicone_glow_ink_compatibility silicone_glow_ink_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_glow_ink_compatibility
    ADD CONSTRAINT silicone_glow_ink_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_glow_ink_product(id);


--
-- Name: silicone_led_uv_spray_compatibility silicone_led_uv_spray_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_led_uv_spray_compatibility
    ADD CONSTRAINT silicone_led_uv_spray_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_led_uv_spray_product(id);


--
-- Name: silicone_mica_pearl_compatibility silicone_mica_pearl_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_mica_pearl_compatibility
    ADD CONSTRAINT silicone_mica_pearl_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_mica_pearl_product(id);


--
-- Name: silicone_orange_peel_uv_compatibility silicone_orange_peel_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_orange_peel_uv_compatibility
    ADD CONSTRAINT silicone_orange_peel_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_orange_peel_uv_product(id);


--
-- Name: silicone_sandblast_uv_compatibility silicone_sandblast_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_sandblast_uv_compatibility
    ADD CONSTRAINT silicone_sandblast_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_sandblast_uv_product(id);


--
-- Name: silicone_screen_uv_compatibility silicone_screen_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_screen_uv_compatibility
    ADD CONSTRAINT silicone_screen_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_screen_uv_product(id);


--
-- Name: silicone_water_base_transparent_compatibility silicone_water_base_transparent_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_water_base_transparent_compatibility
    ADD CONSTRAINT silicone_water_base_transparent_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_water_base_transparent_product(id);


--
-- Name: silicone_watercolor_ink_compatibility silicone_watercolor_ink_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_watercolor_ink_compatibility
    ADD CONSTRAINT silicone_watercolor_ink_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_watercolor_ink_product(id);


--
-- Name: silicone_white_uv_compatibility silicone_white_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_white_uv_compatibility
    ADD CONSTRAINT silicone_white_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_white_uv_product(id);


--
-- Name: silicone_wrinkle_uv_compatibility silicone_wrinkle_uv_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.silicone_wrinkle_uv_compatibility
    ADD CONSTRAINT silicone_wrinkle_uv_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.silicone_wrinkle_uv_product(id);


--
-- Name: specifications specifications_project_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.specifications
    ADD CONSTRAINT specifications_project_id_fkey FOREIGN KEY (project_id) REFERENCES public.products(id);


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: uv_effect_feasibility_rules uv_effect_feasibility_rules_effect_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_feasibility_rules
    ADD CONSTRAINT uv_effect_feasibility_rules_effect_id_fkey FOREIGN KEY (effect_id) REFERENCES public.uv_effect_options(id);


--
-- Name: uv_effect_feasibility_rules uv_effect_feasibility_rules_product_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_effect_feasibility_rules
    ADD CONSTRAINT uv_effect_feasibility_rules_product_type_id_fkey FOREIGN KEY (product_type_id) REFERENCES public.product_type_options(id);


--
-- Name: uv_oil_matte_compatibility uv_oil_matte_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uv_oil_matte_compatibility
    ADD CONSTRAINT uv_oil_matte_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.uv_oil_matte_product(id);


--
-- Name: water_varnish_matte_compatibility water_varnish_matte_compatibility_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.water_varnish_matte_compatibility
    ADD CONSTRAINT water_varnish_matte_compatibility_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.water_varnish_matte_product(id);


--
-- PostgreSQL database dump complete
--

