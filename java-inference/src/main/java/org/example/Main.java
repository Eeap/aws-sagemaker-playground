package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointRequest;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointResponse;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        SageMakerRuntimeClient client = SageMakerRuntimeClient.builder().region(Region.US_EAST_1).build();
        Dotenv dotenv = Dotenv.load();
        try {
            InputStream is = new FileInputStream("nyc_taxi.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            ArrayList<String> data = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (isNum(values[1].trim()))
                    data.add(values[1].trim());
            }
            InvokeEndpointRequest req = InvokeEndpointRequest.builder()
                    .endpointName(dotenv.get("ENDPOINT_NAME"))
                    .contentType("text/csv")
                    .accept("application/json")
                    .body(SdkBytes.fromByteBuffer(ByteBuffer.wrap(String.join("\n", data).getBytes())))
                    .build();
            InvokeEndpointResponse res = client.invokeEndpoint(req);
            System.out.println(res.body().asUtf8String());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SageMaker Inference Test");
    }

    private static boolean isNum(String trim) {

        try {
            Integer.parseInt(trim);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}