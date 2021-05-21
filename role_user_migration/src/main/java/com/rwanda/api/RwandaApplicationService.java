package com.rwanda.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.rwanda.RwandaApplication;
import com.rwanda.model.RequestForRoles;
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
}
