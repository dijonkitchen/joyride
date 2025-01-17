(ns joyride.utils
  (:require ["vscode" :as vscode]
            [promesa.core :as p]
            [clojure.string :as str]))

(defn jsify [clj-thing]
  (clj->js clj-thing))

(defn cljify [js-thing]
  (js->clj js-thing :keywordize-keys true))

(defn vscode-read-uri+ [^js uri-or-path]
  (let [uri (if (string? uri-or-path)
              (vscode/Uri.file uri-or-path)
              uri-or-path)]
    (-> (p/let [_ (vscode/workspace.fs.stat uri)
                data (vscode/workspace.fs.readFile uri)
                decoder (js/TextDecoder. "utf-8")
                code (.decode decoder data)]
          code)
        (p/catch
            (fn [e]
              (js/console.error "Reading file failed: " (.-message e)))))))

(defn workspace-root []
  vscode/workspace.rootPath)

(defn info [& xs]
  (vscode/window.showInformationMessage (str/join " " (mapv str xs))))

(defn warn [& xs]
  (vscode/window.showWarningMessage (str/join " " (mapv str xs))))

(defn error [& xs]
  (vscode/window.showErrorMessage (str/join " " (mapv str xs))))
