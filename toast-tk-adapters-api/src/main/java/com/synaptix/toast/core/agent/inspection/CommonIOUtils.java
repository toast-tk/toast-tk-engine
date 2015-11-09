package com.synaptix.toast.core.agent.inspection;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.synaptix.toast.core.agent.interpret.AWTCapturedEvent;
import com.synaptix.toast.core.agent.interpret.IEventInterpreter.EventType;
import com.synaptix.toast.core.agent.interpret.WebEventRecord;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.CommandRequest.COMMAND_TYPE;
import com.synaptix.toast.core.net.request.HighLightRequest;
import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.core.net.request.InitInspectionRequest;
import com.synaptix.toast.core.net.request.PoisonPill;
import com.synaptix.toast.core.net.request.RecordRequest;
import com.synaptix.toast.core.net.request.ScanRequest;
import com.synaptix.toast.core.net.request.TableCommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQuery;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;
import com.synaptix.toast.core.net.response.ErrorResponse;
import com.synaptix.toast.core.net.response.ExistsResponse;
import com.synaptix.toast.core.net.response.InitResponse;
import com.synaptix.toast.core.net.response.RecordResponse;
import com.synaptix.toast.core.net.response.ScanResponse;
import com.synaptix.toast.core.net.response.ValueResponse;
import com.synaptix.toast.core.net.response.WebRecordResponse;

public class CommonIOUtils {

	public static final int DRIVER_TCP_PORT = 1470;
	public static final int AGENT_TCP_PORT = 1471;

	public static void initSerialization(
		Kryo kryo) {
		kryo.register(ArrayList.class);
		kryo.register(DirectColorModel.class);
		kryo.register(BufferedImage.class);
		kryo.register(COMMAND_TYPE.class);
		kryo.register(InitInspectionRequest.class);
		kryo.register(CommandRequest.class);
		kryo.register(TableCommandRequestQueryCriteria.class);
		kryo.register(TableCommandRequestQuery.class);
		kryo.register(TableCommandRequest.class);
		kryo.register(EventType.class);
		kryo.register(AWTCapturedEvent.class);
		kryo.register(IIdRequest.class);
		kryo.register(ScanRequest.class);
		kryo.register(RecordRequest.class);
		kryo.register(HighLightRequest.class);
		kryo.register(ExistsResponse.class);
		kryo.register(ErrorResponse.class);
		kryo.register(ValueResponse.class);
		kryo.register(InitResponse.class);
		kryo.register(ScanResponse.class);
		kryo.register(RecordResponse.class);
		kryo.register(WebEventRecord.class);
		kryo.register(WebRecordResponse.class);
		kryo.register(PoisonPill.class);
	}
}
