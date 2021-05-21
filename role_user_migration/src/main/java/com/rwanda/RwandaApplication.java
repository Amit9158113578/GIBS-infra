package com.rwanda;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rwanda.api.RwandaApplicationService;

@SpringBootApplication
public class RwandaApplication {

	public static String urlToFetchAllRoles;

	public static String urlToFetchAllUsers;

	public static String createRoleURL;

	public static String createUserURL;

	public static String defaultPassword;

	public static String defaultUpload;

	public static boolean createRoleEnable;

	public static boolean createUserEnable;

	public static String urlToFetchExistingUsers;

	@Value("${rwanda.create.role}")
	public void setCreateRoleEnable(boolean createRoleEnable) {
		RwandaApplication.createRoleEnable = createRoleEnable;
	}

	@Value("${rwanda.create.user}")
	public void setCreateUserEnable(boolean createUserEnable) {
		RwandaApplication.createUserEnable = createUserEnable;
	}

	@Value("${rwanda.default.user.role.upload}")
	public void setDefaultUpload(String defaultUpload) {
		RwandaApplication.defaultUpload = defaultUpload;
	}

	@Value("${rwanda.get.allroles.url}")
	public void setUrlToFetchAllRoles(String urlToFetchAllRoles) {
		RwandaApplication.urlToFetchAllRoles = urlToFetchAllRoles;
	}

	@Value("${rwanda.get.allusers.url}")
	public void setUrlToFetchAllUsers(String urlToFetchAllUsers) {
		RwandaApplication.urlToFetchAllUsers = urlToFetchAllUsers;
	}

	@Value("${rwanda.create.roles.url}")
	public void setCreateRoleURL(String createRoleURL) {
		RwandaApplication.createRoleURL = createRoleURL;
	}

	@Value("${rwanda.create.users.url}")
	public void setCreateUserURL(String createUserURL) {
		RwandaApplication.createUserURL = createUserURL;
	}

	@Value("${rwanda.default.password}")
	public void setDefaultPassword(String defaultPassword) {
		RwandaApplication.defaultPassword = defaultPassword;
	}

	@Value("${rwanda.get.allusers.dest.url}")
	public void setUrlToFetchAllUsersfromDest(String urlToFetchAllUsersfromDest) {
		RwandaApplication.urlToFetchExistingUsers = urlToFetchAllUsersfromDest;
	}

	public static void main(String[] args) {
		Logger log = Logger.getLogger(RwandaApplication.class);
		try {
			ConfigurableApplicationContext ctx = SpringApplication.run(RwandaApplication.class, args);
			String authheader = "Basic "
					+ new String((Base64.encodeBase64((args[0] + ":" + args[1]).getBytes(StandardCharsets.UTF_8))));

			RwandaApplicationService callApiRequest = new RwandaApplicationService();

			log.debug("*** Creating role section start ***");
			if (createRoleEnable) {
				try {
					JSONArray roles = callApiRequest.getRoles(urlToFetchAllRoles, authheader);
					if (roles != null) {
						callApiRequest.createRole(roles, createRoleURL, authheader);
					} else {
						log.debug("Unable to fetch role from server:");
					}
				} catch (IOException e) {
					log.error("Error in role API ", e);
				} catch (Exception e) {
					log.error("Error in role API ", e);
				}
			} else {
				log.info("Create role property is disabled ");
			}

			log.debug("*** Creating user section start ***");
			if (createUserEnable) {
				try {
					JSONObject users = callApiRequest.getUsers(urlToFetchAllUsers, authheader);
					if (users != null) {
//						JSONObject existingUsers = callApiRequest.getExistingUsers(urlToFetchExistingUsers, authheader);
						callApiRequest.createUsers(users, createUserURL, authheader);
					} else {
						log.debug("Unable to fetch user from server:");
					}
				} catch (IOException e) {
					log.error("Error in user API ", e);
				} catch (Exception e) {
					log.error("Error in user API ", e);
				}
			} else {
				log.info("Create user property is disabled ");
			}

			log.debug("***  End of execution ***");
		} catch (Exception e) {
			log.error("Exception in main : ", e);
		}
		ctx.close();
	}
}
