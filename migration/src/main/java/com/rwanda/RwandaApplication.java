package com.rwanda;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

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

	public static boolean createSpaceEnable;

	public static String urlToFetchSpace;

	public static String urlToFetchSpaceDest;

	public static String spaceIds;

	public static boolean importExportObjectEnable;

	public static String urlToExportObjects;

	public static String typesOfObjectToBeExported;

	public static String ndsJsonFilePath;

	public static String spaceIdToExportObjects;

	public static String urlToImportObjects;

	public static String spaceIdToImportObjects;

	/*@Value("${rwanda.import.objects.space}")
	public void setSpaceIdToImportObjects(String spaceIdToImportObjects) {
		RwandaApplication.spaceIdToImportObjects = spaceIdToImportObjects;
	}*/

	@Value("${rwanda.object.import.url}")
	public void setUrlToImportObjects(String urlToImportObjects) {
		RwandaApplication.urlToImportObjects = urlToImportObjects;
	}

	/*@Value("${rwanda.object.export.space}")
	public void setSpaceIdforExportObjects(String spaceIdforExportObjects) {
		RwandaApplication.spaceIdToExportObjects = spaceIdforExportObjects;
	}*/

	@Value("${rwanda.object.export.filepath}")
	public void setNdsJsonFilePath(String ndsJsonFilePath) {
		RwandaApplication.ndsJsonFilePath = ndsJsonFilePath;
	}

	@Value("${rwanda.object.export.types}")
	public void setTypeOfObjectToBeExported(String typeOfObjectToBeExported) {
		RwandaApplication.typesOfObjectToBeExported = typeOfObjectToBeExported;
	}

	@Value("${rwanda.object.enable}")
	public void setCreateObjectEnable(boolean createObjectEnable) {
		RwandaApplication.importExportObjectEnable = createObjectEnable;
	}

	@Value("${rwanda.object.export.url}")
	public void setCreateObjectURL(String createObjectURL) {
		RwandaApplication.urlToExportObjects = createObjectURL;
	}

	@Value("${rwanda.space.spaceids}")
	public void setSpaceIds(String spaceIds) {
		RwandaApplication.spaceIds = spaceIds.trim();
	}

	@Value("${rwanda.space.enable}")
	public void setCreateSpaceEnable(boolean createSpaceEnable) {
		RwandaApplication.createSpaceEnable = createSpaceEnable;
	}

	@Value("${rwanda.space.export.url}")
	public void setUrlToFetchSpace(String urlToFetchSpace) {
		RwandaApplication.urlToFetchSpace = urlToFetchSpace;
	}

	@Value("${rwanda.space.import.url}")
	public void setUrlToFetchSpaceDest(String urlToFetchSpaceDest) {
		RwandaApplication.urlToFetchSpaceDest = urlToFetchSpaceDest;
	}

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

			log.debug("*** Creating space section start ***");
			if (createSpaceEnable) {
				try {
					if (spaceIds != null && !spaceIds.isEmpty()) {
						String[] spaceIdArray = spaceIds.split(",");
						for (String spaceId : spaceIdArray) {
							JSONObject space = callApiRequest.getSpaceBySpaceId(urlToFetchSpace, authheader,
									spaceId.trim());
							if (space != null) {
								callApiRequest.importSpace(space, urlToFetchSpaceDest, authheader);

							} else {
								log.debug("Unable to fetch space from server:");
							}
						}
					} else {
						log.debug("Waringing:: No space IDs mentioned :");
					}
				} catch (IOException e) {
					log.error("Error in space API ", e);
				} catch (Exception e) {
					log.error("Error in space API ", e);
				}
			} else {
				log.info("Create space property is disabled ");
			}

			log.debug("*** Exporting/importing Object section start ***");
			if (importExportObjectEnable) {
				try {
					if(spaceIds== null || spaceIds.isEmpty() || typesOfObjectToBeExported == null || typesOfObjectToBeExported.isEmpty()) {
						log.debug("Warning:: SpaceIDs or object Types is not mention in property file");
					} else {
						String[] spaceIdArray = spaceIds.split(",");
						for (String spaceId : spaceIdArray) {
							String[] typesArray = typesOfObjectToBeExported.split(",");
							List<String> objectTypes = new LinkedList(Arrays.asList(typesArray));							
							boolean exported = callApiRequest.exportObjects(urlToExportObjects, objectTypes,
									spaceId, ndsJsonFilePath, authheader);
							if (exported) {
								callApiRequest.importObjects(ndsJsonFilePath, spaceId, urlToImportObjects, authheader);
							}
						}
					}
				} catch (IOException e) {
					log.error("Error in object API ", e);
				} catch (Exception e) {
					log.error("Error in object API ", e);
				}
			} else {
				log.info("Export Object property is disabled ");
			}

			log.debug("***  End of execution ***");
			ctx.close();
		} catch (Exception e) {
			log.error("Exception in main : ", e);
		}
		
	}
}
