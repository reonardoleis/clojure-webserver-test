(ns unicorn.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params]))


;;index: req -> response
;;objetivo: dada uma requisição http, retorna um status, header e um corpo.
(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "<form method='post' action='calcula'>
             a: <input type='number' name='a'>
             <br>
             +
             <br>
             b:<input type='number' name='b'>
             <br>
             <input type='submit' value='calcular'>"})



;;index: req -> response
;;objetivo: dada uma requisição http e dois parâmetros inteiros(a e b), retorna
;;um status, header e um corpo contendo a soma de a e b.
(defn calcula [req]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (str "a + b = " (+ 
                            (Integer/parseInt 
                              (second 
                                (first 
                                  (req :params)))) 

                            (Integer/parseInt 
                              (second 
                                (second 
                                  (req :params)
                                  )
                                )
                              )
                            )
                          )
                        }
                      )


;;define as rotas do servidor
(defroutes app-routes
  (GET "/" [] index)
  (GET "/index" [] index)
  (POST "/calcula" [] calcula)
  (route/not-found "404 / Not Found"))

;;middleware para pegar parâmetros passados na request
(def app
  (-> app-routes
      ring.middleware.params/wrap-params))

(defn -main 
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))] 
  (server/run-server #'app {:port port})
  (println (str "Server rodando em http://localhost:" port ""))))