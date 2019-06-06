INSERT INTO authority  VALUES(1,'ROLE_OAUTH_ADMIN');
INSERT INTO authority VALUES(2,'ROLE_RESOURCE_ADMIN');
INSERT INTO authority VALUES(3,'ROLE_PRODUCT_ADMIN');
INSERT INTO credentials VALUES(1,'1','oauth_admin'   ,'$2a$10$PcPSN9XxabgdRLgrV0hGduQUV.bK3anhe8ypx0GZIO0/lSGwk1Wha','0');
INSERT INTO credentials VALUES(2,'1','resource_admin','$2a$10$PcPSN9XxabgdRLgrV0hGduQUV.bK3anhe8ypx0GZIO0/lSGwk1Wha','0');
INSERT INTO credentials  VALUES(3,'1','product_admin','$2a$10$PcPSN9XxabgdRLgrV0hGduQUV.bK3anhe8ypx0GZIO0/lSGwk1Wha','0');
INSERT INTO credentials_authorities VALUEs (1,1);
INSERT INTO credentials_authorities VALUEs (2,2);
INSERT INTO credentials_authorities VALUEs (3,3);


INSERT INTO oauth_client_details VALUES('curl_client','product_api', '$2a$10$PcPSN9XxabgdRLgrV0hGduQUV.bK3anhe8ypx0GZIO0/lSGwk1Wha', 'read,write', 'client_credentials', 'http://127.0.0.1', 'ROLE_PRODUCT_ADMIN', 7200, 0, NULL, 'true');
