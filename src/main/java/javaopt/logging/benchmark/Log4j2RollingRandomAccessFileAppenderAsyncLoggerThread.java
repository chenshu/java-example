package javaopt.logging.benchmark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2RollingRandomAccessFileAppenderAsyncLoggerThread implements Runnable {

    Logger logger = LogManager.getLogger(this.getClass());

    String trace_id = "c0a8c13814165408688650666511f";
    String trace_stack = "0";
    String trace_path = "0";
    String trace_status = "OK";
    String trace_message = "ca";
    String id = "e31b7f55-e9a4-4e52-bb07-33c853f72566";
    String type = "request";
    String svc = "OpenOther.Delegate.openpush_xforportal_portalquerytargetusernumber";
    String destination = "/queue/ca-request";
    String timestamp = "1416540868866";
    String app = "59395";
    String uid = "900086000000000237";
    String cid = "12df48779df994b6acf9a60e";
    String ts = "1416540692746";
    String addr = "221.226.48.130";
    String reqid_or_resid = "FQHOI5SZSK4XKOV1E20NNLZ882R910RW";
    String replyto_or_status = "/temp-queue/192.168.193.56_1416377403571__Multi_Redis__192.168.193.82:6973";
    String req_param_or_res_body_size = "121";
    String req_or_res_size = "0";
    String local_ip = "192.168.193.56";
    String symbol_time = "1416540868865";
    String elapsed_time = "1.0";

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()
                && !TestLog4j2.interrupted) {
            test();
        }
    }

    protected void test() {
        if (logger.isInfoEnabled()) {
            logger.info(
                    "{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}",
                    trace_id, trace_stack, trace_path, trace_status,
                    trace_message, id, type, svc, destination, timestamp, app,
                    uid, cid, ts, addr, reqid_or_resid, replyto_or_status,
                    req_param_or_res_body_size, req_or_res_size, local_ip,
                    symbol_time, elapsed_time);
        }
    }

}
