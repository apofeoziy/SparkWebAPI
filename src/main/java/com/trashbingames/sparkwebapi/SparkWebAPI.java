package com.trashbingames.sparkwebapi;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SparkWebAPI extends JavaPlugin {
    public Logger logger = getLogger();
    Javalin app;
    Spark spark;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("Hello, world!");

        spark = SparkProvider.get();

        app = Javalin.create(config -> config.jsonMapper(
            new JavalinJackson().updateMapper(mapper -> {
                SimpleModule module = new SimpleModule("Serializers");
                module.addSerializer(MSPTInfo.class, new MSPTInfoSerializer());
                mapper.registerModule(module);
            })
        ));

        app.get("/api/tps", this::getTps);
        app.get("/api/mspt", this::getMspt);
        app.start(3000);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("Shutting down Spark Web API...");
        app.stop();
    }

    void getTps(Context ctx) {
        var tps = spark.tps();

        if (tps == null) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        ctx.json(new TPSInfo(
                tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10),
                tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1),
                tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5),
                tps.poll(StatisticWindow.TicksPerSecond.MINUTES_15)
            )
        );
    }

    void getMspt(Context ctx) {
        var mspt = spark.mspt();

        if (mspt == null) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        ctx.json(
                new MSPTInfo(
                        new MSPTInfo.MSPTStat(mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10)),
                        new MSPTInfo.MSPTStat(mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1))
                )
        );
    }
}
