package com.github.manolo8.simplemachines.database.dao.impl;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.DataBaseBuild;
import com.github.manolo8.simplemachines.database.dao.ChunkIDDao;
import com.github.manolo8.simplemachines.database.dao.MachineDao;
import com.github.manolo8.simplemachines.domain.fuel.FuelMachine;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.ChunkID;
import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import com.github.manolo8.simplemachines.utils.replace.Replace;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MachineDaoSQL implements MachineDao, ChunkIDDao {

    private final Replace insertQuery = new Replace("INSERT INTO machines (uuid,owner,world,facing,chunkX,chunkZ,speed,burningTime,available,base,bluePrintName) VALUES ('{uuid}','{owner}','{world}','{facing}',{chunkX},{chunkZ},{speed},{burningTime},{available},'{base}','{bluePrintName}')").compile().setValues(0);
    private final Replace updateQuery = new Replace("UPDATE machines SET speed={speed},burningTime={burningTime},available={available} WHERE uuid='{uuid}'").compile().setValues(0);
    private final Replace deleteQuery = new Replace("DELETE FROM machines WHERE uuid='{uuid}'").compile();
    private final Replace selectQuery = new Replace("SELECT * FROM machines WHERE chunkX={x} AND chunkZ={z} AND world='{world}'").compile();
    private final String selectChunkId = "SELECT chunkX,chunkZ,world,COUNT(uuid) as quantity FROM machines GROUP BY chunkX,chunkZ";
    private final DataBaseBuild build;
    private final BluePrintService bluePrintService;

    public MachineDaoSQL(DataBaseBuild build, BluePrintService bluePrintService) throws DataBaseException {
        this.build = build;
        this.bluePrintService = bluePrintService;
        try {
            Connection connection = build.getConnection();
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS machines" +
                    " (uuid          VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "  owner         VARCHAR(255) NULL," +
                    "  world         VARCHAR(255) NULL," +
                    "  facing        VARCHAR(10)   NULL," +
                    "  chunkX        INT          NULL," +
                    "  chunkZ        INT          NULL," +
                    "  speed         DOUBLE(16,2) NULL," +
                    "  burningTime   DOUBLE(16,2) NULL," +
                    "  available     DOUBLE(16,2) NULL," +
                    "  base          VARCHAR(255) NULL," +
                    "  bluePrintName VARCHAR(64)  NULL," +
                    "  CONSTRAINT table_name_uuid_uindex" +
                    "  UNIQUE (uuid))");

            statement.close();
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public List<ChunkID> findAllChunkId() {
        List<ChunkID> chunkIDS = new ArrayList<>();
        try {
            Connection connection = build.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(selectChunkId);

            while (result.next()) chunkIDS.add(chunkIdFromResultSet(result));

        } catch (Exception e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
        return chunkIDS;
    }

    @Override
    public List<Machine> loadFromChunk(int x, int z, UUID world) {
        try {
            List<Machine> machines = new ArrayList<>();

            Connection connection = build.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(selectQuery
                    .setValue("x", x)
                    .setValue("z", z)
                    .setValue("world", world)
                    .build());

            try {
                while (result.next()) machines.add(machineFromResultSet(result));
            } catch (SQLException e) {
                SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
            }

            statement.close();

            return machines;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void saveNewMachine(Machine machine) {
        if (machine.getUuid() != null) return;
        machine.setUuid(UUID.randomUUID());
        try {
            Connection connection = build.getConnection();

            Statement statement = connection.createStatement();

            insertQuery
                    .setValue("uuid", machine.getUuid())
                    .setValue("owner", machine.getOwner())
                    .setValue("world", machine.getWorld().getUID())
                    .setValue("facing", machine.getFace())
                    .setValue("chunkX", machine.getChunkX())
                    .setValue("chunkZ", machine.getChunkZ())
                    .setValue("available", machine.getAvailable())
                    .setValue("base", machine.getBase())
                    .setValue("bluePrintName", machine.getBluePrint().getName());

            if (machine instanceof FuelMachine)
                insertQuery
                        .setValue("speed", ((FuelMachine) machine).getSpeed())
                        .setValue("burningTime", ((FuelMachine) machine).getBurningTime());

            statement.executeUpdate(insertQuery.build());

            statement.close();
        } catch (Exception e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    public void saveMachine(Machine machine) {
        if (machine.getUuid() == null) return;
        try {
            Connection connection = build.getConnection();

            Statement statement = connection.createStatement();

            updateQuery
                    .setValue("available", machine.getAvailable())
                    .setValue("uuid", machine.getUuid());

            if (machine instanceof FuelMachine)
                updateQuery
                        .setValue("speed", ((FuelMachine) machine).getSpeed())
                        .setValue("burningTime", ((FuelMachine) machine).getBurningTime());

            System.out.println(updateQuery);

            statement.executeUpdate(updateQuery.build());

            statement.close();

        } catch (Exception e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    public void saveMachine(List<Machine> machines) {
        try {
            Connection connection = build.getConnection();

            Statement statement = connection.createStatement();

            for (Machine machine : machines) {
                if (machine.getUuid() == null) continue;
                updateQuery
                        .setValue("available", machine.getAvailable())
                        .setValue("uuid", machine.getUuid());

                if (machine instanceof FuelMachine)
                    updateQuery
                            .setValue("speed", ((FuelMachine) machine).getSpeed())
                            .setValue("burningTime", ((FuelMachine) machine).getBurningTime());

                System.out.println(updateQuery);

                statement.executeUpdate(updateQuery.build());
            }

            statement.close();

        } catch (Exception e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    @Override
    public void deleteMachine(UUID uuid) {
        if (uuid == null) return;
        try {
            Connection connection = build.getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate(deleteQuery.setValue("uuid", uuid).build());

            statement.close();
        } catch (Exception e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    private Machine machineFromResultSet(ResultSet result) throws SQLException {

        String bluePrintName = result.getString("bluePrintName");
        BluePrint bluePrint = bluePrintService.getBluePrint(bluePrintName);

        Machine machine = bluePrint.newInstance();
        machine.setBase(SimpleLocation.fromString(result.getString("base")));
        machine.setFace(BlockFace.valueOf(result.getString("facing")));
        machine.setChunkX(result.getInt("chunkX"));
        machine.setChunkZ(result.getInt("chunkZ"));
        machine.setOwner(UUID.fromString(result.getString("uuid")));
        machine.setWorld(Bukkit.getWorld(UUID.fromString(result.getString("world"))));
        machine.setUuid(UUID.fromString(result.getString("uuid")));
        machine.setAvailable(result.getDouble("available"));

        if (machine instanceof FuelMachine) {
            ((FuelMachine) machine).setBurningTime(result.getDouble("burningTime"));
            ((FuelMachine) machine).setSpeed(result.getDouble("speed"));
        }

        return machine;
    }

    private ChunkID chunkIdFromResultSet(ResultSet result) throws SQLException {
        ChunkID chunkID = new ChunkID();

        chunkID.setWorld(UUID.fromString(result.getString("world")));
        chunkID.setX(result.getInt("chunkX"));
        chunkID.setZ(result.getInt("chunkZ"));
        chunkID.setQuantity(result.getInt("quantity"));

        return chunkID;
    }
}
