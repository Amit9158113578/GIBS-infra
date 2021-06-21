package com.rwanda.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.rwanda.RwandaApplication;
import com.rwanda.model.APIResponse;
import com.rwanda.model.RequestForObject;
import com.rwanda.model.RequestForRoles;
import com.rwanda.model.RequestForSpace;
import com.rwanda.model.RequestForUsers;

@Component
public class RwandaApplicationService {

	private Logger log = Logger.getLogger(RwandaApplicationService.class);

	public JSONArray getRoles(String url, String authHeader) throws IOException {
		log.info("Get All roles");
		log.info("URL :" + url);
		JSONArray jsonArrResponse = null;
		URL urlForGetRequest = new URL(url);
		String readLine = null;
		SSLContext sc = ignoreSSLCertificate();
		if (sc == null) {
			log.debug("Unable to create SSL context");
			return null;
		}
		HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
		connection.setRequestProperty("Authorization", authHeader);
		connection.setRequestMethod("GET");
		connection.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = connection.getResponseCode();
		log.debug("Response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			String stringResponse = response.toString();
			log.debug("Response:  " + stringResponse);
			jsonArrResponse = new JSONArray(stringResponse);
		} else {
			throw new IOException();
		}
		return jsonArrResponse;
	}

	public void createRole(JSONArray jsonArrResponse, String baseUrl, String authHeader) {
		log.debug("Creating role:");
		log.info("baseUrl :" + baseUrl);
		try {
			// setDefaultUpload("2");
			int jsonArrLength = jsonArrResponse.length();
			int counter = (RwandaApplication.defaultUpload.equalsIgnoreCase("ALL") ? jsonArrLength
					: (Integer.parseInt(RwandaApplication.defaultUpload)));
			for (int i = 0; i < (counter >= jsonArrLength ? jsonArrLength : counter); i++) {
				JSONObject jsonObj = jsonArrResponse.getJSONObject(i);
				log.debug("Role Request Body: " + jsonObj);
				Gson gson = new Gson();
				RequestForRoles request = gson.fromJson(jsonObj.toString(), RequestForRoles.class);
				if (!request.getMetadata().is_reserved()) {
					SSLContext sc = ignoreSSLCertificate();
					if (sc == null) {
						log.debug("Unable to create SSL context");
						return;
					}
					URL url = new URL(baseUrl + request.getName());
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("PUT");
					conn.setConnectTimeout(900000);
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Authorization", authHeader);
					conn.setRequestProperty("kbn-xsrf", "reporting");
					conn.setSSLSocketFactory(sc.getSocketFactory());
					ObjectMapper objmapper = new ObjectMapper();
					objmapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
					String requestBody = objmapper.writeValueAsString(request);
					log.info("Request Body for update: " + requestBody);
					sendStream(conn, requestBody);
					BufferedReader br = null;
					log.info("Response Code: " + conn.getResponseCode());
					if (conn.getResponseCode() == HttpStatus.CREATED.value()
							|| (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)) {
						br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
						log.info("Role created successfully");
					} else {
						br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
						log.info("Failed to add role!");
					}
					String output = readStream(br);
					log.debug("response :  " + output);
				} else {
					log.debug("Ignoring role :" + request.getName());
				}
			}
		} catch (Exception e) {
			log.error("Error while creating Roles : " + e);
		}

	}

	public SSLContext ignoreSSLCertificate() {
		SSLContext sc = null;
		// Install the all-trusting trust manager
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			sc = SSLContext.getInstance("TLS");

			sc.init(null, trustAllCerts, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {

					return true;

				}

			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		} catch (Exception e) {

			;

		}
		return sc;
	}

	public String readStream(BufferedReader br) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int c; (c = br.read()) != -1;) {
			sb.append((char) c);
		}
		return sb.toString();
	}

	public void sendStream(HttpURLConnection conn, String inputBody) throws IOException {
		OutputStream os;
		os = conn.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		osw.write(inputBody.toString());
		osw.flush();
		osw.close();
	}

	public JSONObject getUsers(String url, String authHeader) throws IOException {
		log.info("Get All Users");
		log.info("URL :" + url);
		JSONObject jsonObject = null;
		URL urlForGetRequest = new URL(url);
		String readLine = null;
		SSLContext sc = ignoreSSLCertificate();
		if (sc == null) {
			log.debug("Unable to create SSL context");
			return null;
		}
		HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
		connection.setRequestProperty("Authorization", authHeader);
		connection.setRequestMethod("GET");
		connection.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = connection.getResponseCode();
		log.debug("Response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			String stringResponse = response.toString();
			log.debug("Response:  " + stringResponse);
			jsonObject = new JSONObject(stringResponse);
		} else {
			throw new IOException();
		}
		return jsonObject;
	}

	public JSONObject getExistingUsers(String url, String authHeader) throws IOException {
		log.info("Get All existing Users");
		log.info("URL :" + url);
		JSONObject jsonObject = null;
		URL urlForGetRequest = new URL(url);
		String readLine = null;
		SSLContext sc = ignoreSSLCertificate();
		if (sc == null) {
			log.debug("Unable to create SSL context");
			return null;
		}
		HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
		connection.setRequestProperty("Authorization", authHeader);
		connection.setRequestMethod("GET");
		connection.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = connection.getResponseCode();
		log.debug("Response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			String stringResponse = response.toString();
			log.debug("Response:  " + stringResponse);
			jsonObject = new JSONObject(stringResponse);
		} else {
			throw new IOException();
		}
		return jsonObject;
	}

	public void createUsers(JSONObject jsonObj, String baseUrl,String authHeader) {
		log.debug("Creating user:");
		log.info("Create User baseUrl :" + baseUrl);
		try {
			// setDefaultUpload("2");
			log.debug("User Request Body : " + jsonObj);
			int jsonLength = jsonObj.length();
			int defaultCounter = (RwandaApplication.defaultUpload.equalsIgnoreCase("ALL") ? jsonLength
					: Integer.parseInt(RwandaApplication.defaultUpload));
			Gson gson = new Gson();
			Iterator<String> keys = jsonObj.keys();
			int counter = 0;
			while (keys.hasNext() && (defaultCounter >= jsonLength ? true : counter < defaultCounter)) {
				String key = keys.next();
				if (jsonObj.get(key) instanceof JSONObject) {
					RequestForUsers request = gson.fromJson(jsonObj.get(key).toString(), RequestForUsers.class);
					if (request == null) {
						log.debug("Unable to parse user request for index : ");
						return;
					}
					if (!request.getMetadata().is_reserved()) {
						/*
						 * if (!userExist(existingUsers, request.getUsername())) {
						 * request.setPassword(RwandaApplication.defaultPassword); } else {
						 * request.setPassword(null); }
						 */
						if (!userExist(baseUrl, authHeader, request.getUsername())) {
							request.setPassword(RwandaApplication.defaultPassword);
						} else {
							request.setPassword(null);
						}

						SSLContext sc = ignoreSSLCertificate();
						if (sc == null) {
							log.debug("Unable to create SSL context");
							return;
						}
						URL url = new URL(baseUrl + request.getUsername());
						HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.setRequestMethod("PUT");
						conn.setConnectTimeout(900000);
						conn.setRequestProperty("Content-Type", "application/json");
						conn.setRequestProperty("Authorization", authHeader);
						conn.setRequestProperty("kbn-xsrf", "reporting");
						conn.setSSLSocketFactory(sc.getSocketFactory());
						ObjectMapper objmapper = new ObjectMapper();
						objmapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
						String requestBody = objmapper.writeValueAsString(request);
						log.info("Request Body for update: " + requestBody);
						sendStream(conn, requestBody);
						BufferedReader br = null;
						log.info("Response Code: " + conn.getResponseCode());
						if (conn.getResponseCode() == HttpStatus.CREATED.value()
								|| (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)) {
							br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
							log.info("User created successfully");
						} else {
							br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
							log.info("Failed to add user!");
						}
						String output = readStream(br);
						log.debug("response :  " + output);
					} else {
						log.debug("Ignoring user :" + request.getUsername());
					}
				}
				counter++;
			}
		} catch (Exception e) {
			log.error("Error while creating Users : " + e);
		}

	}

	public boolean userExistOrNot(JSONObject existingUsers, String checkUser) {

		Gson gson = new Gson();
		Iterator<String> keys = existingUsers.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if (existingUsers.get(key) instanceof JSONObject) {
				RequestForUsers request = gson.fromJson(existingUsers.get(key).toString(), RequestForUsers.class);
				if (request == null) {
					log.debug("Unable to parse user request for index : ");
					return false;
				} else {
					if (request.getUsername().equals(checkUser)) {
						log.debug("User already exist in destination : " + checkUser);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean userExist(String url, String authHeader,String username) {

		try {
			JSONObject jsonObjectresponse = getUser(url, authHeader,username);
			if(jsonObjectresponse == null)
				return false;
			Gson gson = new Gson();
			Iterator<String> keys = jsonObjectresponse.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				if (jsonObjectresponse.get(key) instanceof JSONObject) {
					RequestForUsers request = gson.fromJson(jsonObjectresponse.get(key).toString(), RequestForUsers.class);
					if (request == null) {
						log.debug("Unable to parse user request for index : ");
						return false;
					} else {
						if (request.getUsername().equals(username)) {
							log.debug("User already exist in destination : " + username);
							return true;
						}
					}
				}
			}
			return false;

		} catch (IOException e) {
			log.error("Error while getting user details against user : "+username+ " : ", e);;
			e.printStackTrace();
		}
		return false;
	}

	public JSONObject getUser(String url, String authHeader,String username) throws IOException {
		log.info("Get Users by username : "+ username);
		url = url +username;
		log.info("URL :" + url);
		JSONObject jsonObject = null;

		URL urlForGetRequest = new URL(url);
		String readLine = null;
		SSLContext sc = ignoreSSLCertificate();
		if (sc == null) {
			log.debug("Unable to create SSL context");
			return null;
		}
		HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
		connection.setRequestProperty("Authorization", authHeader);
		connection.setRequestMethod("GET");
		connection.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = connection.getResponseCode();
		log.debug("Response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			String stringResponse = response.toString();
			log.debug("Response:  " + stringResponse);
			jsonObject = new JSONObject(stringResponse);
		} else {
			return null;
		}
		return jsonObject;
	}

	public JSONObject getSpaceBySpaceId(String url, String authHeader,String spaceId) throws IOException {
		log.info("Get space :");
		JSONObject jsonObject = null;
		url = url + "/" + spaceId;
		URL urlForGetRequest = new URL(url);
		log.info("URL :" + url);
		String readLine = null;
		SSLContext sc = ignoreSSLCertificate();
		if (sc == null) {
			log.debug("Unable to create SSL context");
			return null;
		}
		HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
		connection.setRequestProperty("Authorization", authHeader);
		connection.setRequestMethod("GET");
		connection.setSSLSocketFactory(sc.getSocketFactory());
		int responseCode = connection.getResponseCode();
		log.debug("Response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			String stringResponse = response.toString();
			log.debug("Response:  " + stringResponse);
			jsonObject = new JSONObject(stringResponse);
		} else {
			return null;
		}
		return jsonObject;
	}

	public void importSpace(JSONObject jsonResponse, String baseUrl, String authHeader) {
		log.debug("importing spaces to :"+baseUrl);
		try {
			log.debug("Space Request Body: " + jsonResponse);
			Gson gson = new Gson();
			RequestForSpace request = gson.fromJson(jsonResponse.toString(), RequestForSpace.class);
			SSLContext sc = ignoreSSLCertificate();
			if (sc == null) {
				log.debug("Unable to create SSL context");
				return;
			}
			URL url = null;
			HttpsURLConnection conn = null;
			if (getSpaceBySpaceId(baseUrl, authHeader, request.getId()) != null) {
				url = new URL(baseUrl+"/"+jsonResponse.getString("id"));
				conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("PUT");
			} else {
				url = new URL(baseUrl);
				conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
			}
			conn.setDoOutput(true);
			conn.setConnectTimeout(900000);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", authHeader);
			conn.setRequestProperty("kbn-xsrf", "reporting");
			conn.setSSLSocketFactory(sc.getSocketFactory());
			ObjectMapper objmapper = new ObjectMapper();
			objmapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			String requestBody = objmapper.writeValueAsString(request);
			log.info("Request Body for update / create : " + requestBody);
			sendStream(conn, requestBody);
			BufferedReader br = null;
			log.info("Response Code: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpStatus.CREATED.value()
					|| (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)) {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				log.info("Space created / Updated successfully");
			} else {
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				log.info("Failed to add Space!");
			}
			String output = readStream(br);
			log.debug("response :  " + output);
		} catch (Exception e) {
			log.error("Error while creating Space : " + e);
		}

	}

	public boolean exportObjects(String baseUrl, List<String> objectTypes, String spaceId, String filePathToExportObjects,
			String authHeader) throws IOException {
		boolean result = false;
		log.debug("Exporting objects:");
		baseUrl = baseUrl + spaceId + "/api/saved_objects/_export";
		log.info("baseUrl :" + baseUrl);
		try {
			RequestForObject requestForObjectExport = new RequestForObject();			
			requestForObjectExport.setType(objectTypes);
			requestForObjectExport.setIncludeReferencesDeep("true");
			SSLContext sc = ignoreSSLCertificate();
			if (sc == null) {
				log.debug("Unable to create SSL context");
				return result;
			}
			URL url = new URL(baseUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setConnectTimeout(900000);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", authHeader);
			conn.setRequestProperty("kbn-xsrf", "reporting");
			conn.setSSLSocketFactory(sc.getSocketFactory());
			ObjectMapper objmapper = new ObjectMapper();
			objmapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			String requestBody = objmapper.writeValueAsString(requestForObjectExport);
			log.info("Request Body for exporting objects : " + requestBody);
			sendStream(conn, requestBody);
			BufferedReader br = null;
			log.info("Response Code: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpStatus.CREATED.value()
					|| (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)) {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			} else {
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				log.info("Failed to export Object!");
				String output = readStream(br);
				if(output!=null && !output.isEmpty()) {
					APIResponse resp = objmapper.readValue(output, APIResponse.class);
					if(resp !=null) {
						if(resp.getMessage().contains("Trying to export non-exportable type(s)")) {
							String type = resp.getMessage().substring(resp.getMessage().indexOf(":")+1);
							if(type !=null ) {
								String split[] = type.split(",");
								for(String input: split) {
									log.debug("deleteing type: "+input);
									input = input.trim();
									objectTypes.remove(input);
								}
								return exportObjects(RwandaApplication.urlToExportObjects, objectTypes, spaceId, filePathToExportObjects, authHeader);
							}

						}
 					}
				}
				return result;
			}
			String output = readStream(br);
			if(output.contains("exportedCount\":0")){
				log.error("Invalid space ID : ");
				return result;
			}
			File dir = new File(filePathToExportObjects.trim());
			if(!dir.exists()) {
				dir.mkdir();
			}

			FileWriter writer = new FileWriter(filePathToExportObjects + "Exportedobjects.ndjson");
			try {
				writer.write(output);
				log.debug("File exported at : " + filePathToExportObjects);
				log.info("Object exported successfully");
				result = true;
			} catch (Exception e) {
				log.error("Exception in writing ndjson file :" + e);
				result = false;
			} finally {
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
			log.error("Error while exporting Objects: " + e);
			result = false;
		}
		return result;
	}

	public void importObjects(String filePath, String spaceId, String baseUrl, String authHeader) {
		//		log.debug("Importing objects:");
		baseUrl = baseUrl + spaceId + "/api/saved_objects/_import?overwrite=true";
		log.info("Importing objects to baseUrl : " + baseUrl);
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(baseUrl);
			httppost.addHeader("Authorization", authHeader);
			httppost.addHeader("kbn-xsrf", "reporting");

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			File f = new File(filePath + "Exportedobjects.ndjson");
			if (f == null || !f.exists()) {
				return;
			}
			builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());
			HttpEntity multipart = builder.build();
			httppost.setEntity(multipart);
			CloseableHttpResponse response = null;
			response = httpclient.execute(httppost);
			HttpEntity responseEntity = response.getEntity();
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader((responseEntity.getContent())));
			String output = readStream(br);
			log.info("Response From server : " + output);
		} catch (IOException e) {
			log.error("IO exception : " + e);
		} catch (Exception e) {
			log.error("Exception  : " + e);
		}
	}
}
